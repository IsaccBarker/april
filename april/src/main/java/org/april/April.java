package org.april;

public class April {
	public static void main(String[] args) {
		GLFWContext glfwContext = new GLFWContext();
		SharedGL.init();
		Renderer renderer = new Renderer(glfwContext);
		World world = new World(100, glfwContext);
		EventLoop eventLoop = new EventLoop(glfwContext, renderer, world);

		eventLoop.enter();

		// Goodbyte!
		//     Originally a typo, but it makes for a good pun :D
		world.destroy();
		glfwContext.destroy();
	}
}

