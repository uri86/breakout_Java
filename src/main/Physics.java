package main;

public class Physics {
	public static double getVector(double angle, double yVector) {
		return yVector/Math.sin(angle);
	}
	public static double getXVector(double vector, double radianAngle) {
		return vector*Math.cos(radianAngle);
	}
	public static double getYVector(double vector, double radianAngle) {
		return vector*Math.sin(radianAngle);
	}
}
