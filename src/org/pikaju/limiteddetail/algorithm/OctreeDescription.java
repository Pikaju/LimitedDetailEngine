package org.pikaju.limiteddetail.algorithm;

public class OctreeDescription {

	private float x;
	private float y;
	private float z;
	private float detail;
	
	public OctreeDescription(float x, float y, float z, float detail) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.detail = detail;
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
	public float getDetail() {
		return detail;
	}
	public void setDetail(float detail) {
		this.detail = detail;
	}
}
