package practice14.data;

/**
 * {@code Rateable<T>} interface represents an functional interface e.g. for the
 * products in the Shop.
 * 
 * @author PascaL
 * @version 1.0
 */
@FunctionalInterface
public interface Rateable<T> {

	public static final Rating DEFAULT_RATING = Rating.NOT_RATED;

	/* Functional Interface (only one abstract method) */
	T applyRating(Rating rating);

	/* Default methods */ 
	default T applyRating(int nbrStars) {
		return applyRating(convert(nbrStars));
	}

	/**
	 * Get default rating
	 * @return
	 */
	default Rating getRating() {
		return DEFAULT_RATING;
	}

	/* Static method (public or private, not default) */
	/**
	 * Convert nbr. of stars value to rating type 
	 * @param nbrStars
	 * @return Rating
	 */
	static Rating convert(int nbrStars) {
		Rating r = (nbrStars >= 0 && nbrStars <= 5) ? Rating.valueOfStars(nbrStars) : DEFAULT_RATING;
		//System.out.println("DEBUG - convert: " + r);
		return r;
	}

}
