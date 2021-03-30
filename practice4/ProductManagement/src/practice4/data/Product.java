package practice4.data;

import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;

/**
 * {@code Product} class represents properties and behaviors of product objects
 * in the Product Management System.<br>
 * Each product has an id, name, and price.<br>
 * {@link DISCOUNT_RATE discount rate}
 * 
 * @author PascaL
 * @version 1.0
 * 
 */

public class Product {

	/**
	 * A constant that defines a {@link java.math.BigDecimal BigDecimal} value of
	 * the discount rate.<br>
	 * Discount rate is 10%.<br>
	 * 
	 * @author PascaL
	 * @version 1.0
	 * 
	 */
	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.10); // a constant

	private int id;
	private String name;
	private BigDecimal price;

	
	/* Getters and setters */

	/**
	 * Get product id
	 * @return id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set product id
	 * @param id product id
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Set product name
	 * @return name 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get product name
	 * @param name product name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Set product price
	 * @return price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Get product price
	 * @param price product price
	 */
	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	/**
	 * Get discount applied on price (logic and computation) 
	 * @return discount i.e. the price with the discount applied on it
	 */
	public BigDecimal getPriceDiscount() {

		if (this.price == null) {
			return BigDecimal.ZERO;
		}

		return this.price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
	}
}
