#version 410 core
out vec4 FragColor;

in vec4 vertexColor;
in vec4 vertexCoord;
uniform float time;

void main() {
    float foo = vertexCoord.x + vertexCoord.y + vertexCoord.z;

    // FragColor = vec4(time/2, foo*2, foo/time, 1);
	FragColor = vec4(sin(time), time*time, sin(time)/cos(time), 1);
}

