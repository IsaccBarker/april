package org.april.Shader;

import org.lwjgl.opengl.*;

public class Program {
    private int program;
    private Shader vertexID;
    private Shader fragmentID;

    public Program() {
        program = GL41.glCreateProgram();
    }

    public void attachCompiledShader(Shader shader) throws Exception {
        switch (shader.getShaderType()) {
            case VERTEX:
                vertexID = shader;

                break;
            case FRAGMENT:
                fragmentID = shader;

                break;
            default:
                throw new InvalidShaderTypeException("Invalid shader type: " + shader);
        }

        GL41.glAttachShader(program, shader.getShader());
    }

    public void link() throws Exception {
        if (program == 0) {
            throw new ProgramCreationException("Failed to create program.");
        }

        GL41.glLinkProgram(program);

        if (GL41.glGetProgrami(program, GL41.GL_LINK_STATUS) == 0) {
            throw new ProgramLinkException("Failed to link program: " + GL41.glGetProgramInfoLog(program, 4096));
        }

        GL41.glValidateProgram(program);
        if (GL41.glGetProgrami(program, GL41.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader code: " + GL41.glGetProgramInfoLog(program, 4096));
        }
    }

    // https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glUniform.xhtml
    public void setUniform(String uniform, float a) {
        int id = getUniformID(uniform);

        GL41.glUniform1f(id, a);
    }

    // https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glUniform.xhtml
    public void setUniform(String uniform, float a, float b) {
        int id = getUniformID(uniform);

        GL41.glUniform2f(id, a, b);
    }

    // https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glUniform.xhtml
    public void setUniform(String uniform, float a, float b, float c) {
        int id = getUniformID(uniform);

        GL41.glUniform3f(id, a, b, c);
    }

    // https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glUniform.xhtml
    public void setUniform(String uniform, float a, float b, float c, float d) {
        int id = getUniformID(uniform);

        GL41.glUniform4f(id, a, b, c, d);
    }

    public void bind() {
        GL41.glUseProgram(program);
    }

    public void unbind() {
        GL41.glUseProgram(0);
    }

    public void destroy() {
        unbind();

        GL41.glDeleteProgram(program);
    }

    private int getUniformID(String uniform) {
        return GL41.glGetUniformLocation(getProgram(), uniform);
    }

    public int getProgram() {
        return program;
    }
}
