package org.april;

public class Position {
	private double x = 0;
	private double y = 0;
	private double z = -5;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void addX(double x) {
		this.x += x;
	}

	public void addY(double y) {
		this.y += y;
	}

	public void addZ(double z) {
		this.z += z;
	}
}

