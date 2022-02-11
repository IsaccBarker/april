package org.april;

public class Position {
	private double x = 0;
	private double y = 0;
	private double z = 0;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void addX(int x) {
		this.x += x;
	}

	public void addY(int y) {
		this.y += y;
	}

	public void addZ(int z) {
		this.z += z;
	}
}

