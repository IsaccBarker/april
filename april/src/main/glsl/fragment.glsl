#version 410 core

out vec4 fragColor;

in vec4 vertexCoord;
uniform float time;
uniform vec2 resoulution;

#define MAX_MARCHING_STEPS 255
#define MIN_DIST 0.0
#define MAX_DIST 100.0
#define EPSILON 0.0001

float sphereSDF(vec3 samplePoint) {
    return length(samplePoint) - 1.0;
}

float sceneSDF(vec3 samplePoint) {
    return sphereSDF(samplePoint);
}

float shortestDistanceToSurface(vec3 eye, vec3 marchingDirection, float start, float end) {
	float depth = start;
	
	for (int i = 0; i < MAX_MARCHING_STEPS; i++) {
		float dist = sceneSDF(eye + depth * marchingDirection);
		
		if (dist < EPSILON) {
			return depth;
		}
		
		depth += dist;
		
		if (depth >= end) {
			return end;
		}
	}
	
	return end;
}

vec3 rayDirection(float fieldOfView, vec2 size, vec2 fragCoord) {
	vec2 xy = fragCoord - size / 2.0;
	float z = size.y / tan(radians(fieldOfView) / 2.0);
	
	return normalize(vec3(xy, -z));
}

void main() {
	// FragColor = vec4(sin(time), time*time, sin(time)/cos(time), 1);


	vec3 dir = rayDirection(45.0, vec2(1000, 1000), vertexCoord.xy);
	vec3 eye = vec3(0.0, 0.0, 5.0);
	float dist = shortestDistanceToSurface(eye, dir, MIN_DIST, MAX_DIST);

	if (dist > MAX_DIST - EPSILON) {
		// Didn't hit anything
		fragColor = vec4(0.0, 1.0, 0.0, 1.0);
		return;
	}

	fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}

