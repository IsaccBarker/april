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

	public static final double SPEED = 0.1f;
	public static final float FOV = 90.0f;
	public static final float ZOOM = 45.0f;

	private double yaw = 0.0f;
	private double pitch =  0.0f;
	private double speed =  SPEED;
	private float sensitivity =  0.1f;
	private float zoom =  ZOOM;
	private float fov = FOV;

	private Vector2f prevMouse = new Vector2f();
	private Vector3f position = new Vector3f(0.0f);
    private Vector3f front = new Vector3f(0.0f);
    private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    private Vector3f right = new Vector3f(0.0f);
    private Vector3f worldUp = new Vector3f(0.0f);

	public Camera() {
		updateVectors();
	}

	public void offsetPosition(CameraMovement direction) {
		Vector3f dump = new Vector3f();
		Vector3f speed = new Vector3f((float) this.speed, (float) this.speed, (float) this.speed);
		
		if (direction == CameraMovement.FORWARD) {
			System.out.println(front + " * " + speed);
			// position.add(front);
		}
	}

	public Vector3f getFront() {
		return front;
	}

	public Vector2f getPrevMouse() {
		return prevMouse;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Matrix4f getViewMatrix() {
		Matrix4f view = new Matrix4f();

		view.rotateY((float) Math.toRadians(yaw));
		view.rotateX((float) Math.toRadians(pitch));

		return view;
	}

	public void addSpeed(double s) {
		speed += s;

		if (speed < 0.005f) {
			speed = 0.005f;
		}

		if (speed > 15) {
			speed = 15;
		}
	}

	public void setSpeed(double s) {
		speed = s;
	}

	public double getSpeed() {
		return speed;
	}

	public void addYaw(double y) {
		yaw += y;
	}

	public void addPitch(double p) {
		pitch += p;
	}

	public void addFOV(double f) {
		fov += f;
	}

	public void setFOV(float f) {
		fov = f;
	}

	public float getFOV() {
		return fov;
	}

	public void reset() {
		fov = FOV;
		speed = SPEED;
		zoom = ZOOM;

		position = new Vector3f(0.0f);
		yaw = 0;
		pitch = 0;
	}

	public void updateVectors() {
        /* Vector3f _front = new Vector3f();
		Vector3f right_tmp = new Vector3f();
		Vector3f up_tmp = new Vector3f();

        _front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        _front.y = (float) (Math.sin(Math.toRadians(pitch)));
        _front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		_front.normalize(front); */

		/* front.cross(worldUp, right_tmp);
		right = right_tmp.normalize();

		right.cross(front, up_tmp);
		up = up_tmp.normalize(); */

        /* right = glm::normalize(glm::cross(front, worldUp));
		up = glm::normalize(glm::cross(Right, front)); */

		Vector3f front = new Vector3f();
		front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		front.y = (float) (Math.cos(Math.toRadians(pitch)));
		front.z = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));

		front.normalize();

		this.front = front;

		System.out.println(front);
	}
}

