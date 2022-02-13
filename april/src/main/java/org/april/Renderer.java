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

import org.april.Shader.*;

public class Renderer {
    // These next two blocks may seem like a bad idea, but
    // as far as I can tell, it isn't (famous last words...).
    // These values will never be changes. This is only setting
    // up a plane directly in front of the "camera" for the
    // fragment shader to take over and do the ray marching.
    final float vertices[] = {
        1f,  1f, 0.0f,
        1f, -1f, 0.0f,
       -1f, -1f, 0.0f,
       -1f,  1f, 0.0f,
    };

    final int indices[] = {
        0, 1, 3,
        1, 2, 3,
    };

    GLFWContext glfwContext;
    Program program;
    int ebo;
    int vao;
    int vbo;
	double mousePosX = 0;
	double mousePoxY = 0;

    public Renderer(GLFWContext glfwContext) {
        this.glfwContext = glfwContext;

        try (MemoryStack stack = stackPush()) {
            ebo = createEBO(stack).get(0);
            vao = createVAO(stack).get(0);
            vbo = createVBO(stack).get(0);

            bindBufferData();
            attributeArray();
        }

        createDefaultProgram();
    }

    public void renderToBuffer() { 
        double timeValue = Math.abs(Math.sin(glfwGetTime()));

		// System.out.println(glfwContext.getCamera().getViewMatrix().toString());

		program.bind();
		program.setUniform("time", (float) timeValue);
		program.setUniform("resolution", glfwContext.getWidth(), glfwContext.getHeight());
		program.setUniform("speed", (float) glfwContext.getCamera().getSpeed());
		program.setUniform("rotationMatrix", glfwContext.getCamera().getViewMatrix());
		program.setUniform("cameraPos", glfwContext.getCamera().getPosition());
		/* program.setUniform("cameraPos",
				(float) glfwContext.getCamera().getPosition().getX(),
				(float) glfwContext.getCamera().getPosition().getY(),
				(float) glfwContext.getCamera().getPosition().getZ());
		program.setUniform("cameraLook",
				(float) glfwContext.getCamera().getLookAt().getX(),
				(float) glfwContext.getCamera().getLookAt().getY()); */

        GL41.glBindVertexArray(vao);
        GL41.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        GL41.glBindVertexArray(0);
    }

	public void clearBuffer() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private IntBuffer createEBO(MemoryStack stack) {
        IntBuffer ebo = stack.mallocInt(1);
        GL41.glGenBuffers(ebo);

        GL41.glBufferData(GL41.GL_ELEMENT_ARRAY_BUFFER, indices, GL41.GL_STATIC_DRAW); 
        GL41.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        return ebo;
    }

    private IntBuffer createVAO(MemoryStack stack) {
        IntBuffer vao = stack.mallocInt(1);

        GL41.glGenVertexArrays(vao); 
        GL41.glBindVertexArray(vao.get(0));

        return vao;
    }

    private IntBuffer createVBO(MemoryStack stack) {
        IntBuffer vbo = stack.mallocInt(1);
        GL41.glGenBuffers(vbo);

        return vbo;
    }

    private void bindBufferData() {
        GL41.glBindBuffer(GL41.GL_ARRAY_BUFFER, vbo);
        GL41.glBufferData(GL41.GL_ARRAY_BUFFER, vertices, GL41.GL_STATIC_DRAW);
        GL41.glBindBuffer(GL41.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL41.glBufferData(GL41.GL_ELEMENT_ARRAY_BUFFER, indices, GL41.GL_STATIC_DRAW);
    }

    private void attributeArray() {
        GL41.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        GL41.glEnableVertexAttribArray(0);
    }

    private void createDefaultProgram() {
        Shader vertex = null;
        Shader fragment = null;

        program = new Program();

        try {
            vertex = new Shader(ShaderType.VERTEX);
			vertex.attachSource("./src/main/glsl/vertex.glsl");
            vertex.compile();

            fragment = new Shader(ShaderType.FRAGMENT);
			fragment.attachSource("./src/main/glsl/fragment.glsl");
			fragment.attachSource("./src/main/glsl/util.glsl");
			fragment.attachSource("./src/main/glsl/render.glsl");
			fragment.attachSource("./src/main/glsl/sdfs.glsl");
			// fragment.attachSource("./src/main/glsl/camera.glsl");
            fragment.compile();
        } catch (Exception e) {
            System.err.println("Failed to perform shader steps prior to program: " + e.getMessage());
            System.exit(1);
        }

        try {
            program.attachCompiledShader(vertex);
            program.attachCompiledShader(fragment);

            program.link();
        } catch (Exception e) {
            System.err.println("Failed to perform program operation: " + e.getMessage());
            System.exit(1);
        }
    }
}

