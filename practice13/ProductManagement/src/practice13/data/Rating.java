/**
 * 
 */
package practice13.data;

import java.util.HashMap;
import java.util.Map;


/**
 * {@Code Rating} Enumeration of Rating stars <br>
 * <br>
 * Reminders:<br>
 * enum is always implicitly public static final<br>
 * enum constructor and variable with a default or private access<br>
 * 
 * @author PascaL
 * @version 3.0 <br>
 *          <br>
 *          History: <br>
 *          2.0 : add rating <br>
 *          3.0 : add several methods : valueOfNbrStars, getPosition,
 *          getByName<br>
 *          <br>
 */
public enum Rating {

	NOT_RATED("\u2606\u2606\u2606\u2606\u2606"), ONE_STAR("\u2605\u2606\u2606\u2606\u2606"),
	TWO_STAR("\u2605\u2605\u2606\u2606\u2606"), THREE_STAR("\u2605\u2605\u2605\u2606\u2606"),
	FOUR_STAR("\u2605\u2605\u2605\u2605\u2606"), FIVE_STAR("\u2605\u2605\u2605\u2605\u2605");

	String stars; // defined with star characters
	private static final Map<Integer, Rating> RATING_STARS = new HashMap<>();

	static {
		Rating[] values = Rating.values(); // enum.values() returns an array
		var i = 0;
		for (Rating r : values) {
			RATING_STARS.put(Integer.valueOf(i++), r);
		}
		assert i + 1 != 6 : "Invalid number of ratings " + i + " vs. (0..5)";
		assert values.length + 1 != 6 : "Invalid number of rating values " + values.length + " vs. (0..5)";
	}

	/**
	 * Rating constructor for this enum
	 * 
	 * @param rating
	 */
	Rating(String stars) {
		this.stars = stars;
	}

	/**
	 * Get rating with stars
	 * 
	 * @return stars String
	 */
	public String getStars() {
		return stars;
	}

	/**
	 * Get rating representation
	 * 
	 * @return String
	 */
	public String toString() {
		return name() + "/" + getStars() + " (ordinal=" + ordinal() + ")";
	}

	/**
	 * Get rating value (which is the position in Enum array)... alias of ordinal().
	 * 
	 * @return int position
	 * @throws Exception 
	 */
	public int getRank() {
		return ordinal();
	}
	
	/**
	 * Value of number of stars <br>
	 * Idea got from https://www.baeldung.com/java-enum-values
	 * 
	 * @param nbrOfStar
	 * @return Rating
	 */
	public static Rating valueOfStars(int nbrOfStar) {
		return RATING_STARS.get(nbrOfStar);
	}

	/**
	 * Get by his name (which is the same as enum.valueOf)
	 * 
	 * @param name
	 * @return
	 */
	public static Rating getByName(String name) {
		return valueOf(name);
	}

}
