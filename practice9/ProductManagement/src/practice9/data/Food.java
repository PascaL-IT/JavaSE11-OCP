package practice9.data;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class Food extends Product {
	
	private LocalDate bestBefore;
	
	private static final LocalDate DEFAULT_BESTBEFORE = LocalDate.now().plus(3, ChronoUnit.DAYS);
	
	
	/**
	 * Food constructor n°1
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @param bestBefore
	 */
	Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		super(id, name, price, rating);
		this.bestBefore = bestBefore;
	}
	
	/**
	 * Food constructor n°2
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @param bestBefore
	 */
	Food(int id, String name, BigDecimal price) {
		super(id, name, price);
		this.bestBefore = DEFAULT_BESTBEFORE;
	}
	
	
	/**
	 * getBestBefore
	 * @return LocalDate bestBefore
	 */
	public LocalDate getBestBefore() {
		return this.bestBefore;
	}

	
	
	@Override
	/**
	 * Get price discount on condition that bestBefore value date is today
	 */
	public BigDecimal getPriceDiscount() {
		return (this.bestBefore.equals(LocalDate.now())) ? super.getPriceDiscount() : BigDecimal.ZERO;
	}

//	@Override - see parent (Product)
//	public String toString() {
//		return "Food [getId()=" + getId() + ", getName()=" + getName() + ", getPrice()="
//				+ getPrice() + ", getRating()=" + getRating() + ", bestBefore=" + bestBefore + "] - hashCode=" + hashCode();
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bestBefore == null) ? 0 : bestBefore.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Food other = (Food) obj;
		if (bestBefore == null) {
			if (other.bestBefore != null)
				return false;
		} else if (!bestBefore.equals(other.bestBefore))
			return false;
		return true;
	}

	@Override
	public Product applyRating(Rating rating) {
		return new Food(super.getId(), super.getName(), super.getPrice(), rating, this.bestBefore);
	}
	

}
