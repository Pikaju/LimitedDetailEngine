package org.pikaju.limiteddetail.util;

import java.util.Random;

public class Noise {

	public static float noise(double x, double y, double z) {
		return (float) preciseNoise(x, y, z);
	}

	public static double preciseNoise(double x, double y, double z) {
		int X = (int) Math.floor(x) & 255, Y = (int) Math.floor(y) & 255, Z = (int) Math.floor(z) & 255;
		x -= Math.floor(x);
		y -= Math.floor(y);
		z -= Math.floor(z);
		double u = fade(x), v = fade(y), w = fade(z);
		int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;

		return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), grad(p[BA], x - 1, y, z)), lerp(u, grad(p[AB], x, y - 1, z), grad(p[BB], x - 1, y - 1, z))), lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), grad(p[BA + 1], x - 1, y, z - 1)), lerp(u, grad(p[AB + 1], x, y - 1, z - 1), grad(p[BB + 1], x - 1, y - 1, z - 1))));
	}
	
	public static float octaves(int octaves, double x, double y, double z) {
		return (float) preciseOctaves(octaves, x, y, z);
	}
	
	public static double preciseOctaves(int octaves, double x, double y, double z) {
		double lacunarity = 1.9;
		double gain = 0.65;
		double sum = 0.0;
		double amplitude = 1.0;
	 
		for (int i = 0; i < octaves; i++) {
			sum += amplitude * preciseNoise(x, y, z);
	 
			amplitude *= gain;
			
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
		}
		return sum;
	}

	private static double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	private static double grad(int hash, double x, double y, double z) {
		int h = hash & 15;
		double u = h < 8 ? x : y, v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	public static void setSeed(long seed) {
		Random random = new Random(seed);
		for(int i = 0; i < 256; i++) {
			int num = random.nextInt(255);
			for(int j = 0; j < i; j++) {
				if(permutation[j] == num) {
					i--;
				}
			}
			permutation[i] = num;
		}

		for(int i = 0; i < 256; i++)
			p[256 + i] = p[i] = permutation[i];
	}

	private static int[] p = new int[512];
	private static int[] permutation = new int[256];
	static {
		setSeed(System.currentTimeMillis());
	}
}
