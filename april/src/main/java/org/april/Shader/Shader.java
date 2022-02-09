package org.april.Shader;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
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
    private String shaderSource = new String();
    private int shader;
    
    public Shader(ShaderType shaderType, String shaderPathString) throws IOException {
        this.shaderType = shaderType;

        shaderPath = Paths.get(shaderPathString);
        shaderSource = Files.readString(shaderPath, StandardCharsets.US_ASCII);
    }

    public int getShader() {
        return shader;
    }

    public void compile() throws Exception {
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
}
