float sdf(vec3 p);
float sphereSDF(vec3 p, float r);
float capsuleSDF(vec3 p, vec3 a, vec3 b, float r);
float cubeSDF(vec3 p, float l, float w, float h);
float torusSDF(vec3 p, vec2 t);

%%%

/** https://www.shadertoy.com/view/Ml3Gz8 */
float smin(float a, float b, float k) {
    float h = clamp(0.5 + 0.5*(a-b)/k, 0.0, 1.0);
    return mix(a, b, h) - k*h*(1.0-h);
}

float sdBox( vec3 p, vec3 b )
{
  vec3 q = abs(p) - b;
  return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);
}

float mengersponge_de(int i, vec3 p) { //by recursively digging a box
	float d = sdBox(p,vec3(1.0));
    float s = 1.0;

	for( int m=0; m<i; m++ ) {
    	vec3 a = mod( p*s, 2.0 )-1.0;
		s *= 3.0;
		vec3 r = abs(1.0 - 3.0*abs(a));

		float da = max(r.x,r.y);
		float db = max(r.y,r.z);
		float dc = max(r.z,r.x);
		float c = (min(da,min(db,dc))-1.0)/s;

		d = max(d,c);
	}

   return d;
}

float sdf(vec3 p) {
	// float t = torusSDF(p + vec3(0.0, 0.0, 1.0), vec2(2, 0.25)); // sphereSDF(p + vec3(0.0, 0.0, 1.0), 1);
	// float t = torusSDF(p + vec3(0.0, 0, 25), vec2(5-sin(time)*5, 1)); // cos(time), tan(time), sin(time));
	// t = smin(capsuleSDF(p + vec3(0.0, 0, 25), vec3(0.0, (sin(time)*5), 0.0), vec3(0.0, -(sin(time)*5), 0.0), 1), t, sin(time)*5);
	float t = mengersponge_de(6, p + vec3(0.0, 0.0, 5));

	return t;
}

float sphereSDF(vec3 p, float r) {
    return length(p) - r;
}

float capsuleSDF( vec3 p, vec3 a, vec3 b, float r ) {
  vec3 pa = p - a, ba = b - a;
  float h = clamp( dot(pa,ba)/dot(ba,ba), 0.0, 1.0 );

  return length( pa - ba*h ) - r;
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

