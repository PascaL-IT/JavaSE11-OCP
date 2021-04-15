/** 
 * TestList 
 * <br>Covering ArrayList 
 */
package practice9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author PascaL
 *
 */
public class TestList {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] words = { "Aa", "Bb", "Cc" };
		System.out.println("words - length=" + words.length + " -> " + Arrays.toString(words));
		List<String> list0 = Arrays.asList(words);
		System.out.println("list0 - size=" + list0.size() + " -> " + list0);
		
		// List n° 1
		List<String> list1 = new ArrayList<>();
		System.out.println("\nlist1 - size=" + list1.size());
		list1.add("Dd");
		list1.add("Ee");
		list1.add("Ff");
		System.out.println("list1 - size=" + list1.size());
		System.out.println("list1 = " + list1);
		list1.remove(0);
		System.out.println("list1 = " + list1);
		System.out.println("add(), remove()... is supported on list1 as constructed with ArrayList");

		// List n° 2
		List<String> list2 = Arrays.asList(words); // Attention: modification on list2 will affect words and list0 -> fixed-size list backed by the specified array
		System.out.println("\nlist2 - size=" + list2.size() + " -> " + list2);
		try {
			list2.set(0, "Ee"); // !!! change allow - bridge on words !!!
			System.out.println("list2 - size=" + list2.size() + " -> " + list2 + " - fixed-sized, but mutable, as constructed with Arrays.asList(..)");
		} catch (Exception e) {
			System.out.println("Exception: add(), remove() ... NOT supported on list2 as constructed with Arrays.asList(..) - fixed-sized, but mutable");
		}
		
		// List n° 3
		List<String> list3 = List.of(words);
		list3 = List.of(list1.get(0), list1.get(1));
		//list3 = List.copyOf(list1);
		System.out.println("\nlist3 - size=" + list3.size() + " -> " + list3);
		try {
			// list3.add("Ff");
			// list3.remove(0);
			list3.set(0, "Bb");
			System.out.println("list3 - size=" + list3.size() + " -> " + list3);
		} catch (Exception e) {
			System.out.println("Exception: add(), remove() ... NOT supported on list3 as constructed with List.of(..) - immutable, as constructed with List.of(..)");
		}
		
		
		// List n° 4
		List<String> list4 = List.copyOf(list1);
		System.out.println("\nlist4 - size=" + list4.size() + " -> " + list4);
		try {
			//list4.add("Ff");
			list4.remove(0);
			//list4.set(0, "Bb");
			System.out.println("list4 - size=" + list4.size() + " -> " + list4);
		} catch (Exception e) {
			System.out.println("Exception: add(), remove()... NOT supported on list4 as constructed with List.copyOf(..) - immutable, as constructed with List.copyOf(..)");
		}

		
		// toArray conversion
		Object[] arrayWords = list0.toArray();
		System.out.println("\narrayWords - length=" + arrayWords.length + " -> " + Arrays.toString(arrayWords));
		System.out.println(Arrays.equals(words, arrayWords)); // IMPORTANT : Arrays.equals 
		
		
		// Ref.
		// https://www.geeksforgeeks.org/arrays-aslist-method-in-java-with-examples/
		// http://blog.paumard.org/cours/java-api/chap01-api-collection-collections.html

		
		
		/**
		 * IMPORTANT REMINDER :
		 * 
			 * The List.of and List.copyOf static factory methods provide a convenient way to create unmodifiable lists. 
			 * The List instances created by these methods have the following characteristics:
			 * They are unmodifiable. Elements cannot be added, removed, or replaced.
			 * Calling any mutator method on the List will always cause UnsupportedOperationException to be thrown.
			 * However, if the contained elements are themselves mutable, this may cause the List's contents to appear to change.
			 * They disallow null elements !!!
			 * Attempts to create them with null elements result in NullPointerException.
			 * They are serializable if all elements are serializable.
			 * The order of elements in the list is the same as the order of the provided arguments, or of the elements in the provided array.
			 * They are value-based. Callers should make no assumptions about the identity of the returned instances. Factories are free to create 
			 * new instances or reuse existing ones. Therefore, identity-sensitive operations on these instances (reference equality
			 * (==), identity hash code, and synchronization) are unreliable and should be avoided.
			 * They are serialized as specified on the Serialized Form page.
			 * 
			 * Ref. https://docs.oracle.com/javase/10/docs/api/java/util/List.html
		     * 	    https://docs.oracle.com/javase/10/docs/api/java/util/Arrays.html#asList(T...)
		 * 
		 */
		
	}
}

