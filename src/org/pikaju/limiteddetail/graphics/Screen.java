package org.pikaju.limiteddetail.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen extends Bitmap {

	private BufferedImage frameBuffer;
	
	public Screen(int width, int height) {
		super(width, height);
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) frameBuffer.getRaster().getDataBuffer()).getData();
	}

	public void render(Graphics2D g, int width, int height) {
		g.drawImage(frameBuffer, 0, 0, width, height, null);
		clear();
	}
}
