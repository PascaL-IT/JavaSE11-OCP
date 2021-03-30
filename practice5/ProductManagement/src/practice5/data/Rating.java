/**
 * 
 */
package practice5.data;

/**
 * {@Code Rating} Enumeration of Rating stars
 * 
 * @author PascaL
 * <br>
 * Rem. enum is always implicitly public static final<br>
 *      enum constructor and variable with a default or private access
 */
public enum Rating {

	NOT_RATED  ("\u2606\u2606\u2606\u2606\u2606") ,
	ONE_STAR   ("\u2605\u2606\u2606\u2606\u2606") ,
	TWO_STAR   ("\u2605\u2605\u2606\u2606\u2606") ,
	THREE_STAR ("\u2605\u2605\u2605\u2606\u2606") ,
	FOUR_STAR  ("\u2605\u2605\u2605\u2605\u2606") ,
	FIVE_STAR  ("\u2605\u2605\u2605\u2605\u2605") ;
	
	String stars; // defined with star characters 
	
	/**
	 * Rating constructor for this enum
	 * @param rating
	 */
	Rating(String stars) {
		this.stars = stars;
	}

	/**
	 * Get rating with stars
	 * @return stars String 
	 */
	public String getStars() {
		return stars;
	}
	
	/**
	 * Get rating representation
	 * @return String
	 */
	public String toString() {
		return name() + "/"+ getStars();
	}
		
}
