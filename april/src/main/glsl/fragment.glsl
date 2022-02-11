out vec4 fragColor;
// in vec4 vertexCoord;

uniform float time;
uniform vec2 resolution;

%%%

/** Entry. */
void main() {
	/* vec2 p = screenSpaceToWorldSpace(gl_FragCoord.xyz);
	float sd = sdf(p);
	vec3 col = shade(sd);
	
	fragColor = vec4(col, 1.0); */

	vec3 camPos = vec3(0, 0, -1);
    vec3 camTarget = vec3(0.0, 0.0, 0.0);

    vec2 uv = normalizeScreenCoords(gl_FragCoord.xy); // , cameraPos);
    vec3 rayDir = getDirCameraRay(uv, camPos, camTarget);

	camPos += cameraPos;

    vec3 col = render(camPos, rayDir);

    fragColor = vec4(col, 1); // Output to screen
}

