package org.april;

import org.joml.Vector3f;
import org.joml.Vector2f;
import org.joml.Vector2d;
import org.joml.Matrix4f;

public class Camera {
	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
	private Vector3f center = new Vector3f();
	private Matrix4f viewMatrix = new Matrix4f();
	private final Vector2d previousPos = new Vector2d();
    private final Vector2d currentPos = new Vector2d();
    private final Vector2f displVec = new Vector2f();
	private double speed = 1;

	public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
		offsetX *= speed;
		offsetY *= speed;
		offsetZ *= speed;

        if (offsetZ != 0 ) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }

        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }

		position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

	public void updateViewMatrix() {
		/* viewMatrix.identity();

		// First do the rotation so camera rotates over its position
		viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
			.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));

		// Then do the translation
		viewMatrix.translate(-position.x, -position.y, -position.z); */

		viewMatrix.identity();
		viewMatrix.lookAt(position, rotation, up);
	}

	public Vector2d getPrevVec() {
        return previousPos;
    }

	public Vector2d getCurrentVec() {
        return currentPos;
    }

	public Vector2f getDisplVec() {
        return displVec;
    }

	public double getSpeed() {
		return speed;
	}

	public void addSpeed(double speed) {
		this.speed += speed;

		if (this.speed < 0.001) {
			this.speed = 0.001;
		}
	}
}

