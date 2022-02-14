Ray castRay(vec3 eye, vec3 marchingDirection);
bool rayCollided(float r);
vec3 rayDirection(float fieldOfView, vec2 size, vec2 fragCoord);
vec3 estimateNormal(vec3 p);
mat4 viewMatrix(vec3 eye, vec3 center, vec3 up);
mat3 rotateX(float theta);
mat3 rotateY(float theta);
mat3 rotateZ(float theta);
mat3 identity();
vec3 phongContribForLight(vec3 k_d, vec3 k_s, float alpha, vec3 p, vec3 eye, vec3 lightPos, vec3 lightIntensity);
vec3 phongIllumination(vec3 k_a, vec3 k_d, vec3 k_s, float alpha, vec3 p, vec3 eye);

const int MAX_MARCHING_STEPS = 256;
const float MIN_DIST = 0.01;
const float MAX_DIST = 100.0;
const float EPSILON = 0.001;

uniform mat4 rotationMatrix;
uniform vec3 cameraPos;
uniform float fov;

%%%

Ray castRay(vec3 eye, vec3 marchingDirection) {
    float depth = MIN_DIST;
    
	for (int i = 0; i < MAX_MARCHING_STEPS; i++) {
        DistanceMeta meta = sdf(eye + depth * marchingDirection);
        float dist = meta.dist;

		if (dist < EPSILON) {
			return Ray(i, meta);
        }
        
		depth += dist;
        
		if (depth >= MAX_DIST) {
            return Ray(MAX_MARCHING_STEPS, meta);
        }
    }

    // return Ray(MAX_MARCHING_STEPS, DistanceMeta(MAX_DIST, vec3(0)));
	return Ray(MAX_MARCHING_STEPS, DistanceMeta(MAX_DIST, vec3(0)));
}

bool rayCollided(float r) {
	return r > MAX_DIST - EPSILON;
}

vec3 rayDirection(float fieldOfView, vec2 size, vec2 fragCoord) {
    vec2 xy = fragCoord - size / 2.0;
    float z = size.y / tan(radians(fieldOfView) / 2.0);
    return normalize(vec3(xy, -z));
}

vec3 estimateNormal(vec3 p) {
    return normalize(vec3(
        sdf(vec3(p.x + EPSILON, p.y, p.z)).dist - sdf(vec3(p.x - EPSILON, p.y, p.z)).dist,
        sdf(vec3(p.x, p.y + EPSILON, p.z)).dist - sdf(vec3(p.x, p.y - EPSILON, p.z)).dist,
        sdf(vec3(p.x, p.y, p.z  + EPSILON)).dist - sdf(vec3(p.x, p.y, p.z - EPSILON)).dist
    ));
}

// Rotation matrix around the X axis.
mat3 rotateX(float theta) {
    float c = cos(theta);
    float s = sin(theta);
    return mat3(
        vec3(1, 0, 0),
        vec3(0, c, -s),
        vec3(0, s, c)
    );
}

// Rotation matrix around the Y axis.
mat3 rotateY(float theta) {
    float c = cos(theta);
    float s = sin(theta);
    return mat3(
        vec3(c, 0, s),
        vec3(0, 1, 0),
        vec3(-s, 0, c)
    );
}

// Rotation matrix around the Z axis.
mat3 rotateZ(float theta) {
    float c = cos(theta);
    float s = sin(theta);
    return mat3(
        vec3(c, -s, 0),
        vec3(s, c, 0),
        vec3(0, 0, 1)
    );
}

// Identity matrix.
mat3 identity() {
    return mat3(
        vec3(1, 0, 0),
        vec3(0, 1, 0),
        vec3(0, 0, 1)
    );
}

vec3 phongContribForLight(vec3 k_d, vec3 k_s, float alpha, vec3 p, vec3 eye,
                          vec3 lightPos, vec3 lightIntensity) {
    vec3 N = estimateNormal(p);
    vec3 L = normalize(lightPos - p);
    vec3 V = normalize(eye - p);
    vec3 R = normalize(reflect(-L, N));
    
    float dotLN = dot(L, N);
    float dotRV = dot(R, V);
    
    if (dotLN < 0.0) {
        // Light not visible from this point on the surface
        return vec3(0.0, 0.0, 0.0);
    } 
    
    if (dotRV < 0.0) {
        // Light reflection in opposite direction as viewer, apply only diffuse
        // component
        return lightIntensity * (k_d * dotLN);
    }
    return lightIntensity * (k_d * dotLN + k_s * pow(dotRV, alpha));
}

vec3 phongIllumination(vec3 k_a, vec3 k_d, vec3 k_s, float alpha, vec3 p, vec3 eye) {
    const vec3 ambientLight = 0.5 * vec3(1.0, 1.0, 1.0);
    vec3 color = ambientLight * k_a;
    
    vec3 light1Pos = vec3(0,
                          0,
                          0);
    vec3 light1Intensity = vec3(0.4, 0.4, 0.4);
    
    color += phongContribForLight(k_d, k_s, alpha, p, eye,
                                  light1Pos,
                                  light1Intensity);
    
    return color;
}

