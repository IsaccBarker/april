package org.april;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.joml.Vector2f;
import org.joml.Vector2d;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/** A wrapper around GLFW related objects.
 * Most of this code is boilerplate taken and modified from here.
 * Note that the following links are for C++, not Java, so I figured
 * it was fair to use:
 *     https://learnopengl.com/Getting-started/Creating-a-window
 *     https://learnopengl.com/Getting-started/Hello-Window */
public class GLFWContext {
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 1000;

	private GLFWVidMode monitorResolution;
	private Camera camera = new Camera();
	private long window;
	private int width;
	private int height;
	private boolean mouseCapture = false;
	private double last;

	public long getWindow() {
		return window;
	}

    public GLFWContext() {
        setGLFWErrorCallback();
		initGLFW();
		setGLFWHints();
		initWindow();
		setGLFWCurrentContext();
		setFramebufferResizeCallback();
		setCursorMoveCallback();
		setScrollCallback();
		setMonitorResolution();
		toggleMouseCapture();
		setInputCallback();
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

	public Camera getCamera() {
		return camera;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getLast() {
		return last;
	}

	public void setLast(double l) {
		last = l;
	}

	public void toggleMouseCapture() {
		mouseCapture = !mouseCapture;

		if (mouseCapture) {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		} else {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		}
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
		window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "April Ray Marching", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
        }

		this.width = WINDOW_WIDTH;
		this.height = WINDOW_HEIGHT;
	}
	
	private void setInputCallback() {
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
	}

	private void setCursorMoveCallback() {
		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			xpos /= 15;
			ypos /= 15;

			Vector2f prev = camera.getPrevMouse();

			camera.addYaw(prev.x - xpos);
			camera.addPitch(prev.y - ypos);

			prev.x = (float) xpos;
			prev.y = (float) ypos;

			/* Vector2d previous = camera.getPrevVec();
			Vector2f display = camera.getDisplVec();

			display.x = 0;
			display.y = 0;

			if (previous.x > 0 && previous.y > 0) {
				double deltax = xpos - previous.x;
				double deltay = ypos - previous.y;
				boolean rotateX = deltax != 0;
				boolean rotateY = deltay != 0;

				if (rotateX) {
					display.y = (float) deltax;
				}
				if (rotateY) {
					display.x = (float) deltay;
				}
			}

			previous.x = xpos;
			previous.y = ypos;

			camera.moveRotation(display.x / 25, display.y / 25, 0); */
		});
	}

	private void setFramebufferResizeCallback() {
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;

			glViewport(0, 0, width, height);
		});
	}

	// There is no way to not use a callback.
	private void setScrollCallback() {
		glfwSetScrollCallback(window, (window, x, y) -> {
			if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
				camera.addFOV((y/25));
			} else {
				camera.addSpeed(-(y/25));
			}
		});
	}

	private void pushFrame() {
		try (MemoryStack stack = stackPush()) {
			// Get the resolution of the primary monitor
			monitorResolution = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(monitorResolution.width() - width) / 2,
				(monitorResolution.height() - height) / 2
			);
		}
	}

	private void setGLFWCurrentContext() {
		glfwMakeContextCurrent(window);
	}

	private void setWindowVisibility(boolean visible) {
		if (visible) {
			glfwShowWindow(window);

			return;
		}

		glfwHideWindow(window);
	}

	private void createCapabilities() {
		// Detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
	}
}
