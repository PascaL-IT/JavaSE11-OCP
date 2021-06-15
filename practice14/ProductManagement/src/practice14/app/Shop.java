package practice14.app;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import practice14.data.Product;
import practice14.data.ProductManager;
import practice14.data.Rating;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 14.0
 * 
 */
public class Shop {
	
	public static final int NBR_PRODUCTS_MAX_VALUE = 6; // e.g. 6 products (see csv files)
	public static final int NBR_CLIENTS_MAX_VALUE = 5; // e.g. 5 random clients
	public static final int NBR_THREAD_MAX_VALUE = 3; // e.g. a pool of 3 threads

	public static void main(String[] args) {
		
		System.out.println("--- practice14 -- ");
		System.out.println("Supported locale: " + ProductManager.supportedLocale()); // fr-BE, en-GB, fr-FR

		Locale defaultLocale = Locale.getDefault();
		System.out.println("Default locale: " + defaultLocale + " -> " + defaultLocale.toLanguageTag());

		if (!ProductManager.supportedLocale().contains(defaultLocale.toLanguageTag())) {
			System.err.println("Locale is not supported");
			System.exit(1);
		}

		ProductManager pm = ProductManager.getInstance(); // Manager to ease creation of products
				
		AtomicInteger clientCount = new AtomicInteger(0);
		
		
		// Client functional interface (i.e. a Client task that returns a result (or an exception)
		Callable<String> client = () -> {
			String threadName = Thread.currentThread().getName();
			String clientId = "Client" + clientCount.addAndGet(1);
			int randomProductId = ThreadLocalRandom.current().nextInt(1, NBR_PRODUCTS_MAX_VALUE);
			Set<String> locales = ProductManager.getSupportedLocales();
			int randomLocaleNbr = ThreadLocalRandom.current().nextInt(locales.size());
			String randomlangageTag = locales.stream()
					                   .skip(randomLocaleNbr) // number of leading elements to skip
					                   .findFirst().get();
			StringBuilder log = new StringBuilder();
			log.append(clientId).append("|").append(threadName).append("|").append(randomlangageTag).append("|");
			log.append(pm.getDiscounts(randomlangageTag).entrySet().stream().map(e -> e.getKey() + "_" + e.getValue())
					.collect(Collectors.joining("|")));
			int randomRatingNbr = ThreadLocalRandom.current().nextInt(Rating.values().length);
			String review = "a review of productId=" + randomProductId + " with rating=" + Rating.valueOfStars(randomRatingNbr).getStars(); 
			Product product = (Product) pm.reviewProduct(randomProductId, Rating.valueOfStars(randomRatingNbr), review);
			log.append( (product != null) ? "Product " + randomProductId + " reviewed" : "Product " + randomProductId + " not found");
			pm.printProductReport(randomProductId, randomlangageTag, clientId);
			log.append(clientId + " generated a report for productId=" + randomProductId);
						
			return log.toString(); 
		}; 
		
		// Build a list of 5 random clients
		List<Callable<String>> clients = Stream.generate(() -> client).limit(NBR_CLIENTS_MAX_VALUE).collect(Collectors.toList());

		// Build an executor with only 3 threads to force the scheduler to time-share the 5 clients
		ExecutorService executor = Executors.newFixedThreadPool(NBR_THREAD_MAX_VALUE); 
		
		// Invoke all clients tasks
		try {
			List<Future<String>> results = executor.invokeAll(clients);
			executor.shutdown();
			// Print out each future result
			results.stream().forEach(f -> {
				try {
					System.out.println(f.get());
				} catch (InterruptedException | ExecutionException iee) {
					Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, iee.getMessage(), iee);
				} 
			});
			
		} catch (InterruptedException ex) {
			Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		
		
//		pm.printSummaryProducts();
//		String client = "A";
//		
//		//Test bulk load by printing reports
//		pm.printProductReport(1, "fr-BE", client);
//		pm.printProductReport(2, "fr-BE", client);
//		pm.printProductReport(3, "fr-BE", client);
//		
//		//Test print products report (sorted by price)
//		pm.printProducts( (p1, p2) -> ((int) p1.getPrice().floatValue() - (int) p2.getPrice().floatValue() ) , "fr-BE");
//		//Test print products report (sorted by id)
//		pm.printProducts( (p1, p2) -> (p1.getId() - p2.getId()) , "fr-BE");
//
//		//Create and add a new product
//		pm.createProduct(4, "Coca-Cola", BigDecimal.valueOf(5.65), Rating.FIVE_STAR); 
//		pm.printProductReport(4, "fr-BE", client);
//		
//		pm.createProduct(5, "Hamburger", BigDecimal.valueOf(8.51), Rating.FOUR_STAR, LocalDate.now()); 
//		pm.printProductReport(5, "fr-BE", client);
//		
//		pm.createProduct(6, "Kefta", BigDecimal.valueOf(14.36), Rating.THREE_STAR, LocalDate.now()); 
//		pm.printProductReport(6, "fr-BE", client);
//		
//		//Display discount per rating 
//		pm.getDiscounts("fr-BE").forEach( (r,d) -> System.out.format("rating=%s <-> discount=%s\n",r,d) );
//		
//		//Test dump & restore of full data products
//		pm.dumpData();
//		pm.restoreData();
//		pm.printSummaryProducts();
//		// System.exit(0);
	}
}