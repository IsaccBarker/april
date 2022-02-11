package org.april.Shader;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;
import java.lang.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;
import java.util.Scanner;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Shader {
    private ShaderType shaderType;
    private Path shaderPath;
    private int shader;

	private static final String SHADER_PRELUDE = "#version 410 core\n";
	private ArrayList<String> shaderHeaders = new ArrayList<String>();
	private ArrayList<String> shaderSources = new ArrayList<String>();
    
    public Shader(ShaderType shaderType) throws IOException {
        this.shaderType = shaderType;
    }

    public int getShader() {
        return shader;
    }

	/** Attach a source file that the other source files can access.
	 * Each source file should provide a "header section" before the %%%s,
	 * and the actual shader code itself after them. */
	public void attachSource(String shaderPathString) throws IOException {
		Path shaderPath = Paths.get(shaderPathString);
		String shaderSource = Files.readString(shaderPath, StandardCharsets.US_ASCII);
		String[] parts = shaderSource.split("%%%");

		// TODO: Better error handling here.
		assert(parts.length == 2);

		shaderHeaders.add(parts[0]);
		shaderSources.add(parts[1]);
	}

	/** Compiles the GLSL mega-shader. */
    public void compile() throws Exception {
        String shaderSource = assembleShaderSource();

		shader = GL41.glCreateShader(shaderType.getInternalShaderType());

        if (shader == 0) {
            throw new ShaderCreationException("Failed to create shader (" +
                shaderPath + "): " + shaderType);
        }

        GL41.glShaderSource(shader, shaderSource);
        GL41.glCompileShader(shader);

        if (GL41.glGetShaderi(shader, GL41.GL_COMPILE_STATUS) == 0) {
            throw new ShaderCompileException("Failed to compile shader (" +
                shaderPath + "):\n\033[1;31m" + GL41.glGetShaderInfoLog(shader, 4096) + "\033[0m");
        }
    }

    public ShaderType getShaderType() {
        return shaderType;
    }

	/** Assemble all the shader sources into one source (mega-shader).
	 * This is, strangely enough, how you are actually suppose to do things. */
	private String assembleShaderSource() {
		StringBuilder builder = new StringBuilder();

		builder.append(SHADER_PRELUDE);

		for (String header : shaderHeaders) {
			builder.append(header);
		}

		for (String source : shaderSources) {
			builder.append(source);
		}

		return builder.toString();
	}
}
