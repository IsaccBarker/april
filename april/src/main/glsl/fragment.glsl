// in vec4 vertexCoord;
out vec4 fragColor;

// We need speed.
const int numColors = 1000;

layout (std140) uniform bigColor {
	vec3 colors[numColors];
};

%%%

mat4 viewMatrix(vec3 eye, vec3 center, vec3 up) {
    // Based on gluLookAt man page
    vec3 f = normalize(center - eye);
    vec3 s = normalize(cross(f, up));
    vec3 u = cross(s, f);
    return mat4(
        vec4(s, 0.0),
        vec4(u, 0.0),
        vec4(-f, 0.0),
        vec4(0.0, 0.0, 0.0, 1)
    );
}

/** Entry. */
void main() {
	fragColor = vec4(colors[int((gl_FragCoord.y * 100) + gl_FragCoord.x)], 1.0);

	return;

	vec3 glow;
	vec3 p;
	vec3 viewDir = rayDirection(fov, resolution.xy, gl_FragCoord.xy);
    vec3 worldDir = (rotationMatrix * vec4(viewDir, 0.0)).xyz;
    Ray ray = castRay(cameraPos, worldDir);
	float dist = ray.meta.dist;
	vec3 color = ray.meta.color;
	int steps = ray.steps;

    if (rayCollided(dist)) {
        // Didn't hit anything
        fragColor = vec4(0.0, 0.0, 0.0, 1.0);

		return;
    }

    // The closest point on the surface to the eyepoint along the view ray
    /* p = cameraPos + dist * worldDir;

    vec3 K_a = vec3(1.0);
    vec3 K_d = vec3(1.0);
    vec3 K_s = vec3(1.0);
    float shininess = 50.0;

	glow = vec3(ray.steps/(MAX_MARCHING_STEPS/5));
    color = phongIllumination(K_a, K_d, K_s, shininess, p, cameraPos); */

	glow = vec3(steps/(MAX_MARCHING_STEPS/5));
	color += glow;

	// Ambient Occulusion
	color = ray.meta.color;
	color *= (1.0-vec3(steps/MAX_MARCHING_STEPS));

	// color = vec3(1.0 - float(steps)/float(MAX_MARCHING_STEPS));

	fragColor = vec4(color, 1.0);

	/* p = cameraPos + dist * worldDir;
	color = vec3((estimateNormal(p) * vec3(0.5) + vec3(0.5)));
    fragColor = vec4(color, 1.0); */
}

