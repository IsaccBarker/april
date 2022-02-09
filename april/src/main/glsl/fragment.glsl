#version 410 core
out vec4 FragColor;

in vec4 vertexColor;
in vec4 vertexCoord;

void main() {
    float foo = vertexCoord.x + vertexCoord.y + vertexCoord.z;

    FragColor = vec4(foo/2, foo/2, foo*2, 1);
}
