package org.april;

import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import glm_.mat4x4.Mat4;
import glm_.Java.glm;

public class Camera {
	private Vec3 position = new Vec3(0.0, 0.0, 3.0);
	private Vec3 front = new Vec3(0.0, 0.0, 0.0);
	private Vec3 up = new Vec3();
	private Mat4 look = new Mat4();
	private double zoom = 0;

	public Vec3 getPosition() {
		return position;
	}

	public Vec3 getFront() {
		look = glm.lookAt(position, position.plus(front), up);

		return front;
	}

	public Vec3 getUp() {
		return up;
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

