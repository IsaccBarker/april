package org.april;

public class April {
	public static void main(String[] args) {
		GLFWContext glfwContext = new GLFWContext();
		Renderer renderer = new Renderer(glfwContext);
		EventLoop eventLoop = new EventLoop(glfwContext, renderer);

		eventLoop.enter();

		glfwContext.destroy();
	}
}

