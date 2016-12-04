package org.pikaju.limiteddetail;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import org.pikaju.limiteddetail.algorithm.Atom;
import org.pikaju.limiteddetail.algorithm.Octree;
import org.pikaju.limiteddetail.algorithm.Raycaster;
import org.pikaju.limiteddetail.graphics.Screen;
import org.pikaju.limiteddetail.input.Input;
import org.pikaju.limiteddetail.util.Camera;
import org.pikaju.limiteddetail.util.Noise;

public class LimitedDetail extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static LimitedDetail i;
	
	private int width;
	private int height;
	private int scale;
	
	private JFrame frame;
	
	private Thread thread;
	private boolean running;
	
	private Screen frameBuffer;
	private Input input;
	
	private Octree octree;
	
	private Camera camera;

	public LimitedDetail(int width, int height, int scale) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		Dimension dim = new Dimension(this.width * this.scale, this.height * this.scale);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
	}
	
	public static void main(String[] args) {
		i = new LimitedDetail(360, 360 / 16 * 9, 2);
		i.start();
	}
	
	public void init() {
		frame = new JFrame("Limited Detail Engine");
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		input = new Input();
		addMouseListener(input);
		addMouseMotionListener(input);
		addKeyListener(input);
		
		frameBuffer = new Screen(width, height);
		
		frame.setVisible(true);
	
		octree = new Octree();
		final float scale = 3.0f;
		final float off = 0.003f;
		long ct = System.currentTimeMillis();
		for (float x = 0.0f; x < 1.0f; x += off) {
			for (float y = 0.0f; y < 1.0f; y += off) {
				for (float z = 0.0f; z < 1.0f; z += off) {
					if (Noise.noise(x * scale, y * scale, z * scale) > 1.0 - y) {
						octree.addAtom(new Atom(x, 1.0f - y, z, (char) (Math.random() * 255), (char) (Math.random() * 255), (char) (Math.random() * 255)));
					}
				}
			}
		}
		System.out.println("Generation time: " + (System.currentTimeMillis() - ct) + "ms");
		System.out.println("Total atoms: " + octree.getNumAtoms());
		ct = System.currentTimeMillis();
		octree.splitAtomsUntil(1);
		System.out.println("Sorting time: " + (System.currentTimeMillis() - ct) + "ms");
		//octree.save("octree.pika");
		//octree.load("octree.pika");
		
		camera = new Camera();
		camera.setY(0.0f);
		camera.setZ(-0.25f);
	}

	public void start() {
		init();
		running = true;
		thread = new Thread(this, "Limited Detail");
		thread.start();
	}
	
	public void stop() {
		running = false;
		System.exit(0);
	}

	public void run() {
		long currentTime = 0;
		long lastTime = System.nanoTime();
		
		float delta = 0;
		float ns = 1000000000.0f / 60.0f;
		while (running) {
			currentTime = System.nanoTime();
			delta = (currentTime - lastTime) / ns;
			lastTime = currentTime;
			
			update(delta);
			render();
		}
	}
	
	
	public void update(float delta) {
		camera.update(input, delta);
		input.refresh();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		
		Raycaster.castScreen(octree, frameBuffer, 0, 0, width, height, 90.0f, camera.getX(), camera.getY(), camera.getZ(), camera.getXR(), camera.getYR());
		frameBuffer.render(g, getWidth(), getHeight());
		
		g.dispose();
		bs.show();
	}
}
