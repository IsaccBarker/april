// float sdCircle(vec2 p, vec2 o, float r);

float sdf(vec3 pos);
float sdSphere(vec3 p, float r);
float sdCutHollowSphere(vec3 p, float r, float h, float t);
float sdLink(vec3 p, float le, float r1, float r2);

%%%

float sdf(vec3 pos) {

    // float t = sdSphere(pos - vec3(0.0, 0.0, 10.0), 3.0);
	float t = sdCutHollowSphere(pos - vec3(0.0, 0.0, abs(sin(time)*10+5)), 1.0, 1.0, 1.0);
	t = smoothMin(t, sdLink(pos - vec3(0.0, 0.0, 10.0), 1.0, 3.0, 1.0), 2.5);

    return t;
}

/* float sdSphere(vec3 p, float r) {
	return length(p) - r;
} */

float sdCutHollowSphere(vec3 p, float r, float h, float t) {
  float w = sqrt(r*r-h*h);
  vec2 q = vec2( length(p.xz), p.y );
  return ((h*q.x<w*q.y) ? length(q-vec2(w,h)) : abs(length(q)-r)) - t;
}

float sdLink(vec3 p, float le, float r1, float r2) {
  vec3 q = vec3( p.x, max(abs(p.y)-le,0.0), p.z );
  return length(vec2(length(q.xy)-r1,q.z)) - r2;
}

