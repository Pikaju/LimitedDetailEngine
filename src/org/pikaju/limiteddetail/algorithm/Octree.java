package org.pikaju.limiteddetail.algorithm;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.pikaju.limiteddetail.graphics.Bitmap;
import org.pikaju.limiteddetail.util.Camera;

public class Octree {

	private Octree[] childs;
	
	private ArrayList<Atom> atoms;
	
	public Octree() {
		atoms = new ArrayList<Atom>();
	}
	
	public void createChilds() {
		childs = new Octree[8];
		for (int i = 0; i < 8; i++) childs[i] = new Octree();
	}
	
	public void splitAtoms(float cx, float cy, float cz, float currentDepth) {
		if (childs == null) createChilds();
		for (int i = 0; i < atoms.size(); i++) {
			Atom a = atoms.get(i);
			int x = a.getX() < cx + currentDepth ? 0 : 1;
			int y = a.getY() < cy + currentDepth ? 0 : 1;
			int z = a.getZ() < cz + currentDepth ? 0 : 1;
			childs[x + y * 2 + z * 4].addAtom(a);
		}
		atoms.clear();
	}
	
	public void splitAtomsUntil(float cx, float cy, float cz, float currentDepth, int maxAtoms) {
		splitAtoms(cx, cy, cz, currentDepth);
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				for (int z = 0; z < 2; z++) {
					if (childs[x + y * 2 + z * 4].getNumAtoms() > maxAtoms) childs[x + y * 2 + z * 4].splitAtomsUntil(cx + x * currentDepth, cy + y * currentDepth, cz + z * currentDepth, currentDepth * 0.5f, maxAtoms);			
				}
			}
		}
	}

	public void render(Bitmap frameBuffer, int width, int height, Camera camera) {
		render(frameBuffer, width, height, 0, 0, 0, 0.5f, camera);
	}
	
	private void render(Bitmap frameBuffer, int width, int height, float cx, float cy, float cz, float currentDepth, Camera camera) {
		if (childs != null) {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					for (int z = 0; z < 2; z++) {
						childs[x + y * 2 + z * 4].render(frameBuffer, width, height, cx + x * currentDepth, cy + y * currentDepth, cz + z * currentDepth, currentDepth * 0.5f, camera);			
					}
				}
			}
		} else {
			if (atoms.size() > 0) {
				float dx = cx - camera.getX();
				float dy = cy - camera.getY();
				float dz = cz - camera.getZ();
				float angle = (float) Math.toDegrees(Math.atan2(dz, dx));
				angle *= width / 90.0f;
				frameBuffer.rect((int) angle, (int) dy, 1, 1, atoms.get(0).getRGB());
			}
		}
	}
	
	public void splitAtomsUntil(int maxAtoms) {
		splitAtomsUntil(0.0f, 0.0f, 0.0f, 0.5f, maxAtoms);
	}
	
	public void addAtom(Atom atom) {
		atoms.add(atom);
	}
	
	public int getNumAtoms() {
		return atoms.size();
	}

	public Octree getOctreeAt(OctreeDescription desc, float x, float y, float z) {
		return getOctreeAt(desc, x, y, z, 0, 0, 0, 0.5f);
	}
	
	private Octree getOctreeAt(OctreeDescription desc, float x, float y, float z, float cx, float cy, float cz, float currentDepth) {
		if (childs != null) {
			if (x < cx) return null;
			if (y < cy) return null;
			if (z < cz) return null;
			if (x > cx + currentDepth * 2.0f) return null;
			if (y > cy + currentDepth * 2.0f) return null;
			if (z > cz + currentDepth * 2.0f) return null;
			
			int xx = x < cx + currentDepth ? 0 : 1;
			int yy = y < cy + currentDepth ? 0 : 1;
			int zz = z < cz + currentDepth ? 0 : 1;
			return childs[xx + yy * 2 + zz * 4].getOctreeAt(desc, x, y, z, cx + xx * currentDepth, cy + yy * currentDepth, cz + zz * currentDepth, currentDepth * 0.5f);			
		}
		desc.setX(cx);
		desc.setY(cy);
		desc.setZ(cz);
		desc.setDetail(currentDepth * 2.0f);
		return this;
	}

	public Atom getAtom(int i) {
		return atoms.get(i);
	}
	
	public void save(String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			save(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load(String filePath) {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(new File(filePath)));
			load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(DataOutput out) {
		try {
			out.writeByte(0); // ATOM
			out.writeInt(atoms.size());
			for (int i = 0; i < atoms.size(); i++) {
				atoms.get(i).save(out);
			}
			out.writeByte(1); // CHILDS
			if (childs != null) {
				for (int i = 0; i < 8; i++) childs[i].save(out);
			}
			
			out.writeByte(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(DataInput in) {
		childs = null;
		atoms.clear();
		try {
			byte b = 0;
			while((b = in.readByte()) != -1) {
				if (b == 0) {
					int amount = in.readInt();
					for (int i = 0; i < amount; i++) {
						Atom atom = new Atom();
						atom.load(in);
						atoms.add(atom);
					}
				}
				else if (b == 1) {
					createChilds();
					for (int i = 0; i < 8; i++) childs[i].load(in);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
