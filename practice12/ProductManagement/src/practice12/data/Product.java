package practice12.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.RoundingMode.HALF_UP;

/**
 * {@code Product} class represents properties and behaviors of product objects
 * in the Product Management System.<br>
 * Each product has an id, name, rating and price.<br>
 * {@link DISCOUNT_RATE discount rate}
 * 
 * @author PascaL
 * @version 4.0 <br>
 * 
 *          <br>
 *          *************** <br>
 *          History: <br>
 *          2.0 : add rating
 * 			3.0 : add Rateable interface
 *          4.0 : remove setters (a Product is immutable) 
 */

public abstract class Product implements Rateable<Product> {

	/**
	 * A constant that defines a {@link java.math.BigDecimal BigDecimal} value of
	 * the discount rate.<br>
	 * Discount rate is 10%.<br>
	 * Rating is defined with zero, one, up to five stars.<br>
	 * 
	 * @author PascaL
	 * @version 2.0
	 * 
	 */
	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.10); // a constant

	private int id;
	private String name;
	private BigDecimal price;
	private Rating rating;

	/* Constructors */
	public Product() {
		this(0, "no_name", BigDecimal.ZERO);
	}
	
	public Product(int id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = Rating.NOT_RATED; // default
	}

	public Product(int id, String name, BigDecimal price, Rating rating) {
		this(id, name, price);
		this.rating = rating;
	}

	
	/* Getters only */

	/**
	 * Get product id
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get product name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get product price
	 * 
	 * @return price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	
	/**
	 * Get product rating defined with a set of stars
	 * <br>Add Override annotation as present in Rateable interface (best practice)
	 * @return rating stars
	 */
	@Override
	public Rating getRating() {
		return rating;
	}


	/**
	 * Get discount applied on price (logic and computation)
	 * 
	 * @return discount i.e. the price with the discount applied on it
	 */
	public BigDecimal getPriceDiscount() {

		if (this.price == null) {
			return BigDecimal.ZERO;
		}

		return this.price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
	}
	
	/**
	 * getBestBefore - for a Product, it's the current date  
	 * @return LocalDate today now
	 */
	public LocalDate getBestBefore() {
		return LocalDate.now();
	}
	

	//  Obsoleted by interface Rateable
	//	/**
	//	 * Apply rating on product
	//	 * @param rating Rating
	//	 * @return Product 
	//	 */
	//	public abstract Product applyRating(Rating rating);
	//	{
	// 		return new Product(this.id, this.name, this.price, rating);
	//	}
	

	@Override
	public String toString() {
		return getClass().getSimpleName()  + " [id=" + id + ", name=" + name + ", price=" + price + ", rating=" + rating + ", bestBefore=" + getBestBefore() + "] - hashCode=" + hashCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		Product other = (Product) obj;
		if (id != other.id)
			return false;
// TODO - decide or verify if OK to distinguish Product only by id
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		if (price == null) {
//			if (other.price != null)
//				return false;
//		} else if (!price.equals(other.price))
//			return false;
//		if (rating != other.rating)
//			return false;
		return true;
	}

	
	
}
