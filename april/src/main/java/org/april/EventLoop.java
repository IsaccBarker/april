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
    GLFWContext glfwContext;
    Renderer renderer;

    public EventLoop(GLFWContext glfwContext, Renderer renderer) {
        this.glfwContext = glfwContext;
        this.renderer = renderer;
    }

    public void enter() {
		// Set the clear color
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(glfwContext.getWindow())) {
            frame();
        }
    }
    
    private void frame() {
        renderer.clearBuffer();
		glfwContext.getCamera().updateVectors();
        glfwContext.pollEvents();
		getInput();
		// glfwContext.getCamera().moveRotation(0.0f, 0.01f, 0.0f);
		// System.out.println(glfwContext.getCamera().getViewMatrix().toString());

        renderer.renderToBuffer();

        glfwContext.swapFrontBackBuffers();
    }

	private void getInput() {
		getKeyboardInput();
	}

	private void getKeyboardInput() {
		/* Vec3 pos = glfwContext.getCamera().getPosition();
		Vec3 up = glfwContext.getCamera().getUp();
		Vec3 front = glfwContext.getCamera().getFront();
		double speed = 0.05; */
		Camera cam = glfwContext.getCamera();
		Vector3f pos = cam.getPosition();
		double speed = cam.getSpeed();

		// System.out.println(speed);

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
			pos.z -= speed;
			// cam.offsetPosition(Camera.CameraMovement.FORWARD);
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
			glfwContext.toggleMouseCapture();
		}
	}
}
