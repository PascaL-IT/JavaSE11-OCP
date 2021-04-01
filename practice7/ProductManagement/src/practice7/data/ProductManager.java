package practice7.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * {@code ProductManager} class represents a manager to build and review
 * products.
 * 
 * @author PascaL
 * @version 2.0 <br>
 * 
 *          <br>
 *          *************** <br>
 *          History: <br>
 *          2.0 : remove static and change type to Product<br>
 *          add product and review private variables, <br>
 *          add constructor with various initializations, <br>
 *          add ... , <br>
 *          <br>
 * 
 */
public class ProductManager {

	private Locale locale;
	private ResourceBundle resources;
	private DateTimeFormatter dateFormat;
	private NumberFormat priceFormat;

	private Product product;
	private Review review;

	/**
	 * ProductManager constructor
	 * 
	 * @param locale
	 * @param ressource
	 * @param dateFormat
	 * @param priceFormat
	 * @param product
	 * @param review
	 */
	public ProductManager(Locale locale) {
		this.locale = locale;
		this.resources = ResourceBundle.getBundle("practice7.data.resources", this.locale); 
		// this.dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale); // => Unsupported field: HourOfDay
		this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").localizedBy(locale); 
		this.priceFormat = NumberFormat.getCurrencyInstance(locale);
	}

	/**
	 * Get food product with a default rating and a default best-before date
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @return Product food
	 */
	public Product getProduct(int id, String name, BigDecimal price) {
		product = new Food(id, name, price);
		return product;
	}

	/**
	 * Get food product
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @param bestBefore
	 * @return Product food
	 */
	public Product getProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		product = new Food(id, name, price, rating, bestBefore);
		return product;
	}

	/**
	 * Get drink product
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @return Rateable product
	 */
	public Rateable<Product> getProduct(int id, String name, BigDecimal price, Rating rating) {
		product = new Drink(id, name, price, rating);
		return product;
	}

	/**
	 * Review on a product - apply a new rating and write a review with comments
	 * 
	 * @param product
	 * @param rating
	 * @param review  comments
	 * @return Product
	 */
	public Product reviewProduct(Product product, Rating rating, String review) {
		this.product = product.applyRating(rating);
		this.review = new Review(rating, review);
		return product;
	}

	/**
	 * Print a product report
	 */
	public void prindProductReport() {
		StringBuilder txt = new StringBuilder();
		// A) product = {0}, Price: {1}, Rating: {2}, Best Before: {3}
		txt.append(MessageFormat.format(resources.getString("product"), product.getName(),
				priceFormat.format(product.getPrice()), product.getRating(),
				dateFormat.format(product.getBestBefore())));
		txt.append('\n');
		// B) review = {0}\t{1}
		if (review != null) {
			txt.append(MessageFormat.format(resources.getString("review"), review.getRating().getStars(), review.getComments()));
		} else if (!product.getRating().equals(Rateable.DEFAULT_RATING)) {
			txt.append(resources.getString("no.review.but.rated"));
		} else {
			txt.append(resources.getString("no.reviews"));
		}
		txt.append('\n');
		System.out.println(txt);
	}
}
