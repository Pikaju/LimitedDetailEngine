package org.pikaju.limiteddetail.util;

public class MathHelper {

	private static float[] sinLookup = new float[65536];
	static {
		for (int i = 0; i < sinLookup.length; i++) sinLookup[i] = (float) Math.sin(Math.toRadians(i / (double) sinLookup.length * 360.0));
	}
	
	public static float sin(float angle) {
		while (angle < 0) angle += 360.0f;
		return sinLookup[(int) (angle / 360.0 * (double) sinLookup.length) % sinLookup.length];
	}
	
	public static float cos(float angle) {
		return sin(90 - angle);
	}
}
