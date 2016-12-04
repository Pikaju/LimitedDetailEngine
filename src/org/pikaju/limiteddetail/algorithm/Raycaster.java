package org.pikaju.limiteddetail.algorithm;

import org.pikaju.limiteddetail.graphics.Bitmap;
import org.pikaju.limiteddetail.util.MathHelper;

public class Raycaster {

	public static int castRay(Octree octree, float x, float y, float z, float dx, float dy, float dz) {
		float speed = 0.001f;
		int maxDist = (int) (0.5f / speed);
		int traveledDist = 0;
		Octree o = null;
		OctreeDescription desc = new OctreeDescription(0, 0, 0, 0);
		while (traveledDist < maxDist) {
			traveledDist++;
			
			if (o != null) {
				if (x >= desc.getX() && y >= desc.getY() && z >= desc.getZ() && x < desc.getX() + desc.getDetail() && y < desc.getY() + desc.getDetail() && z < desc.getZ() + desc.getDetail()) {
					float xdist = 0;
					if (dx >= 0) xdist = desc.getX() + desc.getDetail() - x;
					else xdist = desc.getX() - x;
					
					float ydist = 0;
					if (dy >= 0) ydist = desc.getY() + desc.getDetail() - y;
					else ydist = desc.getY() - y;
					
					float zdist = 0;
					if (dz >= 0) zdist = desc.getZ() + desc.getDetail() - z;
					else zdist = desc.getZ() - z;
					
					float xre = xdist / Math.abs(dx);
					float yre = ydist / Math.abs(dy);
					float zre = zdist / Math.abs(dz);
					if (xre < yre && xre < zre) {
						x += xdist;
						y += dx / xdist * dy;
						z += dx / xdist * dz;
					}
					else if (yre < xre && yre < zre) {
						x += dy / ydist * dx;
						y += ydist;
						z += dy / ydist * dz;
					} else if (zre < xre && zre < yre) {
						x += dz / zdist * dx;
						y += dz / zdist * dy;
						z += zdist;
					}
					continue;
				}
				o = null;
			} else {
				x += dx * speed;
				y += dy * speed;
				z += dz * speed;
			}
			
			o = octree.getOctreeAt(desc, x, y, z);
			if (o == null) continue;
			
			Atom atom = null;
			if (o.getNumAtoms() > 0) atom = o.getAtom(0);
			if (atom != null) {
				//float dist = 255.0f / ((traveledDist * 0.01f) + 1);
				//int c = ((int) dist << 16);
				//return c;
				return atom.getRGB();
			}
		}
		return 0x000000;
	}
	
	public static int castRayDist(Octree octree, float x, float y, float z, float dx, float dy, float dz) {
		float speed = 0.001f;
		int maxDist = (int) (0.5f / speed);
		int traveledDist = 0;
		Octree o = null;
		OctreeDescription desc = new OctreeDescription(0, 0, 0, 0);
		while (traveledDist < maxDist) {
			traveledDist++;
			
			x += dx * speed;
			y += dy * speed;
			z += dz * speed;

			if (o != null) {
				if (x >= desc.getX() && y >= desc.getY() && z >= desc.getZ() && x < desc.getX() + desc.getDetail() && y < desc.getY() + desc.getDetail() && z < desc.getZ() + desc.getDetail()) {
					continue;
				}
				o = null;
			}
			
			o = octree.getOctreeAt(desc, x, y, z);
			if (o == null) continue;
			
			Atom atom = null;
			if (o.getNumAtoms() > 0) atom = o.getAtom(0);
			if (atom != null) {
				return atom.getRGB();
			}
		}
		return 0x000000;
	}
	
	public static void castScreen(Octree octree, Bitmap frameBuffer, int xo, int yo, int width, int height, float fov, float xpos, float ypos, float zpos, float xrot, float yrot) {
		float aspect = (float) width / (float) height;
		for (int x = xo; x < xo + width; x++) {
			for (int y = yo; y < yo + height; y++) {
				float yscale = MathHelper.cos(xrot);
				float dy = MathHelper.sin(-(((y - yo) / (float) height - 0.5f) * (fov / aspect)) + xrot);
				float dx = MathHelper.sin(((x - xo) / (float) width - 0.5f) * fov + yrot) * yscale;
				float dz = MathHelper.cos(((x - xo) / (float) width - 0.5f) * fov + yrot) * yscale;
				frameBuffer.pixels[x + y * frameBuffer.width] = castRayDist(octree, xpos, ypos, zpos, dx, dy, dz);
			}
		}
	}
}