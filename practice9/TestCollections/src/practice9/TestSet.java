package practice9;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * TestSet <br>
 * Covering HashSet
 */

public class TestSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] words = { "Aa", "Bb", "Cc" };
		System.out.println("words - length=" + words.length +" -> \"Aa\", \"Bb\", \"Cc\"");


		// List n° 1
		Set<String> set1 = new HashSet<>();
		System.out.println("set1 - size=" + set1.size() +" -> "+ set1);

		set1.add("Dd");
		set1.add("Ee");
		set1.add("Ff");
		set1.add("Ff"); // duplicated are silently discarded 
		System.out.println("\nset1 - size=" + set1.size());
		System.out.println("set1 = " + set1);
		
		set1.remove(0);
		System.out.println("set1 - size=" + set1.size() +" -> "+ set1);
		boolean res = set1.containsAll(Arrays.asList(words));
		System.out.println("set1 contains all words ? " + res);
		
		res = set1.contains("Ee");
		System.out.println("set1 contains \"Ee\" ? " + res);
		
		set1.clear();
		set1 = Set.of(words); // unmodifiable set containing an arbitrary number of elements
		System.out.println("\nset1 - size=" + set1.size() +" -> "+ set1);
		res = set1.containsAll(Arrays.asList(words));
		System.out.println("set1 contains all words ? " + res);
		// set1.add("Dd"); // UnsupportedOperationException as Set.of(..)
		res = set1.contains("Ee");
		System.out.println("set1 contains \"Ee\" ? " + res);
		
		Set<String> set2 = Set.copyOf(set1); // unmodifiable set containing an arbitrary number of elements
		System.out.println("set2 - size=" + set2.size() +" -> "+ set2);
		//set2.remove("Cc"); // UnsupportedOperationException as Set.of(..)
		
	}
}
