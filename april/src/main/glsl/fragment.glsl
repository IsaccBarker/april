out vec4 fragColor;
// in vec4 vertexCoord;

uniform float time;
uniform vec2 resolution;

%%%

/** Entry. */
void main() {
	/* vec3 dir = rayDirection(zoom, resolution.xy, gl_FragCoord.xy);
	vec3 eye = vec3(0.0, cameraPos.x, cameraPos.z);
    float dist = castRay(eye, dir);

    if (rayCollided(dist)) {
        // Didn't hit anything
        fragColor = vec4(0.0, 0.0, 0.0, 1.0); // vec4(dist, 0.0, dist, 1.0);

		return;
    }

	// fragColor = vec4(1.0, 0.0, 1.0, 1.0); // vec4(vec3(1.0 - dist * 0.13), 1.0);

    // The closest point on the surface to the eyepoint along the view ray
    vec3 p = eye + dist * dir;

    vec3 K_a = vec3(0.2, 0.2, 0.2);
    vec3 K_d = vec3(0.7, 0.2, 0.2);
    vec3 K_s = vec3(1.0, 1.0, 1.0);
    float shininess = 10;

    vec3 color = phongIllumination(K_a, K_d, K_s, shininess, p, eye);

    fragColor = vec4(color, 1.0); */

	/* vec3 dir = rayDirection(45.0, resolution.xy, gl_FragCoord.xy);
    vec3 eye = vec3(0.0, 0.0, 5.0);
    float dist = castRay(eye, dir);
   
	if (rayCollided(dist)) {
        // Didn't hit anything
        fragColor = vec4(0.0, 0.0, 0.0, 0.0);
		return;
    }
    
    // The closest point on the surface to the eyepoint along the view ray
    vec3 p = eye + dist * dir;
    
    vec3 K_a = vec3(0.2, 0.2, 0.2);
    vec3 K_d = vec3(0.7, 0.2, 0.2);
    vec3 K_s = vec3(1.0, 1.0, 1.0);
    float shininess = 10.0;
    
    vec3 color = phongIllumination(K_a, K_d, K_s, shininess, p, eye);
    
    fragColor = vec4(color, 1.0); */

	// vec2 uv = (gl_FragCoord.xy - 0.5 * resolution.xy) / resolution.y;
	vec2 uv = gl_FragCoord.st / resolution.xy;
	vec3 col = vec3(1);
	vec3 ro = vec3(cameraPos.x, cameraPos.y, cameraPos.z - zoom); // ray origin that represents camera position
	vec3 rd = normalize(vec3(uv, -1)); // ray direction

	fragColor = vec4(uv.x,uv.y,0.0,1.0);
	return;

	rd *= rotateY(-cameraLook.x/100);

	float dist = castRay(ro, rd);

	if (rayCollided(dist)) {
		fragColor = vec4(vec3(0.30, 0.36, 0.60) - (rd.y * 0.7), 1.0); // vec4(0.0, 0.0, 0.0, 0.0);

		return;
	}

	fragColor = vec4(vec3(1.0 - dist * 0.075), 1.0);
}

