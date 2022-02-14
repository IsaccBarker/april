out vec4 fragColor;

uniform float time;
uniform vec2 resolution;

struct DistanceMeta {
	float dist;
	vec3 color;
};

struct Ray {
	int steps;
	DistanceMeta meta;
};

%%%
