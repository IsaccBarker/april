vec3 getDirCameraRay(vec2 uv, vec3 pos, vec3 targ);
float castRay(vec3 origin, vec3 dir);
vec3 render(vec3 origin, vec3 dir);

uniform vec3 cameraPos;
uniform vec2 cameraLook;

%%%

const float RAY_INTERCEPT_DISTANCE = 0.000001;
const int MAX_RAY_MARCHES = 64;

vec3 getDirCameraRay(vec2 uv, vec3 pos, vec3 targ) {
	// pos += cameraPos;
	// targ -= cameraPos;

	// Calculate camera's "orthonormal basis", i.e. its transform matrix components
    vec3 forward = normalize(targ - pos);
	vec3 right = normalize(cross(vec3(0.0, 1.0, 0.0), forward));
    vec3 up = normalize(cross(forward, right));

    float fPersp = 2.0;
    vec3 vDir = normalize(uv.x * right + uv.y * up + forward * fPersp);

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
		// return vec3(dir.x+pulse(), dir.y-pulse(), dir.z*pulse());
		return vec3(dir.x, dir.y, dir.z);
	}

	vec3 L = normalize(vec3(sin(time)*1.0, cos(time*0.5)+0.5, -0.5));
	vec3 pos = origin + dir * t;
	vec3 normal = calcNormal(pos);
	vec3 objectSurfaceColour = vec3(1.0, 1.0, 1.0);
	float NoL = max(dot(normal, L), 0.0);
	vec3 LDirectional = vec3(0.0,0.0,1.0) * NoL;
	vec3 LAmbient = vec3(0.5, 0.25, 0.15);
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

