package org.april;

import org.joml.Vector3f;
import org.joml.Vector2f;
import org.joml.Vector2d;
import org.joml.Matrix4f;

import java.lang.Math;

public class Camera {
	public enum CameraMovement {
		FORWARD,
		BACKWARD,
		LEFT,
		RIGHT
	};

	private float yaw = -90.0f;
	private float pitch =  0.0f;
	private double speed =  2.5f;
	private float sensitivity =  0.1f;
	private float zoom =  45.0f;

	private Vector3f position = new Vector3f(0.0f);
    private Vector3f front = new Vector3f(0.0f);
    private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    private Vector3f right = new Vector3f(0.0f);
    private Vector3f worldUp = new Vector3f(0.0f);

	public Camera() {
		updateVectors();
	}

	public Vector3f getPosition() {
		return position;
	}

	public void offsetPosition(float n, CameraMovement direction) {
		if (direction == CameraMovement.FORWARD) {
			position.add(front);
		} else if (direction == CameraMovement.BACKWARD) {
			position.sub(front);
		} else if (direction == CameraMovement.LEFT) {
            position.sub(right);
		} else if (direction == CameraMovement.RIGHT) {
            position.add(right);
		}
	}

	public Matrix4f getViewMatrix() {
		Vector3f tmp = new Vector3f();
		Matrix4f view = new Matrix4f();
		tmp = position.add(front, tmp);

		System.out.println(tmp + "\n" + tmp + "\n" + up + "\n\n");

		return view.lookAt(position, tmp, up);
	}

	public void addSpeed(double s) {
		speed += s;
	}

	public double getSpeed() {
		return speed;
	}

	public void addYaw(double y) {
		yaw += y;
	}

	public void updateVectors() {
        Vector3f _front = new Vector3f();
		Vector3f right_tmp = new Vector3f();
		Vector3f up_tmp = new Vector3f();

        _front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        _front.y = (float) (Math.sin(Math.toRadians(pitch)));
        _front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		_front.normalize(front);

		front.cross(worldUp, right_tmp);
		right = right_tmp.normalize();

		right.cross(front, up_tmp);
		// up = up_tmp.normalize();

        /* right = glm::normalize(glm::cross(front, worldUp));
		up = glm::normalize(glm::cross(Right, front)); */
	}
}

