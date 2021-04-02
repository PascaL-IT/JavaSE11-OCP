package practice8.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
	private Review[] reviews;

	/**
	 * ProductManager constructor
	 * 
	 * @param locale
	 */
	public ProductManager(Locale locale) {
		this.locale = locale;
		this.resources = ResourceBundle.getBundle("practice8.data.resources", this.locale);
		// this.dateFormat =
		// DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		// // => Unsupported field: HourOfDay
		this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").localizedBy(locale);
		this.priceFormat = NumberFormat.getCurrencyInstance(locale);
		this.reviews = new Review[5];
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
	 * @return Product product
	 */
	public Product getProduct(int id, String name, BigDecimal price, Rating rating) {
		product = new Drink(id, name, price, rating);
		return product;
	}

	/**
	 * Review on a rateable product<br>
	 * This method allows you to apply a new rating and write a review with
	 * comments.
	 * 
	 * @param product
	 * @param rating
	 * @param review
	 * @return Rateable<Product>
	 */
	public Rateable<Product> reviewProduct(Rateable<Product> product, Rating rating, String review) {
		Review newReview = new Review(rating, review);
		// Check if Array of reviews is full, then increase it's size by 5
		int length = this.reviews.length;
		if (this.reviews[length - 1] != null) {
			this.reviews = Arrays.copyOf(this.reviews, length + 5);
		}
		// Add the new review into Array of reviews and compute rating average
		int sum = 0, i = 0;
		for (Review r : this.reviews) {
			if (r == null) {
				this.reviews[i++] = newReview;
				sum += newReview.getRating().ordinal();
				break;
			}
			sum += this.reviews[i++].getRating().ordinal();
		}
		// Apply average rating
		float ratingAverage = (float) sum / i;
		// System.out.println("DEBUG - Computed rating average=" + ratingAverage + ",
		// round value="+Math.round(ratingAverage));
		this.product = product.applyRating(Math.round(ratingAverage));
		return product;
	}

	/**
	 * Print a product report
	 */
	public void prindProductReport() {

		// Report-A) product = {0}, Price: {1}, Rating: {2}, Best Before: {3}
		StringBuilder txt = new StringBuilder("\nProduct report of '");
		txt.append(MessageFormat.format(resources.getString("product"), product.getName(),
				priceFormat.format(product.getPrice()), product.getRating(),
				dateFormat.format(product.getBestBefore())));
		txt.append("'\n");
		// Report-B) review = {0}\t{1}
		int i = 0;
		for (Review review : this.reviews) {
			if (review != null) {
				txt.append(MessageFormat.format(resources.getString("review"), ++i, review.getRating().getStars(),
						review.getComments()));
				txt.append('\n');
			}
		}
		// In the case, there is no review
		if (i == 0 && !product.getRating().equals(Rateable.DEFAULT_RATING)) {
			txt.append(resources.getString("no.review.but.rated"));
		} else if (i == 0) {
			txt.append(resources.getString("no.reviews"));
		}
		//
		txt.append('\n');
		System.out.println(txt);
	}
}
