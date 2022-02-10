#version 410 core

out vec4 fragColor;

in vec4 fragCoord;
uniform float time;
uniform vec2 resolution;

vec2 screenToWorld(vec2 screen) {
    vec2 result = 2.0 * (screen/resolution.xy - 0.5);

    result.x *= resolution.x/resolution.y;
    return result;
}

float sdf(vec2 p) {
    return p.y;
}

void main() {
	// fragColor = vec4(sin(time), time*time, sin(time)/cos(time), 1);
	// project screen coordinate into world
	vec2 p = screenToWorld(fragCoord.xy);
    
    // signed distance for scene
    float sd = sdf(p);
    
    fragColor = vec4(sd, sd, sd, 1.0);
}

