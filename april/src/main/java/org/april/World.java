package org.april;

import org.april.Shader.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.FileNotFoundException;
import java.lang.Math;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class World {
	private static final double WORLD_WIDTH = 1;
	private static final double WORLD_HEIGHT = 1;
	private static final double WORLD_DEPTH = 1;

	private GLFWContext glfwContext;
	private ArrayList<Agent> agents;
	private Random rand = new Random();
	private Program distanceFunctions = new Program();
	private int transferFBO;
	private int transferRBO;
	private final int numAgents;

	public World(int numAgents, GLFWContext glfwContext) {
		agents = new ArrayList<Agent>(numAgents);
		this.numAgents = numAgents;
		this.glfwContext = glfwContext;

		for (int i = 0; i < numAgents; i++) {
			Agent agent = new Agent(
				WORLD_WIDTH * rand.nextDouble(),
				WORLD_HEIGHT,
				WORLD_DEPTH * rand.nextDouble()
			);

			agents.add(i, agent);
		}

		Shader shader = new Shader(ShaderType.FRAGMENT);

		try {
			shader.attachSource("./src/main/glsl/global.glsl");
			shader.attachSource("./src/main/glsl/sdfs.glsl");
			shader.attachSource("./src/main/glsl/sdfDriver.glsl");
			shader.compile();
		} catch (Exception e) {
			System.err.println("Failed to perform shader steps prior to program (world): " + e.getMessage());
            System.exit(1);
		}

		try {
			distanceFunctions.attachCompiledShader(shader);
			distanceFunctions.attachCompiledShader(SharedGL.getVertex());
			distanceFunctions.link();
		} catch (Exception e) {
			System.err.println("Failed to perform program operation (world): " + e.getMessage());
            System.exit(1);
		}

		try (MemoryStack stack = stackPush()) {
			transferFBO = GLUtil.createFBO(stack).get(0);
			transferRBO = GLUtil.createRBO(stack, GL41.GL_R16F, 27, numAgents).get(0);
		}
	}

	public void destroy() {
		GLUtil.destroyFBO(transferFBO);
	}

	public void tick() {
		distanceFunctions.bind();
		// GLUtil.bindFramebufferRenderbuffer(transferFBO, transferRBO);
		GLUtil.renderFacade();

		FloatBuffer pixels = BufferUtils.createFloatBuffer(27 * numAgents);
		GL41.glReadPixels(10, 10, 27, numAgents, GL_RED, GL_FLOAT, pixels);

		System.out.println(pixels.get(0));

		// GLUtil.unbindFramebuffer();
		/* distanceFunctions.bind();
		GLUtil.bindFramebufferRenderbuffer(transferFBO, transferRBO);
		GLUtil.clearBuffer();
		GLUtil.renderFacade(); */
	}
}

