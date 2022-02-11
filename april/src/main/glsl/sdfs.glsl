// float sdCircle(vec2 p, vec2 o, float r);

float sdf(vec3 pos);
float sdMandel(vec3 pos, int i, int p, int b);
float sdSphere(vec3 p, float r);
float sdCutHollowSphere(vec3 p, float r, float h, float t);
float sdLink(vec3 p, float le, float r1, float r2);

%%%

float sdf(vec3 pos) {

	float t = sdSphere(pos - vec3(cos(time), 0.0, abs(sin(time)*10+5)), 1.0); // sdCutHollowSphere(pos - vec3(0.0, 0.0, abs(sin(time)*10+5)), 1.0, 1.0, 1.0);
	t = smoothMin(t, sdLink(pos - vec3(0.0, 0.0, 10.0), 1.0, 3.0, 1.0), 0);

    return t;
}

/* float sdSphere(vec3 p, float r) {
	return length(p) - r;
} */

// The following distance functions are not my code. Please see the
// note in the README.

// TODO: This is fucking broken.
float sdMandel(vec3 pos, int i, int p, int b) {
	vec3 z = pos;
	float dr = 1.0;
	float r = 0.0;
	for (int i = 0; i < i; i++) {
		r = length(z);
		if (r>b) break;
		
		// convert to polar coordinates
		float theta = acos(z.z/r);
		float phi = atan(z.y,z.x);
		dr =  pow( r, p-1.0)*p*dr + 1.0;
		
		// scale and rotate the point
		float zr = pow( r,p);
		theta = theta*p;
		phi = phi*p;
		
		// convert back to cartesian coordinates
		z = zr*vec3(sin(theta)*cos(phi), sin(phi)*sin(theta), cos(theta));
		z+=pos;
	}
	return 0.5*log(r)*r/dr;
}

float sdSphere(vec3 p, float s) {
	return length(p)-s;
}

float sdCutHollowSphere(vec3 p, float r, float h, float t) {
	float w = sqrt(r*r-h*h);
	vec2 q = vec2( length(p.xz), p.y );
	return ((h*q.x<w*q.y) ? length(q-vec2(w,h)) : abs(length(q)-r)) - t;
}

float sdLink(vec3 p, float le, float r1, float r2) {
	vec3 q = vec3( p.x, max(abs(p.y)-le,0.0), p.z );
	return length(vec2(length(q.xy)-r1,q.z)) - r2;
}

