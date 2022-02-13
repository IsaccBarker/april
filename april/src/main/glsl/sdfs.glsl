float sdf(vec3 p);
float sphereSDF(vec3 p, float r);
float cubeSDF(vec3 p, float l, float w, float h);
float torusSDF(vec3 p, vec2 t);

%%%

float sdf(vec3 p) {
	// float t = torusSDF(p + vec3(0.0, 0.0, 1.0), vec2(2, 0.25)); // sphereSDF(p + vec3(0.0, 0.0, 1.0), 1);
	float t = cubeSDF(p + vec3(0.0, 0.0, 10), 1, 1, 1); // cos(time), tan(time), sin(time));
	// float t = sphereSDF(p + vec3(0.0, 0.0, 0.0), 1);

	return t;
}

float sphereSDF(vec3 p, float r) {
    return length(p) - r;
}

float cubeSDF(vec3 p, float l, float w, float h) {
    // If d.x < 0, then -1 < p.x < 1, and same logic applies to p.y, p.z
    // So if all components of d are negative, then p is inside the unit cube
    vec3 d = abs(p) - vec3(l, w, h);

    // Assuming p is inside the cube, how far is it from the surface?
    // Result will be negative or zero.
    float insideDistance = min(max(d.x, max(d.y, d.z)), 0.0);

    // Assuming p is outside the cube, how far is it from the surface?
    // Result will be positive or zero.
    float outsideDistance = length(max(d, 0.0));

    return insideDistance + outsideDistance;
}

float torusSDF(vec3 p, vec2 t) {
  vec2 q = vec2(length(p.xz)-t.x,p.y);

  return length(q)-t.y;
}

