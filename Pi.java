/* Run as : java Pi [numThreads] [Iterations]
 * Calculates an estimate of Pi using the Monte Carlo method
 * Used as an exercise to learn Multithreading and Atomic variables
 * Written by Daniel Mailloux	
 */

import java.lang.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class Pi {
	public static AtomicLong insideTotal;

	static class PiThread extends Thread {

		private Long iterations;

		public PiThread(Long i) {
			iterations = i;
		}

		public void run() {
			try {
				for (int i = 0; i < iterations; i++) {
					ThreadLocalRandom  rand = ThreadLocalRandom.current(); 
					//random x and y
					Double x = rand.nextDouble(1);
					Double y = rand.nextDouble(1);

					Double total = Math.pow(x, 2) + Math.pow(y, 2);
					if (total < 1) {
						insideTotal.incrementAndGet();
					}

				}
			} catch (Exception e) {
				System.out.println("Something went wrong " + e);
			}
		}
	}

	public static void main(String [] args) {
		Long numThreads = Long.parseLong(args[0]);
		Long iterations = Long.parseLong(args[1]);
		insideTotal = new AtomicLong();

		Long threadIterations = iterations / numThreads;

		ArrayList<PiThread> pThreads = new ArrayList<PiThread>();

		for (int i = 0; i < numThreads; i++) {
			pThreads.add(new PiThread(threadIterations));
			pThreads.get(i).start();
		}
		for (int i = 0; i < numThreads; i++) {
			try {
				pThreads.get(i).join();
			} catch (Exception e) {
				System.out.println("Problem joining thread: "+ i);
			}
		}
		System.out.println("Total = " + iterations);
		System.out.println("Inside = " + insideTotal.get());
		Double ratio = (double) insideTotal.get() / iterations;
		System.out.println("Ratio = " + ratio);
		Double pie = ratio * 4.0;
		System.out.println("Pi = " + pie);
	}
}

