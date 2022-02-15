package org.april;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.FileNotFoundException;
import java.lang.Math;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.april.shader.*;

public class Renderer {
    // These next two blocks may seem like a bad idea, but
    // as far as I can tell, it isn't (famous last words...).
    // These values will never be changes. This is only setting
    // up a plane directly in front of the "camera" for the
    // fragment shader to take over and do the ray marching.
    GLFWContext glfwContext;
    Program program;
	double mousePosX = 0;
	double mousePoxY = 0;

    public Renderer(GLFWContext glfwContext) {
        this.glfwContext = glfwContext;

        try (MemoryStack stack = stackPush()) {
            GLUtil.createEBO(stack, GLUtil.indices);
            GLUtil.createVAO(stack);
            GLUtil.createVBO(stack);
        }

        createDefaultProgram();
    }

    public void renderToBuffer() { 
        double timeValue = glfwGetTime();

		// System.out.println(glfwContext.getCamera().getViewMatrix().toString());

		program.bind();
		program.setUniform("time", (float) timeValue);
		program.setUniform("resolution", glfwContext.getWidth(), glfwContext.getHeight());
		program.setUniform("speed", (float) glfwContext.getCamera().getSpeed());
		program.setUniform("rotationMatrix", glfwContext.getCamera().getViewMatrix());
		program.setUniform("cameraPos", glfwContext.getCamera().getPosition());
		program.setUniform("fov", glfwContext.getCamera().getFOV());
		/* program.setUniform("cameraPos",
				(float) glfwContext.getCamera().getPosition().getX(),
				(float) glfwContext.getCamera().getPosition().getY(),
				(float) glfwContext.getCamera().getPosition().getZ());
		program.setUniform("cameraLook",
				(float) glfwContext.getCamera().getLookAt().getX(),
				(float) glfwContext.getCamera().getLookAt().getY()); */

		GLUtil.renderFacade();
	}

    private void createDefaultProgram() {
        Shader vertex = null;
        Shader fragment = null;

        program = new Program();

        try {
            fragment = new Shader(ShaderType.FRAGMENT);
			fragment.attachSource("./src/main/glsl/global.glsl");
			fragment.attachSource("./src/main/glsl/fragment.glsl");
			fragment.attachSource("./src/main/glsl/util.glsl");
			fragment.attachSource("./src/main/glsl/render.glsl");
			fragment.attachSource("./src/main/glsl/sdfs.glsl");
			// fragment.attachSource("./src/main/glsl/camera.glsl");
            fragment.compile();
        } catch (Exception e) {
            System.err.println("Failed to perform shader steps prior to program (marching): " + e.getMessage());
            System.exit(1);
        }

        try {
            program.attachCompiledShader(GLUtil.getVertex());
            program.attachCompiledShader(fragment);

            program.link();
        } catch (Exception e) {
            System.err.println("Failed to perform program operation (marching): " + e.getMessage());
            System.exit(1);
        }
    }
}

