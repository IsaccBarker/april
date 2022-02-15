package org.april;

import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import org.april.initial.*;

public class April {
	public static void main(String[] args) {
		GLFWContext glfwContext = new GLFWContext();

		GLUtil.init();

		Renderer renderer = new Renderer(glfwContext);
		EventLoop eventLoop = new EventLoop(glfwContext, renderer);

		eventLoop.enter();

		// Goodbyte!
		//     Originally a typo, but it makes for a good pun :D
		glfwContext.destroy();
	}
}

