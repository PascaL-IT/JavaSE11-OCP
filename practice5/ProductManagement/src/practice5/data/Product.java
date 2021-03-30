package practice5.data;

import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;
// import static practice5.data.Rating.*;

/**
 * {@code Product} class represents properties and behaviors of product objects
 * in the Product Management System.<br>
 * Each product has an id, name, rating and price.<br>
 * {@link DISCOUNT_RATE discount rate}
 * 
 * @author PascaL
 * @version 2.0 <br>
 * 
 *          <br>
 *          *************** <br>
 *          History: <br>
 *          2.0 : add rating
 * 
 */

public class Product {

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
		this.rating = Rating.NOT_RATED;
	}

	public Product(int id, String name, BigDecimal price, Rating rating) {
		this(id, name, price);
		this.rating = rating;
	}

	/* Getters and setters */

	/**
	 * Get product id
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set product id
	 * 
	 * @param id product id
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Set product name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get product name
	 * 
	 * @param name product name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Set product price
	 * 
	 * @return price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Get product rating defined with a set of stars
	 * 
	 * @return rating stars
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * Set product rating
	 * 
	 * @param rating
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}

	/**
	 * Get product price
	 * 
	 * @param price product price
	 */
	public void setPrice(final BigDecimal price) {
		this.price = price;
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
	 * Apply rating on product
	 * @param rating Rating
	 * @return Product 
	 */
	public Product applyRating(Rating rating) {
		return new Product(this.id, this.name, this.price, rating);
	}
	

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", rating=" + rating + "]";
	}

}
