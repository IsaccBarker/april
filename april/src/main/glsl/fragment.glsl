#version 410 core

out vec4 fragColor;

in vec4 fragCoord;
uniform float time;
uniform vec2 resolution;

void main() {
	fragColor = vec4(sin(time), time*time, sin(time)/cos(time), 1);
}

