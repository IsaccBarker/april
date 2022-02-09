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

        renderer.renderToBuffer();

        glfwContext.swapFrontBackBuffers();
    }
}
