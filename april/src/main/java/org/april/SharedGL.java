package org.april;

import org.april.Shader.*;

public class SharedGL {
	static Shader vertex;

	public static void init() {
		try {
            vertex = new Shader(ShaderType.VERTEX);
			vertex.attachSource("./src/main/glsl/vertex.glsl");
            vertex.compile();
        } catch (Exception e) {
            System.err.println("Failed to perform shader steps prior to program (vertex): " + e.getMessage());
            System.exit(1);
        }
	}

	public static Shader getVertex() {
		return vertex;
	}
}

