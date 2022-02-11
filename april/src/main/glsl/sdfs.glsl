// float sdCircle(vec2 p, vec2 o, float r);

float sdf(vec3 pos);
float sdSphere(vec3 p, float r);

%%%

/* 
/ Signed distance function for a sphere. /
float sdCircle(vec2 p, vec2 o, float r) {
	return length(p - o) - r;
} */

float sdf(vec3 pos) {
    float t = sdSphere(pos-vec3(0.0, 0.0, 10.0), 3.0);

    return t;
}

float sdSphere(vec3 p, float r) {
	return length(p) - r;
}

