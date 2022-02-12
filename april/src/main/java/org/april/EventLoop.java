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

        renderer.renderToBuffer();

        glfwContext.swapFrontBackBuffers();
    }

	private void getInput() {
		getKeyboardInput();
		getMouseInput();
	}

	private void getKeyboardInput() {
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
			glfwContext.getCamera().getPosition().addZ(-0.1);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
			glfwContext.getCamera().getPosition().addX(-0.1);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
			glfwContext.getCamera().getPosition().addZ(0.1);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
			glfwContext.getCamera().getPosition().addX(0.1);
		}

		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_C) == GLFW_PRESS) {
			glfwContext.getCamera().getPosition().addY(-0.1);
		}
		
		if (glfwGetKey(glfwContext.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
			glfwContext.getCamera().getPosition().addY(0.1);
		}
	}

	private void getMouseInput() {
		try (MemoryStack stack = stackPush()) {
			DoubleBuffer x = stack.mallocDouble(1);
			DoubleBuffer y = stack.mallocDouble(1);

			glfwGetCursorPos(glfwContext.getWindow(), x, y);

			glfwContext.getCamera().getLookAt().setX(x.get(0));
			glfwContext.getCamera().getLookAt().setY(y.get(0));
		}
	}
}
