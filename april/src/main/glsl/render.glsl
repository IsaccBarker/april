float castRay(vec3 eye, vec3 marchingDirection);
bool rayCollided(float r);
vec3 rayDirection(float fieldOfView, vec2 size, vec2 fragCoord);
vec3 estimateNormal(vec3 p);
mat4 viewMatrix(vec3 eye, vec3 center, vec3 up);
mat3 rotateX(float theta);
mat3 rotateY(float theta);
mat3 rotateZ(float theta);
mat3 identity();

const int MAX_MARCHING_STEPS = 256;
const float MIN_DIST = 0.00;
const float MAX_DIST = 100.0;
const float EPSILON = 0.001;

uniform mat4 view;
uniform vec3 cameraPos;

%%%

float castRay(vec3 eye, vec3 marchingDirection) {
    float depth = MIN_DIST;
    
	for (int i = 0; i < MAX_MARCHING_STEPS; i++) {
        float dist = sdf(eye + depth * marchingDirection);
        
		if (dist < EPSILON) {
			return depth;
        }
        
		depth += dist;
        
		if (depth >= MAX_DIST) {
            return MAX_DIST;
        }
    }

    return MAX_DIST;
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
        sdf(vec3(p.x + EPSILON, p.y, p.z)) - sdf(vec3(p.x - EPSILON, p.y, p.z)),
        sdf(vec3(p.x, p.y + EPSILON, p.z)) - sdf(vec3(p.x, p.y - EPSILON, p.z)),
        sdf(vec3(p.x, p.y, p.z  + EPSILON)) - sdf(vec3(p.x, p.y, p.z - EPSILON))
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

