package practice9;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * TestDeque <br>
 * Covering ArrayDeque
 */

public class TestDeque {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String e = "Ee";
		String[] words = { "Aa", "Bb", "Cc" };
		System.out.println("words - length=" + words.length + " -> \"Aa\", \"Bb\", \"Cc\"");

		// List n° 1
		Deque<String> dq1 = new ArrayDeque<>();
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);

		dq1.add("Dd");
		dq1.add("Ee");
		dq1.add("Ff");
		dq1.add("Ff"); // duplicated are silently discarded
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		
		// A) FIFO operations
		String first = dq1.getFirst();
		System.out.println("\nfirst element = " + first);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		dq1.offerFirst(e); // if capacity-restricted vs. dq1.addFirst(e);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		String pf = dq1.peekFirst();
		System.out.println("peekFirst=" + pf);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		pf = dq1.pollFirst();
		System.out.println("pollFirst=" + pf);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		
		// B) LIFO operations
		String last = dq1.getLast();
		System.out.println("\nlast element = " + last);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		dq1.offerLast(e); // if capacity-restricted vs. dq1.addLast(e);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		String pl = dq1.peekLast();
		System.out.println("peekLast=" + pl);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
		pl = dq1.pollLast();
		System.out.println("pollLast=" + pf);
		System.out.println("dq1 - size=" + dq1.size() + " -> " + dq1);
				
	}
}
