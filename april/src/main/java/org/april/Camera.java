package org.april;

public class Camera {
	Position position = new Position();
	Point lookAt = new Point();

	public Position getPosition() {
		return position;
	}

	public Point getLookAt() {
		return lookAt;
	}
}

