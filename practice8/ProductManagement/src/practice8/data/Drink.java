package practice8.data;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Drink extends Product {

	
	/**
	 * Drink constructor n°1 (to allow setup via setters)
	 */
	public Drink() {
		super();
	}
	
	/**
	 * Drink constructor n°2
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 */
	Drink(int id, String name, BigDecimal price, Rating rating) {
		super(id, name, price, rating);
	}

	@Override
	/**
	 * Get price discount on condition that local time is between 17:30 and 19:30.
	 */
	public BigDecimal getPriceDiscount() {
		LocalTime lt = LocalTime.now();
		return (lt.isAfter(LocalTime.of(17, 30)) || lt.isBefore(LocalTime.of(19, 30))) ? super.getPriceDiscount()
				: BigDecimal.ZERO;
	}

//	@Override - see parent (Product)
//	public String toString() {
//		return "Drink [getId()=" + getId() + ", getName()=" + getName() + ", getPrice()=" + getPrice()
//				+ ", getRating()=" + getRating() + "] - hashCode=" + hashCode();
//	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public Product applyRating(Rating rating) {
		return new Drink(super.getId(), super.getName(), super.getPrice(), rating);
	}

}
