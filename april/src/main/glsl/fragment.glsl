#version 410 core

out vec4 fragColor;

in vec4 fragCoord;
in vec4 vertexCoord;
uniform float time;
uniform vec2 resolution;

void main() {
	// fragColor = vec4(sin(time), time*time, sin(time)/cos(time), 1);
	fragColor = vec4(resolution/2000, 0.0, 1.0);
}

