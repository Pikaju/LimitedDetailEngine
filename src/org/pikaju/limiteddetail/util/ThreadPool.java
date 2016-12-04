package org.pikaju.limiteddetail.util;

import java.util.Random;

public class ThreadPool {

	public static boolean running;
	
	public static void startThreads(int numThreads) {
		running = true;	
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					
				}
			}
		}).start();
		Random random = new Random();
		random.nextInt(54);
	}
	
	public static void stopThreads() {
		running = false;
	}
}
