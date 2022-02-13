out vec4 fragColor;
// in vec4 vertexCoord;

uniform float time;
uniform vec2 resolution;

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
	vec3 viewDir = rayDirection(45+zoom, resolution.xy, gl_FragCoord.xy);
    vec3 worldDir = (view * vec4(viewDir, 0.0)).xyz;
    float dist = castRay(cameraPos, worldDir);

    if (rayCollided(dist)) {
        // Didn't hit anything
        fragColor = vec4(0.0, 0.0, 0.0, 0.0);

		return;
    }

    // The closest point on the surface to the eyepoint along the view ray
    vec3 p = cameraPos + dist * worldDir;

    vec3 K_a = vec3(0.2, 0.2, 0.2);
    vec3 K_d = vec3(0.7, 0.2, 0.2);
    vec3 K_s = vec3(1.0, 1.0, 1.0);
    float shininess = 100.0;

    vec3 color = phongIllumination(K_a, K_d, K_s, shininess, p, cameraPos);

    fragColor = vec4(color, 1.0);
}

