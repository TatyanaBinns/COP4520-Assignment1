import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tanya
 */
public class Driver {
	private static final int MAX_PRIME_SIZE = 100_000_000;
	private static final int THREAD_COUNT = 8;

	private static void findMultiples(BitSet arr, int startPoint, int toSet) {
		for (int p = 2; p < MAX_PRIME_SIZE / 2; p++) {
			int start = startPoint - (startPoint % p);
			for (int i = Math.max(2 * p, start); i < startPoint + toSet; i += p)
				arr.set(i);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		AtomicInteger sharedCounter = new AtomicInteger(2);

		Thread[] threads = new Thread[THREAD_COUNT];
		BitSet[] arr = new BitSet[threads.length];

		for (int i = 0; i < threads.length; i++) {
			BitSet b = new BitSet(MAX_PRIME_SIZE);
			arr[i] = b;
			int areaEach = MAX_PRIME_SIZE / THREAD_COUNT;
			int startPoint = i * areaEach;
			threads[i] = new Thread(() -> findMultiples(b, startPoint, areaEach + 1));
		}

		Instant start = Instant.now();

		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();

		BitSet res = new BitSet(MAX_PRIME_SIZE);
		res.set(0, MAX_PRIME_SIZE);
		for (int i = 0; i < arr.length; i++)
			res.andNot(arr[i]);

		Duration exectime = Duration.between(start, Instant.now());

		String output = reportFoundPrimes(MAX_PRIME_SIZE, res, exectime);
		try {
			Files.write(Paths.get(".", "primes.txt"), output.getBytes());
		} catch (IOException e) {
			System.err.println("Error writing data out to file.");
			System.err.println("Output would have been:");
			System.err.println(output);
			e.printStackTrace();
		}
	}

	private static String reportFoundPrimes(int n, BitSet arr, Duration exectime) {
		StringBuilder res = new StringBuilder();
		int count = 0;
		long sum = 0;

		for (int i = 2; i < n && i > 0; i = arr.nextSetBit(i + 1)) {
			count++;
			sum += i;
		}

		int[] top = new int[10];

		for (int i = top.length - 1, prime = arr.previousSetBit(arr.length()); i >= 0; i--, prime = arr
				.previousSetBit(prime - 1))
			top[i] = prime;

		res.append(exectime.getSeconds() + "." + String.format("%03ds", exectime.getNano() / 1_000_000) + " " + count
				+ " " + sum + "\n");

		for (int i : top)
			res.append(i).append('\n');
		return res.toString();
	}

}
