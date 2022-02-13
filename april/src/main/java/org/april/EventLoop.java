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
        glfwContext.pollEvents();
		getInput();
		// glfwContext.getCamera().moveRotation(0.0f, 0.01f, 0.0f);
		glfwContext.getCamera().updateViewMatrix();
		System.out.println(glfwContext.getCamera().getViewMatrix().toString());

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

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addZ(-0.1);
			// pos = pos.plus(front.times(speed));
			cam.movePosition(0, 0, -0.1f);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addX(-0.1);
			// pos = pos.minus(front.cross(up).normalize().times(speed));
			cam.movePosition(-0.1f, 0, 0);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addZ(0.1);
			// pos = pos.minus(front.times(speed));
			cam.movePosition(0, 0, 0.1f);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addX(0.1);
			// pos = pos.plus(front.cross(up).normalize().times(speed));
			cam.movePosition(0.1f, 0, 0);
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_C) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addY(-0.1);
			cam.movePosition(0, -0.1f, 0);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addY(0.1);
			cam.movePosition(0, 0.1f, 0);
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_Q) == GLFW_PRESS) {
			cam.moveRotation(0.0f, 0.5f, 0.0f);
		}
	}
}
