package org.april;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.joml.Vector3f;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class EventLoop {
    private GLFWContext glfwContext;
    private Renderer renderer;
	private World world;
	private char lastKey = ' ';

    public EventLoop(GLFWContext glfwContext, Renderer renderer, World world) {
        this.glfwContext = glfwContext;
        this.renderer = renderer;
		this.world = world;
    }

    public void enter() {
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

		while (!glfwWindowShouldClose(glfwContext.getWindow())) {
			world.tick();
			frame();
        }
    }
    
    private void frame() {
        GLUtil.clearBuffer();
		glfwContext.getCamera().updateVectors();
        glfwContext.pollEvents();
		getInput();

        renderer.renderToBuffer();

        glfwContext.swapFrontBackBuffers();
    }

	private void getInput() {
		getKeyboardInput();
	}

	private void getKeyboardInput() {
		Camera cam = glfwContext.getCamera();
		Vector3f pos = cam.getPosition();
		double speed = cam.getSpeed();

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
			pos.z -= speed;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
			pos.x -= speed;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
			pos.z += speed;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
			pos.x += speed;
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_C) == GLFW_PRESS) {
			pos.y -= speed;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
			pos.y += speed;
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_M) == GLFW_PRESS) {
			if (glfwGetTime() - glfwContext.getLast() < 100) {
				glfwContext.toggleMouseCapture();

				glfwContext.setLast(glfwGetTime());
			}
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_0) == GLFW_PRESS) {
			cam.reset();
		}
	}
}
