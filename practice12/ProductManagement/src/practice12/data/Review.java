package practice12.data;


/**
 * {@code Review} class represents product review.
 * 
 * @author PascaL
 * @version 1.0 <br>
 */
public class Review implements Comparable<Review> {

	private Rating rating;
	private String comments;
	
	public Review(Rating rating, String comments) {
		this.rating = rating;
		this.comments = comments;
	}

	public Rating getRating() {
		return rating;
	}

	public String getComments() {
		return comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (rating != other.rating)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Review [rating=" + rating + ", comments=" + comments + "]";
	}

	@Override
	/**
	 * compareTo used to sort the reviews by rating ordered descendant
	 * @param arg0
	 * @return
	 */
	public int compareTo(Review otherReview) {
		int naturalOrder = this.rating.getRank() - otherReview.getRating().getRank(); // + if greater , - if lower , 0 if equal
		return -naturalOrder; // reverse , i.e. from highest to lowest (5 -> 0)
	}
	
	
	
}
