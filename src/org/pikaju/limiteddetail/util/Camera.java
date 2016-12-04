package org.pikaju.limiteddetail.util;

import java.awt.event.KeyEvent;

import org.pikaju.limiteddetail.input.Input;

public class Camera {

	private float x = 0.0f;
	private float y = 0.0f;
	private float z = 0.0f;
	private float xr = 0.0f;
	private float yr = 0.0f;
	
	public void update(Input input, float delta) {
		float speed = delta * 0.01f;
		if (input.isKeyDown(KeyEvent.VK_CONTROL)) speed *= 0.2f;
		
		if (input.isKeyDown(KeyEvent.VK_W)) {
			x += Math.sin(Math.toRadians(yr)) * speed;
			z += Math.cos(Math.toRadians(yr)) * speed;
		}
		if (input.isKeyDown(KeyEvent.VK_S)) {
			x += Math.sin(Math.toRadians(yr + 180)) * speed;
			z += Math.cos(Math.toRadians(yr + 180)) * speed;
		}
		if (input.isKeyDown(KeyEvent.VK_A)) {
			x += Math.sin(Math.toRadians(yr - 90)) * speed;
			z += Math.cos(Math.toRadians(yr - 90)) * speed;
		}
		if (input.isKeyDown(KeyEvent.VK_D)) {
			x += Math.sin(Math.toRadians(yr + 90)) * speed;
			z += Math.cos(Math.toRadians(yr + 90)) * speed;
		}
		if (input.isKeyDown(KeyEvent.VK_SPACE)) {
			y += speed;
		}
		if (input.isKeyDown(KeyEvent.VK_SHIFT)) {
			y -= speed;
		}
		
		if (input.isKeyDown(KeyEvent.VK_UP)) {
			xr -= 2.0f * delta;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			xr += 2.0f * delta;
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			yr -= 2.0f * delta;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			yr += 2.0f * delta;
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getXR() {
		return xr;
	}

	public void setXR(float xr) {
		this.xr = xr;
	}

	public float getYR() {
		return yr;
	}

	public void setYR(float yr) {
		this.yr = yr;
	}
}
