package practice13.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import practice13.data.ProductManager;
import practice13.data.Rating;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 13.0
 * 
 */
public class Shop {

	public static void main(String[] args) {
		System.out.println("--- practice13 -- ");

		System.out.println("Supported locale: " + ProductManager.supportedLocale());

		Locale defaultLocale = Locale.getDefault();
		System.out.println("Default locale: " + defaultLocale + " -> " + defaultLocale.toLanguageTag());

		if (!ProductManager.supportedLocale().contains(defaultLocale.toLanguageTag())) {
			System.err.println("Locale is not supported");
			System.exit(1);
		}


		ProductManager pm = new ProductManager(defaultLocale); // Manager to ease creation of products
		pm.printSummaryProducts();
		
		//Test bulk load by printing reports
		pm.printProductReport(1);
		pm.printProductReport(2);
		pm.printProductReport(3);
		
		//Test print products report (sorted by price)
		pm.printProducts( (p1, p2) -> ((int) p1.getPrice().floatValue() - (int) p2.getPrice().floatValue() ) );
		//Test print products report (sorted by id)
		pm.printProducts( (p1, p2) -> (p1.getId() - p2.getId()) );

		//Create and add a new product
		pm.createProduct(4, "Coca-Cola", BigDecimal.valueOf(5.65), Rating.FIVE_STAR); 
		pm.printProductReport(4);
		
		pm.createProduct(5, "Hamburger", BigDecimal.valueOf(8.51), Rating.FOUR_STAR, LocalDate.now()); 
		pm.printProductReport(5);
		
		pm.createProduct(6, "Kefta", BigDecimal.valueOf(14.36), Rating.THREE_STAR, LocalDate.now()); 
		pm.printProductReport(6);
		
		//Display discounts 
		pm.getDiscounts().forEach( (r,d) -> System.out.format("rating=%s <-> discount=%s\n",r,d) );
		
		//Test dump & restore of full data products
		pm.dumpData();
		pm.restoreData();
		pm.printSummaryProducts();
		// System.exit(0);
	}
}