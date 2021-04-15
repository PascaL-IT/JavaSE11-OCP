package practice9;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * TestMap <br>
 * Covering HashMap
 */
public class TestMap {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String e = "Ee";
		String[] words = { "Aa", "Bb", "Cc" };
		System.out.println("words - length=" + words.length + " -> \"Aa\", \"Bb\", \"Cc\"");

		Map<Integer, String> map1 = new HashMap<>();
		System.out.println("map1 - size=" + map1.size() + " -> " + map1);

		map1.put(Integer.valueOf(1),"Dd");
		map1.put(Integer.valueOf(2),"Ee");
		map1.put(Integer.valueOf(3),"Ff");
		map1.put(Integer.valueOf(4),"Ff"); // duplicated are silently discarded
		System.out.println("map1 - size=" + map1.size() + " -> " + map1);
		System.out.println("map1 - size=" + map1.size() + " -> keys = " + map1.keySet());
		System.out.println("map1 - size=" + map1.size() + " -> values = " + map1.values());
		
	}
	
}		
