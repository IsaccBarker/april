package org.april;

import glm_.vec3.Vec3

public class Camera {
	Vec3 position = new Vec3(0.0, 0.0, 3.0);
	Vec3 target = new Vec3(0.0, 0.0, 0.0);
	Vec3 direction = new Vec3();
	double zoom = 0;

	public Vec3 getPosition() {
		return position;
	}

	public Vec3 getTarget() {
		return target;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public double getZoom() {
		return zoom;
	}

	public void addZoom(double zoom) {
		this.zoom += zoom;

		if (this.zoom < 1) {
			this.zoom = 1;
		}
	}
}

