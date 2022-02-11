vec3 getDirCameraRay(vec2 uv, vec3 pos, vec3 targ);
float castRay(vec3 origin, vec3 dir);
vec3 render(vec3 origin, vec3 dir);

uniform vec3 cameraPos;

%%%

const float RAY_INTERCEPT_DISTANCE = 0.0001;
const int MAX_RAY_MARCHES = 64;

vec3 getDirCameraRay(vec2 uv, vec3 pos, vec3 targ) {
	// Calculate camera's "orthonormal basis", i.e. its transform matrix components
    vec3 camForward = normalize(targ - pos);
    vec3 camRight = normalize(cross(vec3(0.0, 1.0, 0.0), camForward));
    vec3 camUp = normalize(cross(camForward, camRight));

    float fPersp = 2.0;
    vec3 vDir = normalize(uv.x * camRight + uv.y * camUp + camForward * fPersp);

    return vDir;
}

/** Casts ray and returns the intercept distance.
 * Returns -1 if nothing was hit. We can draw trippy
 * background effects with this :D */
float castRay(vec3 origin, vec3 dir) {
    float t = 0.0;

    for (int i = 0; i < MAX_RAY_MARCHES; i++) {
        float res = sdf(origin + dir * t);

        if (res < (RAY_INTERCEPT_DISTANCE * t)) {
            return t;
        }

        t += res;
    }

    return -1.0;
}

vec3 render(vec3 origin, vec3 dir) {
    float t = castRay(origin, dir);
	vec3 col;

	// Did we hit nothing?
	if (t == -1) {
		// Draw a neat background. Looked around, and this is
		// what a lot of 3D design software use.
		col = vec3(0.30, 0.36, 0.60) - (dir.y * 0.7);

		return col;
	}

    // Visualize depth. Borrowed, could use some work.
	vec3 L = normalize(vec3(sin(time)*1.0, cos(time*0.5)+0.5, -0.5));
	vec3 pos = origin + dir * t;
	vec3 normal = calcNormal(pos);
	vec3 objectSurfaceColour = vec3(0.4, 0.8, 0.1);
	float NoL = max(dot(normal, L), 0.0);
	vec3 LDirectional = vec3(1.80,1.27,0.99) * NoL;
	vec3 LAmbient = vec3(0.03, 0.04, 0.1);
	vec3 diffuse = objectSurfaceColour * (LDirectional + LAmbient);

	col = diffuse;

	float shadow = 0.0;
	vec3 shadowRayOrigin = pos + normal * 0.01;
	vec3 shadowRayDir = L;
	t = castRay(shadowRayOrigin, shadowRayDir);

	if (t >= -1.0) {
		shadow = 1.0;
	}

	col = mix(col, col*0.8, shadow);

    return col;
}

