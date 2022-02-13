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
        glfwContext.pollEvents();
		getInput();
		glfwContext.getCamera().updateVectors();
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
		Camera.CameraMovement type = null;

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addZ(-0.1);
			// pos = pos.plus(front.times(speed));
			// pos.z -= 0.1;
			type = Camera.CameraMovement.FORWARD;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addX(-0.1);
			// pos = pos.minus(front.cross(up).normalize().times(speed));
			// pos.x -= 0.1;
			type = Camera.CameraMovement.LEFT;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addZ(0.1);
			// pos = pos.minus(front.times(speed));
			// pos.z += 0.1;
			type = Camera.CameraMovement.BACKWARD;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addX(0.1);
			// pos = pos.plus(front.cross(up).normalize().times(speed));
			// pos.x += 0.1;
			type = Camera.CameraMovement.RIGHT;
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_C) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addY(-0.1);
			// pos.y -= 0.1;
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
			// glfwContext.getCamera().getPosition().addY(0.1);
			// pos.y += 0.1;
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_Q) == GLFW_PRESS) {
			cam.addYaw(1f);
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_E) == GLFW_PRESS) {
			cam.addYaw(-1f);
		}

		if (type != null) {
			cam.offsetPosition(0.1f, type);
		}
	}
}
