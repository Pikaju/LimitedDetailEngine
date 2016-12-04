package org.pikaju.limiteddetail.algorithm;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Atom {

	private float x;
	private float y;
	private float z;
	
	private char r;
	private char g;
	private char b;
	
	public Atom(float x, float y, float z, char r, char g, char b) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Atom() {
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

	public char getR() {
		return r;
	}

	public void setR(char r) {
		this.r = r;
	}

	public char getG() {
		return g;
	}

	public void setG(char g) {
		this.g = g;
	}

	public char getB() {
		return b;
	}

	public void setB(char b) {
		this.b = b;
	}

	public int getRGB() {
		return (r << 16) | (g << 8) | b;
	}

	public void save(DataOutput out) {
		try {
			out.writeFloat(x);
			out.writeFloat(y);
			out.writeFloat(z);
			out.writeChar(r);
			out.writeChar(g);
			out.writeChar(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(DataInput in) {
		try {
			x = in.readFloat();
			y = in.readFloat();
			z = in.readFloat();
			r = in.readChar();
			g = in.readChar();
			b = in.readChar();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
