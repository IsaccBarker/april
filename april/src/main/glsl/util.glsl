vec2 screenSpaceToWorldSpace(vec2 screen);
float sminCubic(float a, float b, float k);
float opBlend(float d1, float d2);
vec3 palette(in float t, in vec3 a, in vec3 b, in vec3 c, in vec3 d);
vec3 shade(float sd);
vec2 normalizeScreenCoords(vec2 screenCoord);

%%%

/** Translates 2D screen space coordinates to world space coordinates. */
vec2 screenSpaceToWorldSpace(vec2 screen) {
    vec2 result = 2.0 * (screen / resolution.xy - 0.5);
    result.x *= resolution.x / resolution.y;

    return result;
}

/** Polynomial smooth min (k = 0.1); */
float sminCubic(float a, float b, float k) {
    float h = max(k-abs(a-b), 0.0);
    return min(a, b) - h*h*h/(6.0*k*k);
}

/** Smooth blending. */
float opBlend(float d1, float d2) {
    float k = 0.2;
    return sminCubic(d1, d2, k);
}

/** Colour palette shit.
 * https://www.shadertoy.com/view/ll2GD3 */
vec3 palette(in float t, in vec3 a, in vec3 b, in vec3 c, in vec3 d) {
	t = clamp(t, 0.0, 1.0);
	return a + b * cos(6.28318 * (c * t + d));
}

/** Shade the visualization to make debugging easier. */
vec3 shade(float sd) {
	float maxDist = 2.0;
	vec3 palCol = palette(clamp(0.5 - sd * 0.4, -maxDist, maxDist), 
							vec3(0.3, 0.3, 0.0), vec3(0.8, 0.8, 0.1), vec3(0.9, 0.7, 0.0), vec3(0.3, 0.9, 0.8));

	vec3 col = palCol;

	col = mix(col, col * 1.0 - exp(-10.0 * abs(sd)), 0.4);
	col *= 0.8 + 0.2 * cos(150.0 * sd);
	col = mix(col, vec3(1.0), 1.0 - smoothstep(0.0, 0.01, abs(sd)));

	return col;
}

vec2 normalizeScreenCoords(vec2 screenCoord) {
	vec2 result = 2.0 * (screenCoord / resolution.xy - 0.5);

	result.x *= resolution.x / resolution.y; // Correct for aspect ratio
    return result;
}

