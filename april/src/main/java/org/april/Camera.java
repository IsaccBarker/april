package org.april;

public class Camera {
	Position position = new Position();
	Point lookAt = new Point();
	double zoom = 0;

	public Position getPosition() {
		return position;
	}

	public Point getLookAt() {
		return lookAt;
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

