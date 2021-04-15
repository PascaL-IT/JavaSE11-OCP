package practice9.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.List;

/**
 * {@code ProductManager} class represents a manager to build, search, review
 * and print reports on products.
 * 
 * @author PascaL
 * @version 3.0 <br>
 * 
 *          <br>
 *          *************** <br>
 *          History: <br>
 *          2.0 : remove static and change type to Product<br>
 *          add product and review private variables, <br>
 *          add constructor with various initializations, ... <br>
 *          3.0 : add HashMap of Products, List of Reviews <br>
 *          adapt many methods<br>
 *          add new methods<br>
 *          <br>
 * 
 */
public class ProductManager {

	private Locale locale;
	private ResourceBundle resources;
	private DateTimeFormatter dateFormat;
	private NumberFormat priceFormat;
	private Map<Product, List<Review>> products;
//	private Product product;
//	private Review[] reviews;

	/**
	 * ProductManager constructor
	 * 
	 * @param locale
	 */
	public ProductManager(Locale locale) {
		this.locale = locale;
		this.resources = ResourceBundle.getBundle("practice9.data.resources", this.locale);
		// this.dateFormat =
		// DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
		// // => Unsupported field: HourOfDay
		this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").localizedBy(locale);
		this.priceFormat = NumberFormat.getCurrencyInstance(locale);
		// this.reviews = new Review[5];
		this.products = new HashMap<>();
	}

	/**
	 * Create food product with a default rating and a default best-before date
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @return Product food
	 */
	public Product getProduct(int id, String name, BigDecimal price) {
		Product product = new Food(id, name, price);
		products.putIfAbsent(product, new ArrayList<Review>());
		return product;
	}

	/**
	 * Create food product
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @param bestBefore
	 * @return Product food
	 */
	public Product getProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		Product product = new Food(id, name, price, rating, bestBefore);
		products.putIfAbsent(product, new ArrayList<Review>());
		return product;
	}

	/**
	 * Create drink product
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @return Product product
	 */
	public Product getProduct(int id, String name, BigDecimal price, Rating rating) {
		Product product = new Drink(id, name, price, rating);
		products.putIfAbsent(product, new ArrayList<Review>());
		return product;
	}

	/**
	 * Review on a rateable product<br>
	 * This method allows you to apply a new rating and write a review with
	 * comments. It returns the product with the new rating.
	 * 
	 * @param product
	 * @param rating
	 * @param review
	 * @return Rateable<Product> product with new rating
	 */
	public Rateable<Product> reviewProduct(Rateable<Product> product, Rating rating, String review) {
		// Retrieve list of reviews
		List<Review> reviews = products.get(product);
		if (reviews == null) {
			reviews = new ArrayList<>();
		}
		// Remove current product from the map
		this.products.remove(product, reviews);
		// Create new review and add it to the list
		Review newReview = new Review(rating, review);
		reviews.add(newReview);
		// Compute rating average off all reviews
		int sum = 0;
		for (Review r : reviews) {
			sum += r.getRating().ordinal();
		}
		// Apply average rating
		float ratingAverage = (float) sum / (reviews.size());
		// System.out.println("DEBUG - Product=" + product + " - computed rating
		// average=" + ratingAverage + ", round value="+Math.round(ratingAverage));
		// Apply new rating on the product
		product = product.applyRating(Math.round(ratingAverage));
		// Add product with his reviews to the map
		this.products.put((Product) product, reviews);
		return product;
	}

	/**
	 * Review on a rateable product<br>
	 * @param productId
	 * @param rating
	 * @param review
	 * @return Rateable<Product> product with new rating
	 */
	public Rateable<Product> reviewProduct(int productId, Rating rating, String review) {
		return reviewProduct(findProduct(productId), rating, review);
	}
	
	/**
	 * Print a product report
	 * 
	 * @param productId
	 */
	public void prindProductReport(int productId) {
		prindProductReport(findProduct(productId));
	}

	/**
	 * Print product report
	 * 
	 * @param product
	 */
	public void prindProductReport(Rateable<Product> product) {
		// Retrieve list of reviews
		List<Review> reviews = products.get(product);
		Collections.sort(reviews);
		// Report-A) product = {0}, Price: {1}, Rating: {2}, Best Before: {3}
		StringBuilder txt = new StringBuilder("\nProduct report of '");
		txt.append(MessageFormat.format(resources.getString("product"), ((Product) product).getName(),
				priceFormat.format(((Product) product).getPrice()), product.getRating(),
				dateFormat.format(((Product) product).getBestBefore())));
		txt.append("'\n");
		// Report-B) review = {0}\t{1}
		int i = 0;
		for (Review review : reviews) {
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

	/**
	 * Get list of products with their reviews
	 * 
	 * @return getProducts
	 */
	public Map<Product, List<Review>> getProducts() {
		return products;
	}

	/**
	 * Find a product by his Id
	 * 
	 * @param id
	 * @return Product
	 */
	public Product findProduct(int id) {
		for (Product p : this.products.keySet()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	// TODO - can we find product by id via Predicate 
	// https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html ?

}
