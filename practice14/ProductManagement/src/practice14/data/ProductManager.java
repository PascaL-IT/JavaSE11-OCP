package practice14.data;

//import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
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
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * {@code ProductManager} class represents a manager to build, search, review
 * and print reports on products.
 * 
 * @author PascaL
 * @version 8.0 <br>
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
 *          7.0 : create missing directories if needed<br>
 *          print product report to files<br>
 *          modify parseReview and parseProduct to be prepared for bulk-load<br>
 *          add private loadReviews, loadProduct and loadAllData methods<br>
 *          update constructor to trigger the bulk load of all products<br> 
 *          modify parseProduct, parseReviews from public to private<br>
 *          rename getProduct to createProduct<br>
 *          add dumpData method to dump products data to file<br>
 *          8.0 : remove languageTag as parameter of constructor <br>
 *          made constructor private to implement singleton<br>
 *          remove the formatter as parameter <br>
 *          remove the changeLocale method <br>
 *          adapt createProduct, reviewProduct methods to use Reentrant W Locking<br>
 *          adapt findProduct, getDiscounts methods to use Reentrant R Locking<br>
 *          adapt printProductReport methods to use Reentrant R Locking and client<br>
 *          adapt printProducts methods to use Reentrant R<br>
 *          <br>
 */
public class ProductManager {

	private static final Logger logger = Logger.getLogger(practice14.data.ProductManager.class.getName());
	private static final String CONFIG_PATH = "practice14.data.config";
	private static final ProductManager INSTANCE = new ProductManager();

	private final ResourceBundle configBundle = ResourceBundle.getBundle(CONFIG_PATH);
	private final MessageFormat productFormat = new MessageFormat(configBundle.getString("product.data.format"));
	private final MessageFormat reviewFormat = new MessageFormat(configBundle.getString("review.data.format"));

	private final Path currentWorkingDirectory;
	private final Path reportsFolder; 
	private final Path dataFolders; 
	private final Path tempFolder; 

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock(); // read (find, get products from map)
	private final Lock writeLock = lock.writeLock(); // write (put, add, remove, change products into products map)
	
	private Map<Product, List<Review>> products; // mutable, no choice
	
	// private ResourceFormatter formatter; // v8.0 : formatter can not be unique anymore, will depend on caller language

	
	/**
	 * Resource formatters per Locale
	 * https://www.localeplanet.com/java/fr-BE/index.html
	 * https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html
	 */
	private static Map<String, ResourceFormatter> formatters = 
			Map.of("en-GB", new ResourceFormatter(Locale.UK),
			       "fr-FR", new ResourceFormatter(Locale.FRANCE), 
			       "fr-BE", new ResourceFormatter(Locale.forLanguageTag("fr-BE")),
			       "ja-JP", new ResourceFormatter(Locale.JAPAN)                     );


	
//  // removed from v8.0
//	/**
//	 * ProductManager constructor
//	 * 
//	 * @param locale
//	 */
//	public ProductManager(Locale locale) {
//		this(locale.toLanguageTag());
//	}

	
	/**
	 * Get ProductManager instance (singleton)
	 * @return ProductManager
	 */
	public static ProductManager getInstance() {
		return INSTANCE;
	}
	
	
	/**
	 * ProductManager constructor (private - singleton)
	 * 
	 * @param languageTag
	 */
	// public ProductManager(final String languageTag) { // removed from v8.0
	private ProductManager() {
		// changeLocale(languageTag); // removed from v8.0

		//Current wooking directory (cwd) 
		this.currentWorkingDirectory = Path.of("").toAbsolutePath(); // FileSystems.getDefault().getPath("").toAbsolutePath().toString();
		System.out.println("Current working directory: " + this.currentWorkingDirectory);
		
		// https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
//		System.out.println("Current working directory - root: " + cwd.getRoot());
//		System.out.println("Current working directory - parent: " + cwd.getParent());
//		System.out.println("Current working directory - filename: " + cwd.getFileName());

//		FileSystem fs = cwd.getFileSystem();
//		Iterable<FileStore> ite = fs.getFileStores();
//		ite.forEach((p) -> System.out.println("File stores are: " + p));
		
		this.reportsFolder = Path.of(configBundle.getString("reports.folder"));
		this.dataFolders = Path.of(configBundle.getString("data.folder"));
		this.tempFolder = Path.of(configBundle.getString("temp.folder"));

		createDirectory(this.currentWorkingDirectory, this.reportsFolder);
		createDirectory(this.currentWorkingDirectory, this.dataFolders);
		createDirectory(this.currentWorkingDirectory, this.tempFolder);

		this.products = loadAllData(); //new HashMap<>();
	}

	/**
	 * Create directory if not exists<br>
	 * Also reassign the relativeDir to a resolved path with baseDir!
	 * @param baseDir
	 * @param ralativeDir
	 */
	private void createDirectory(final Path baseDir, Path relativeDir) {
		Path newPath = baseDir.resolve(relativeDir).normalize();
		if (Files.notExists(newPath)) {
			try {
				newPath = Files.createDirectory(newPath);
				System.out.println(newPath + " has been created.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// relativeDir = newPath;
	}

	
// // removed from v8.0
//	/**
//	 * Change locale along with resource formatter
//	 * 
//	 * @param langageTag
//	 */
//	public void changeLocale(final String languageTag) {
//		ResourceFormatter formatter = setFormatter(languageTag);
//		System.out.println("Locale is set on " + formatter.getLocale() + " "
//				+ (formatter.getLocale().toLanguageTag().equals("en-GB") ? "(default)" : ""));
//	}

	
	/**
	 * Configure and retrieve the resource formatter
	 * 
	 * @param languageTag
	 * @return ResourceFormatter
	 */
	public ResourceFormatter getFormatter(final String languageTag) {
		ResourceFormatter defaultFormatter = formatters.get("en-GB");
		return formatters.getOrDefault(languageTag, defaultFormatter);
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
	 * Create a food product with a default rating and a default best-before date
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @return Product food
	 */
	public Product createProduct(int id, String name, BigDecimal price) {
		Product food = null;
		writeLock.lock();
		try { 
			food = new Food(id, name, price);
		    products.putIfAbsent(food, new ArrayList<Review>());
		    createFileProduct('F', food);
		} finally {
			writeLock.unlock();
		}    
		return food;
	}

	/**
	 * Create a food product
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @param bestBefore
	 * @return Product food
	 */
	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		Product food = null;
		writeLock.lock();
		try { 
			food = new Food(id, name, price, rating, bestBefore);
		    products.putIfAbsent(food, new ArrayList<Review>());
		    createFileProduct('F', food);
		} finally {
			writeLock.unlock();
		}    
		return food;
	}

	/**
	 * Create a drink product
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @return Product product
	 */
	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		Product drink = null;
		writeLock.lock();
		try {
			drink = new Drink(id, name, price, rating);
			products.putIfAbsent(drink, new ArrayList<Review>());
			createFileProduct('D', drink);
		} finally {
			writeLock.unlock();
		}  
		return drink;
	}

	/**
     * Create a product file for retention and keep tracking. 
	 */
	private void createFileProduct(char type, Product product) {
		
		String data = MessageFormat.format(this.configBundle.getString("product.data.format"), type, product.getId(),
				product.getName(), product.getPrice().toString(), product.getRating().getRank(), product.getBestBefore());

		Path file = this.dataFolders.resolve(MessageFormat.format(this.configBundle.getString("product.data.file"), product.getId()));
		
		try {
			StandardOpenOption soo = StandardOpenOption.CREATE;
			if (Files.exists(file)) {
				soo = StandardOpenOption.WRITE; // (OVER)WRITE
			} 
			Files.writeString(file, data, soo); // CREATE and WRITE
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while creating file='" + file + "' for " + product, e);
		}
	}
	
	/**
	 * Find a product by his Id (via Predicate & Stream)
	 * 
	 * @param id
	 * @return Product
	 * @throws ProductManagerException
	 */
	public Product findProduct(int id) throws ProductManagerException {
		final Predicate<Product> condition = p -> p.getId() == id;
		readLock.lock();
		try {
			return this.products.keySet().stream().filter(condition)
    				.findFirst()
		            .orElseThrow(() -> new ProductManagerException("Product with Id=" + id + " is not found ! \n"));
		} finally {
			readLock.unlock();
		}
		
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
	private Rateable<Product> reviewProduct(Rateable<Product> product, Rating rating, String review) {
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
	 * @return Rateable<Product> product with new rating, or <null> if not found !
	 * @throws ProductManagerException
	 */
	public Rateable<Product> reviewProduct(int productId, Rating rating, String review) {
		writeLock.lock();
		try {
			return reviewProduct(findProduct(productId), rating, review);
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
		} finally {
			writeLock.unlock();
		}
		return null;
	}

	/**
	 * Print a product report
	 * 
	 * @param productId
	 * @throws ProductManagerException
	 */
	public void printProductReport(int productId, final String languageTag, final String client) {
		readLock.lock();
		try {
			printProductReport(findProduct(productId), languageTag, client);
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, "Error printing product report -" + ioe.getMessage(), ioe);
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * Print product report (using stream instead of for... loop)
	 * 
	 * @param product
	 * @throws IOException
	 */
	private void printProductReport(Rateable<Product> product, final String languageTag, final String client) throws IOException {
		
		if (product == null) {
			System.err.println("WARNING printProductReport called with a <null> product, languageTag=" + languageTag + ", client=" + client);
		} 
		
		ResourceFormatter formatter = getFormatter(languageTag);
		
		// Retrieve list of reviews
		List<Review> reviews = products.get(product);
		Collections.sort(reviews); // sorted by ...

		// Header (title)
		StringBuilder txt = new StringBuilder(formatter.getResourceText("title"));
		
		// Report-A) product = {0}, Price: {1}, Rating: {2}, Best Before: {3}
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
		// Output the report text (optional)
		System.out.println("Output report text : " + txt);

		// Output to report file
		Path rp = this.reportsFolder
				.resolve(MessageFormat.format(configBundle.getString("report.file"), ((Product) product).getId(), client));
		System.out.println("Output report file : " + rp);
		try {
			Files.writeString(rp, txt, StandardOpenOption.CREATE); // CREATE_NEW =>
																	// java.nio.file.FileAlreadyExistsException:
																	// .\reports\product_101_report.txt
		} catch (FileAlreadyExistsException faee) {
			Files.writeString(rp, txt, StandardOpenOption.APPEND); // APPEND
		}

	}

	/**
	 * Print report on products based on comparison (using stream)<br>
	 * See https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
	 * 
	 * @param comparator
	 */
	public void printProducts(Comparator<Product> comparator, final String languageTag) {
		readLock.lock();
		try {
			ResourceFormatter formatter = getFormatter(languageTag);
			StringBuilder txt = new StringBuilder(formatter.getResourceText("report.products"));
			AtomicInteger counter = new AtomicInteger(1);
			this.products.keySet().stream().sorted(comparator).forEach((p) -> {
				txt.append("[");
				txt.append(counter.getAndIncrement()).append("] ");
				txt.append(formatter.formatProduct(p));
				txt.append('\n');
			});
			System.out.println(txt);
		} finally {
			readLock.unlock();
		}
	}
		

	/**
	 * Print report on products based on comparison and a filter (using stream)<br>
	 * See https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Predicate.html
	 * 
	 * @param comparator
	 * @param filter
	 */
	public void printProducts(Comparator<Product> comparator, Predicate<Product> filter, final String languageTag) {
		readLock.lock();
		try {
			ResourceFormatter formatter = getFormatter(languageTag);
			StringBuilder txt = new StringBuilder(formatter.getResourceText("report.products.filtered"));
			AtomicInteger counter = new AtomicInteger(1);
			txt.append(this.products.keySet().stream().sorted(comparator).filter(filter)
					.map(p -> "[" + counter.getAndIncrement() + "] " + formatter.formatProduct(p))
					.collect(Collectors.joining("\n"))).append('\n');
			System.out.println(txt);
		} finally {
			readLock.unlock();
		}
	}


	/**
	 * Print a summary of products<br> 
	 * - number of products
	 * - list of product types
	 * - number of products per type
	 * - etc ...
	 */
	public void printSummaryProducts() {
		
		System.out.println("Summary of products:\n * Nbr. of products : " + this.products.size());
		
		Set<String> set = products.keySet().stream().map(p -> p.getClass().getSimpleName()).collect(Collectors.toSet());
		System.out.println(" * List of types : " + set );
		
		Map<String, String> map = products.keySet().stream()
				                          .collect(Collectors.groupingBy(p -> p.getClass().getSimpleName().toString(),
						                           Collectors.collectingAndThen(Collectors.counting() , c -> String.valueOf(c) )
				));
		map.forEach( (k,v) -> System.out.format(" * Nbr. of %s : %s\n", k, v) );
	}
	
	
	/**
	 * Get discounts - calculate a total of all discount values for each group of
	 * products that have the same rating.
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getDiscounts(final String languageTag) {
		readLock.lock();
		try {
			ResourceFormatter formatter = getFormatter(languageTag);
			return products.keySet().stream()
					.collect(Collectors.groupingBy(p -> p.getRating().getStars(),
							Collectors.collectingAndThen(Collectors.summingDouble(p -> p.getPriceDiscount().doubleValue()), // downstream
									d -> formatter.priceFormat.format(d)) // finisher
					));
		} finally {
			readLock.unlock();
		}
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
	 * @param inputText
	 * @return Review
	 * @throws ProductManagerException
	 */
	private Review parseReview(final String inputText) {
		Review review = null;
		int productId = 0; 
		try {
			Object[] values = reviewFormat.parse(inputText);
			// Product Id
			productId = Integer.valueOf((String) values[0]);
			// Rating value and check if in range
			int rating = Integer.valueOf((String) values[1]);
			checkRating(rating);
			// reviewProduct(productId, Rating.valueOfStars(rating), (String) values[2]);
			review = new Review(Rating.valueOfStars(rating), (String) values[2]);
		} catch (ParseException | NumberFormatException | ProductManagerException ex) {
			logger.log(Level.WARNING, "Error while parsing review for productId=" + productId+ " - '" + inputText + "' - aborted"); // ... , ex);
		}

		return review;
	}

	/**
	 * Create a product by parsing the input String
	 * 
	 * @param inputText - (comma as separator - see config.properties)
	 * @return Product
	 * @throws ProductManagerException
	 */
	private Product parseProduct(final String inputText) throws ProductManagerException {
		Product product = null;
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
			case "D":
				// getProduct(productId, name, price, stars); // create Drink
				product = new Drink(productId, name, price, stars);
				break;
			case "F":
				String aDate = (String) values[5];
				LocalDate date = LocalDate.parse(aDate);
				// getProduct(productId, name, price, stars, date); // create Food
				product = new Food(productId, name, price, stars, date);
				break;
			default:
				throw new ProductManagerException(
						"Invalid product type (must be either 'D' for Drink, or 'F' for Food)");
			}

		} catch (ParseException | NumberFormatException | DateTimeParseException ex) {
			logger.log(Level.WARNING, "Error while parsing product for '" + inputText + "' - aborted", ex);
		}
		return product;
	}

	/**
	 * Load reviews of a product by reading an input CSV file found in data folder.<br>
	 * The Id of the product is used to find the file.<br>
	 * See review filename pattern in 'config' file.
	 * 
	 * @param product
	 * @return List<Review> list of product's reviews
	 */
	private List<Review> loadReviews(Product product) {
		List<Review> reviews = null;
		Path file = this.dataFolders
				.resolve(MessageFormat.format(configBundle.getString("review.data.file"), product.getId()));
		if (Files.notExists(file)) {
			reviews = new ArrayList<Review>();
		} else {
			try {
				reviews = Files.lines(file, Charset.forName("UTF-8"))
				               .map(line -> parseReview(line)) // parse each review line
						       .filter(review -> review != null) // skip null review object
						       .collect(Collectors.toList()); // build a mutable list of reviews
			} catch (IOException ex) {
				logger.log(Level.WARNING, "Error while loading bulk reviews for " + product + " -> " + ex.getMessage());
			} 
		}
		return reviews;
	}

	/**
	 * Load product from input CSV file found in data folder.<br>
	 * See product filename pattern in in 'config' file.<br>
	 * @param file
	 * @return Product product
	 */
	private Product loadProduct(Path file) {
		Product product = null;
		try {
			product = parseProduct(Files.lines(file, Charset.forName("UTF-8"))
					.findFirst() // only the first line is expected
					.orElseThrow() // used to throw NoSuchElementException if no value is present
			);
		} catch (ProductManagerException | IOException ex) {
			logger.log(Level.SEVERE, "Error while loading product from " + file + " -> " + ex.getMessage(), ex);
		}
		return product;
	}

	
	/**
	 * Dump all data products into a temporary file<br>
	 */
	public void dumpData() {
		// Check - if tmp file is already present, skip and avoid to do a dump...
		Path tempFile = null;
		try {
			tempFile = Files.list(this.tempFolder)
						         .filter(file -> file.getFileName().toString().endsWith(".tmp"))
						         .findFirst()
						         .orElseGet(() -> null);
		} catch (IOException ioex) {
			logger.log(Level.SEVERE, "Error while verifying presence of temorary remaining dump file into " + tempFile + " -> " + ioex.getMessage(), ioex);
		} 
		// Warn we can only handle one temp file at a time
		if (tempFile != null) {
			logger.log(Level.WARNING, "A dumping file with all products data is present into " + tempFile + " - dump aborted." );
			logger.log(Level.WARNING, "Please do a restore first or erase this file manually...");
			return;
		}
		// Ok, we can proceed with the full dump
		final long NOW = Instant.now().toEpochMilli();
		tempFile = this.tempFolder.resolve(MessageFormat.format(this.configBundle.getString("temp.file"), String.valueOf(NOW)));
		try( ObjectOutputStream objOS = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE_NEW)) ) {
			objOS.writeObject(this.products);
			this.products = new HashMap<Product, List<Review>>(); // reset, clean, reinitialization !  
			logger.log(Level.INFO, "Dumping all products data is done and available into " + tempFile );
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error while dumping all products data to " + tempFile + " -> " + ex.getMessage(), ex);
		}
	}
	
	
	/**
	 * Restore all data products from temporary file<br>
	 */
	@SuppressWarnings("unchecked")
	public void restoreData() {
		Path tempFile = Path.of("temp_file_not_found.tmp");
				
		try {
			 tempFile = Files.list(this.tempFolder)
					         .filter(file -> file.getFileName().toString().endsWith(".tmp"))
					         .findFirst() // assume only one temp file
					         .orElseThrow();
			 
			 try( ObjectInputStream objIS = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE)) ) { // READ + DELETE_ON_CLOSE
					this.products = (Map<Product, List<Review>>) objIS.readObject();
					logger.log(Level.INFO, "Restoring all products data is done and delete " + tempFile );
			 }
			 
		} catch (IOException | ClassNotFoundException ex) {
			logger.log(Level.SEVERE, "Error while restoring all products data from '" + tempFile + "' -> " + ex.getMessage(), ex);
			
		} catch (NoSuchElementException nseex) {
			logger.log(Level.SEVERE, "Error while trying to restore all products data (temporary file not present) -> " + nseex.getMessage() + " - tempFile='" + tempFile + "'");
		}
	}
	
	
	/**
	 * Load all data products and associated reviews (bulk loading)
	 */
	private Map<Product, List<Review>> loadAllData() {
		Map<Product, List<Review>> map = null;
		try {
			map = Files.list(this.dataFolders) // list files from data folder
					   .filter(file -> file.getFileName().toString().startsWith("product_")) // filtering on product files only 
					   .map(file -> loadProduct(file)) // create product 
					   .filter( product -> product != null ) // skip null product
					   .collect(Collectors.toMap( product -> product , product -> loadReviews(product) )); // build product's reviews and populate Map<K,V>
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Error while bulk loading all products from " + this.dataFolders + " -> " + ex.getMessage());
		}
		
		logger.log(Level.INFO, "We are currently managing a list of " + map.size() + " products."); 

		return map; 
	}
	
	/**
	 * Get set of supported Locale
	 * @return Set<String>
	 */
	public static Set<String> getSupportedLocales() {
		return formatters.keySet();
	}
	
	/**
	 * ResourceFormatter (static nested class)
	 * 
	 * @author PascaL
	 */
	private static class ResourceFormatter {

		private static final String RESOURCES_PATH = "practice14.data.resources";

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

//		Locale getLocale() {
//			return locale;
//		}
	}

}
