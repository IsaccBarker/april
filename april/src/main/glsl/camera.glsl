vec3 getDirCameraRay(vec2 uv, vec3 pos, vec3 targ);
float castRay(vec3 origin, vec3 dir);
vec3 render(vec3 origin, vec3 dir);

%%%

vec3 getDirCameraRay(vec2 uv, vec3 pos, vec3 targ) {
	// Calculate camera's "orthonormal basis", i.e. its transform matrix components
    vec3 camForward = normalize(targ - pos);
    vec3 camRight = normalize(cross(vec3(0.0, 1.0, 0.0), camForward));
    vec3 camUp = normalize(cross(camForward, camRight));

    float fPersp = 2.0;
    vec3 vDir = normalize(uv.x * camRight + uv.y * camUp + camForward * fPersp);

    return vDir;
}

float castRay(vec3 origin, vec3 dir) {
    float t = 0.0;

    for (int i = 0; i < 64; i++) {
        float res = sdf(origin + dir * t);

        if (res < (0.0001 * t)) {
            return t;
        }

        t += res;
    }

    return -1.0;
}

vec3 render(vec3 origin, vec3 dir) {
    float t = castRay(origin, dir);

    // Visualize depth
    vec3 col = vec3(1.0 - t * 0.075);

    return col;
}

