out vec4 fragColor;
// in vec4 vertexCoord;

uniform float time;
uniform vec2 resolution;

%%%

/** Entry. */
void main() {
	vec2 uv = (gl_FragCoord.xy - 0.5 * resolution.xy) / resolution.y;
	vec3 col = vec3(1);
	vec3 ro = vec3(cameraPos.x, cameraPos.y, cameraPos.z - zoom); // ray origin that represents camera position
	vec3 rd = normalize(vec3(uv, -1)); // ray direction

	rd *= rotateY(-cameraLook.x/100);
	rd *= rotateX(-cameraLook.y/100);

	float dist = castRay(ro, rd);

	if (rayCollided(dist)) {
		fragColor = vec4(vec3(0.30, 0.36, 0.60) - (rd.y * 0.7), 1.0); // vec4(0.0, 0.0, 0.0, 0.0);

		return;
	}

	fragColor = vec4(vec3(1.0 - dist * 0.075), 1.0);
}

