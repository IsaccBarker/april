float pulse();
vec2 screenSpaceToWorldSpace(vec2 screen);
vec2 normalizeScreenCoords(vec2 screenCoord); // , vec3 cameraPos);
float smoothMin(float a, float b, float k);
vec3 calcNormal(vec3 pos);

%%%

/** Pulses a value based on the time from 0 to 1.
 * https://stackoverflow.com/questions/3018550/how-to-create-pulsating-value-from-0-1-0-1-0-etc-for-a-given-duration */
float pulse() {
    return 0.5*(1+sin(2 * 3.14159265359 * 1 * time));
}

/** Translates 2D screen space coordinates to world space coordinates. */
vec2 normalizeScreenCoords(vec2 screenCoord) { // , vec3 cameraPos) {
	vec2 result = 2.0 * (screenCoord / resolution.xy - 0.5);

	// I don't think this is the right way to do this, but after tinkering
	// for about 5 minutes it works :D
	// result += cameraPos;

	result.x *= resolution.x / resolution.y;

    return result;
}

/** Borrowed from https://youtu.be/Cp5WWtMoeKg?t=194 */
float smoothMin(float a, float b, float k) {
	float h = max(k - abs(a - b), 0) / k;
	return min(a, b) - h * h * h * k * 1 / 6.0;
}

vec3 calcNormal(vec3 pos) {
    float c = sdf(pos);
    vec2 eps_zero = vec2(0.001, 0.0);

    return normalize(vec3( sdf(pos + eps_zero.xyy), sdf(pos + eps_zero.yxy), sdf(pos + eps_zero.yyx) ) - c);
}

