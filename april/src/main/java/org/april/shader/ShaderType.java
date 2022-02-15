package org.april.shader;

import org.lwjgl.opengl.*;

public enum ShaderType {
    VERTEX(GL41.GL_VERTEX_SHADER), FRAGMENT(GL41.GL_FRAGMENT_SHADER);

    public final int internalShaderType;

    private ShaderType(int internalShaderType) {
        this.internalShaderType = internalShaderType;
    }

    public int getInternalShaderType() {
        return internalShaderType;
    }
}
