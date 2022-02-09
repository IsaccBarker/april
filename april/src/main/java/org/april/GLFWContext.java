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

public class GLFWContext {
	private long window;
	private GLFWVidMode monitorResolution;

	public long getWindow() {
		return window;
	}

    public GLFWContext() {
        setGLFWErrorCallback();
		initGLFW();
		setGLFWHints();
		initWindow();
		setGLFWCurrentContext();
		setMonitorResolution();
		setInputCallback();
		setFramebufferResizeCallback();
		pushFrame();
		setWindowVisibility(true);
		createCapabilities();
    }

    public void destroy() {
        // Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
    }

	public void pollEvents() {
		glfwPollEvents();
	}

	public void swapFrontBackBuffers() {
		glfwSwapBuffers(window);
	}

	public GLFWVidMode getMonitorResolution() {
		return monitorResolution;
	}

	private void setMonitorResolution() {
		monitorResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());
	}

	private void setGLFWErrorCallback() {
		GLFWErrorCallback.createPrint(System.err).set();
	}

	private void initGLFW() {
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
        }
	}

	private void setGLFWHints() {
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Force OpenGL 4
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	}

	private void initWindow() {
		window = glfwCreateWindow(1000, 1000, "April Ray Marching", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
        }
	}

	private void setInputCallback() {
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
	}

	private void setFramebufferResizeCallback() {
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			glViewport(0, 0, width, height);
		});
	}

	private void pushFrame() {
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode monitorResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(monitorResolution.width() - pWidth.get(0)) / 2,
				(monitorResolution.height() - pHeight.get(0)) / 2
			);
		}
	}

	private void setGLFWCurrentContext() {
		glfwMakeContextCurrent(window);
	}

	private void setWindowVisibility(boolean visible) {
		if (visible) {
			glfwShowWindow(window);
		} else {
			glfwHideWindow(window);
		}
	}

	private void createCapabilities() {
		// Detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	}
}
