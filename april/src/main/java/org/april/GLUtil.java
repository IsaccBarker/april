package org.april;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GLUtil {
	public static int ebo;
    public static int vao;
    public static int vbo;
    public GLFWContext glfwContext;

	public static final float vertices[] = {
        1f,  1f, 0.0f,
        1f, -1f, 0.0f,
       -1f, -1f, 0.0f,
       -1f,  1f, 0.0f,
    };

    public static final int indices[] = {
        0, 1, 3,
        1, 2, 3,
    };

	public static void clearBuffer() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void createEBO(MemoryStack stack, int[] indices) {
        IntBuffer ebo = stack.mallocInt(1);
        GL41.glGenBuffers(ebo);

        GL41.glBufferData(GL41.GL_ELEMENT_ARRAY_BUFFER, indices, GL41.GL_STATIC_DRAW); 
        GL41.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

		GLUtil.ebo = ebo.get(0);
    }

    public static void createVAO(MemoryStack stack) {
        IntBuffer vao = stack.mallocInt(1);

        GL41.glGenVertexArrays(vao); 
        GL41.glBindVertexArray(vao.get(0));

    	GLUtil.vao = vao.get(0);
	}

    public static void createVBO(MemoryStack stack) {
        IntBuffer vbo = stack.mallocInt(1);
        GL41.glGenBuffers(vbo);

    	GLUtil.vbo = vbo.get(0);
	}

	public static int createPBO(MemoryStack stack) {
		IntBuffer pbo = stack.mallocInt(1);
		GL41.glGenBuffers(pbo);
		// GL41.glBufferData(GL41.GL_PIXEL_PACK_BUFFER, 500*1000*3, null, GL41.GL_STREAM_COPY);

		return pbo.get(0);
	}

	public static void bindPBO(int pbo) {
		GL41.glBindBuffer(GL41.GL_PIXEL_PACK_BUFFER, pbo);
	}

    public static void bindBufferData() {
        GL41.glBindBuffer(GL41.GL_ARRAY_BUFFER, vbo);
        GL41.glBufferData(GL41.GL_ARRAY_BUFFER, vertices, GL41.GL_STATIC_DRAW);
        GL41.glBindBuffer(GL41.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL41.glBufferData(GL41.GL_ELEMENT_ARRAY_BUFFER, indices, GL41.GL_STATIC_DRAW);
    }

    public static void attributeArray() {
        GL41.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        GL41.glEnableVertexAttribArray(0);
    }

	public static void renderFacade() {
		bindBufferData();
        attributeArray();

        GL41.glBindVertexArray(GLUtil.vao);
        GL41.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        GL41.glBindVertexArray(0);
	}
}

