package org.pikaju.limiteddetail.graphics;

public class Bitmap {

	public int[] pixels;
	public int width;
	public int height;
	
	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[this.width * this.height];
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) pixels[i] = 0x00000000;
	}

	public void rect(int x, int y, int width, int height, int rgb) {
		for (int xx = 0; xx < width; xx++) {
			if (xx + x < 0 || xx + x >= width) continue;
			for (int yy = 0; yy < height; yy++) {
				if (yy + y < 0 || yy + y >= height) continue;
				pixels[(xx + x) + (yy + y) * this.width] = rgb;
			}
		}
	}
}
