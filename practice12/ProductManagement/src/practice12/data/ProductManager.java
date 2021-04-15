package practice12.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * {@code ProductManager} class represents a manager to build, search, review
 * and print reports on products.
 * 
 * @author PascaL
 * @version 6.0 <br>
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
 *          4.0 : add ResourceFormatter (inner/nested class), <br>
 *          add static Map<String, ResourceFormatter> formatters, ...<br>
 *          5.0 : replace for... loops by stream in findProduct, reviewProduct,
 *          printProductReport, printProducts<br>
 *          add getDiscounts()<br>
 *          6.0 : add custom exception when no product found<br>
 *          add config, two message formats and a new method to parse
 *          reviews<br>
 *          <br>
 */
public class ProductManager {

	private static Logger logger = Logger.getLogger(practice12.data.ProductManager.class.getName());
	private static final String CONFIG_PATH = "practice12.data.config";

	private Map<Product, List<Review>> products;
	private ResourceFormatter formatter;

	private ResourceBundle configBundle = ResourceBundle.getBundle(CONFIG_PATH);
	private MessageFormat productFormat = new MessageFormat(configBundle.getString("product.data.format"));
	private MessageFormat reviewFormat = new MessageFormat(configBundle.getString("review.data.format"));

	/**
	 * Resource formatters per Locale
	 * https://www.localeplanet.com/java/fr-BE/index.html
	 * https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html
	 */
	private static Map<String, ResourceFormatter> formatters = Map.of("en-GB", new ResourceFormatter(Locale.UK),
			"fr-FR", new ResourceFormatter(Locale.FRANCE), "fr-BE",
			new ResourceFormatter(Locale.forLanguageTag("fr-BE")));

	/**
	 * ProductManager constructor
	 * 
	 * @param locale
	 */
	public ProductManager(Locale locale) {
		this(locale.toLanguageTag());
	}

	/**
	 * ProductManager constructor
	 * 
	 * @param languageTag
	 */
	public ProductManager(final String languageTag) {
		changeLocale(languageTag);
		this.products = new HashMap<>();
	}

	/**
	 * Change locale along with resource formatter
	 * 
	 * @param langageTag
	 */
	public void changeLocale(final String languageTag) {
		ResourceFormatter defaultFormatter = formatters.get("en-GB");
		formatter = formatters.getOrDefault(languageTag, defaultFormatter);
		System.out.println("Locale is set on " + formatter.getLocale() + " "
				+ (formatter.getLocale().toLanguageTag().equals("en-GB") ? "(default)" : ""));
	}

	/**
	 * Get set of supported locale
	 * 
	 * @returnSet<String>
	 */
	public static Set<String> supportedLocale() {
		return formatters.keySet();
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
	 * comments. It returns the product with the new rating.<br>
	 * Compute rating average off all reviews and apply it to produt via Stream.<br>
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

		// Compute rating average off all reviews (via stream)
		double ratingAverage = reviews.stream().mapToInt(r -> r.getRating().getRank()).average().orElse(0);
		// System.out.format("\nDEBUG - reviewProduct: %s => ratingAverage=%f (<< %s)\n
		// ", product, ratingAverage, rating);
		product = product.applyRating(Rateable.convert((int) Math.round(ratingAverage)));
		// Add product with his reviews to the map
		this.products.put((Product) product, reviews);
		return product;
	}

	/**
	 * Review on a rateable product<br>
	 * 
	 * @param productId
	 * @param rating
	 * @param review
	 * @return Rateable<Product> product with new rating (or null if not found)
	 * @throws ProductManagerException
	 */
	public Rateable<Product> reviewProduct(int productId, Rating rating, String review) {
		try {
			return reviewProduct(findProduct(productId), rating, review);
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * Print a product report
	 * 
	 * @param productId
	 * @throws ProductManagerException
	 */
	public void printProductReport(int productId) {
		try {
			printProductReport(findProduct(productId));
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
			// e.printStackTrace();
		}
	}

	/**
	 * Print product report (using stream instead of for... loop)
	 * 
	 * @param product
	 */
	public void printProductReport(Rateable<Product> product) {
		// Retrieve list of reviews
		List<Review> reviews = products.get(product);
		Collections.sort(reviews); // sorted by ...

		// Header (title)
		// Report-A) product = {0}, Price: {1}, Rating: {2}, Best Before: {3}
		StringBuilder txt = new StringBuilder(formatter.getResourceText("title"));
		txt.append(formatter.formatProduct((Product) product));
		txt.append("'\n");

		// Report-B) review = {0}\t{1}
		// In the case, there is no review ...
		if (reviews.isEmpty()) {
			if (!product.getRating().equals(Rateable.DEFAULT_RATING)) {
				txt.append(formatter.getResourceText("no.review.but.rated"));
			} else {
				txt.append(formatter.getResourceText("no.reviews"));
			}
		}
		// In the case, there are one or more reviews on the product
		else {
			AtomicInteger counter = new AtomicInteger(1); // TIP -
															// https://www.javaquery.com/2015/07/how-to-iterate-over-stream-and.html
			txt.append(reviews.stream().map(r -> formatter.formatReview(r, counter.getAndIncrement()))
					.collect(Collectors.joining("\n"))).append('\n');
		}

//		int index = 0;
//		for (Review review : reviews) {
//			if (review != null) {
//				txt.append(formatter.formatReview(review, ++index));
//				txt.append('\n');
//			}
//		}
//		txt.append('\n');

		System.out.println(txt);
	}

	/**
	 * Print report on products based on comparison (using stream instead of for...
	 * loop)<br>
	 * See https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
	 * 
	 * @param comparator
	 */
	public void printProducts(Comparator<Product> comparator) {
		StringBuilder txt = new StringBuilder(formatter.getResourceText("report.products"));
		AtomicInteger counter = new AtomicInteger(1);
		getProducts().keySet().stream().sorted(comparator).forEach((p) -> {
			txt.append("[");
			txt.append(counter.getAndIncrement()).append("] ");
			txt.append(formatter.formatProduct(p));
			txt.append('\n');
		});
		System.out.println(txt);
	}

	/**
	 * Print report on products based on comparison and a filter (using stream
	 * instead of for... loop)<br>
	 * See
	 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Predicate.html
	 * 
	 * @param comparator
	 * @param filter
	 */
	public void printProducts(Comparator<Product> comparator, Predicate<Product> filter) {
		StringBuilder txt = new StringBuilder(formatter.getResourceText("report.products.filtered"));
		AtomicInteger counter = new AtomicInteger(1);
		txt.append(getProducts().keySet().stream().sorted(comparator).filter(filter)
				.map(p -> "[" + counter.getAndIncrement() + "] " + formatter.formatProduct(p))
				.collect(Collectors.joining("\n"))).append('\n');
		System.out.println(txt);
	}

	/**
	 * Get list of products with their reviews
	 * 
	 * @return Map<Product, List<Review>>
	 */
	public Map<Product, List<Review>> getProducts() {
		return products;
	}

	/**
	 * Get discounts - calculate a total of all discount values for each group of
	 * products that have the same rating.
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getDiscounts() {
		return products.keySet().stream()
				.collect(Collectors.groupingBy(p -> p.getRating().getStars(),
						Collectors.collectingAndThen(Collectors.summingDouble(p -> p.getPriceDiscount().doubleValue()), // downstream
								d -> formatter.priceFormat.format(d)) // finisher
				));
	}

	/**
	 * Find a product by his Id (via Predicate & Stream)
	 * 
	 * @param id
	 * @return Product
	 * @throws ProductManagerException
	 */
	public Product findProduct(int id) throws ProductManagerException {
		Predicate<Product> condition = p -> p.getId() == id;
		return this.products.keySet().stream().filter(condition)
				// .peek((p) -> { System.out.format("\nSTREAM DEBUG -> findProduct for id=%d is
				// %s\n", id, p); })
				.findFirst()
				// .get(); // fix NullPointerException due to null returned by .orElseGet(() ->
				// null);
				.orElseThrow(() -> new ProductManagerException("Product with Id=" + id + " is not found ! \n"));
	}

	/**
	 * checkRating
	 * 
	 * @param rating
	 * @throws ProductManagerException
	 */
	private void checkRating(final int rating) throws ProductManagerException {
		if (rating < 0 || rating > 5) {
			throw new ProductManagerException(
					"Invalid rating value (must be between 0 and " + (Rating.values().length - 1) + ")");
		}
	}

	/**
	 * Review a product by parsing the input String
	 * 
	 * @param source (comma as separator - see config.properties)
	 * @throws ProductManagerException
	 */
	public void parseReview(final String inputText) throws ProductManagerException {
		try {
			Object[] values = reviewFormat.parse(inputText);
			// Product Id
			int productId = Integer.valueOf((String) values[0]);
			// Rating value and check if in range
			int rating = Integer.valueOf((String) values[1]);
			checkRating(rating);
			reviewProduct(productId, Rating.valueOfStars(rating), (String) values[2]);
		} catch (ParseException | NumberFormatException ex) {
			// logger.log(Level.WARNING, "Error while parsing review for '"+inputText+"' -
			// aborted", ex);
			logger.log(Level.WARNING, "Error while parsing review for '" + inputText + "' - aborted");
		}
	}

	/**
	 * Create a product by parsing the input String
	 * 
	 * @param inputText - (comma as separator - see config.properties)
	 * @throws ProductManagerException
	 */
	public void parseProduct(final String inputText) throws ProductManagerException {
		try {
			Object[] values = productFormat.parse(inputText);
			// Product type
			String type = (String) values[0];
			// Product Id
			int productId = Integer.valueOf((String) values[1]);
			// Product name 
			String name = (String) values[2];
			// Product price
			BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
			// Product rating
			int rating = Integer.valueOf((String) values[4]);
			checkRating(rating);
			Rating stars = Rating.valueOfStars(rating);
			//
			switch (type) {
				case "D" :
					getProduct(productId, name, price, stars); // create Drink
					break;
				case "F" :
					String aDate = (String) values[5];
					LocalDate date = LocalDate.parse(aDate);
					getProduct(productId, name, price, stars, date); // create Food
					break;
				default:
					throw new ProductManagerException(
							"Invalid product type (must be either 'D' for Drink, or 'F' for Food)");
			}
			
		} catch (ParseException | NumberFormatException | DateTimeParseException ex) {
			logger.log(Level.WARNING, "Error while parsing product for '"+inputText+"' - aborted", ex);
		}
	}

	/**
	 * ResourceFormatter (static nested class)
	 * 
	 * @author PascaL
	 */
	private static class ResourceFormatter {

		private static final String RESOURCES_PATH = "practice12.data.resources";

		private Locale locale;
		private ResourceBundle resources;
		private DateTimeFormatter dateFormat;
		private NumberFormat priceFormat;

		ResourceFormatter(Locale locale) {
			this.locale = locale;
			this.resources = ResourceBundle.getBundle(RESOURCES_PATH, this.locale);
			// this.dateFormat =
			// DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
			// => Unsupported field: HourOfDay
			this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").localizedBy(locale);
			this.priceFormat = NumberFormat.getCurrencyInstance(locale);
		}

		String formatProduct(Product product) {
			return MessageFormat.format(resources.getString("product"), product.getName(),
					priceFormat.format(product.getPrice()), product.getRating().getStars(),
					dateFormat.format(product.getBestBefore()), product.getId());
		}

		String formatReview(Review review, int index) {
			return MessageFormat.format(resources.getString("review"), index, review.getRating().getStars(),
					review.getComments());
		}

		String getResourceText(String key) {
			return resources.getString(key);
		}

		Locale getLocale() {
			return locale;
		}
	}

}
