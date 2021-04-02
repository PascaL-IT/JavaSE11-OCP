package practice8.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import practice8.data.Food;
import practice8.data.Product;
import practice8.data.ProductManager;
import practice8.data.Rateable;
import practice8.data.Rating;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 8.0
 * 
 */
public class Shop {

	public static void main(String[] args) {
		System.out.println("--- practice8 -- ");
		Locale defaultLocale = Locale.getDefault();
		ProductManager pm = new ProductManager(defaultLocale); // Manager to ease creation of products
		
		// Product p1 = new Drink(101, "White Tea", BigDecimal.valueOf(10.99), Rating.FIVE_STAR);
		Rateable<Product> p1 = pm.getProduct(101, "White Tea", BigDecimal.valueOf(10.99), Rating.FIVE_STAR);
		System.out.println("p1: " + p1);
				
		Rateable<Product> p2 = pm.getProduct(102, "Black Tea", BigDecimal.valueOf(7.65), Rating.THREE_STAR);
		System.out.println("p2: " +p2);

		Product p3 = pm.getProduct(103, "Green Apple", BigDecimal.valueOf(9.053));
		System.out.println("p3: " +p3);
		
		Product p4 = p3.applyRating(Rating.TWO_STAR); // return a new Product of same type as p3 with new rating
		System.out.println("p4: " +p4);
		System.out.println("p3: " +p3); // p3 remains at NOT_RATED - OK
		
		//p3 = p3.applyRating(Rating.THREE_STAR);
		// System.out.println("p3: " +p3); // p3 change to object reference in heap - OK
		
		Rateable<Product> p6 = pm.getProduct(104, "Chocolate",  BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		Product p7 = pm.getProduct(104, "Chocolate",  BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
		Rateable<Product> p8 = pm.getProduct(104, "Chocolate",  BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		System.out.println("p6: " +p6);
		System.out.println("p7: " +p7);
		System.out.println("p8: " +p8);
		System.out.println("Are p6 and p7 same objects? " + (p6.equals(p7))); // answer is false
		System.out.println("Are p6 and p8 same objects? " + (p6.equals(p8))); // answer is true
		
		Product p9 = p8.applyRating(Rating.ONE_STAR);
		System.out.println("p9: " + p9);
		
		//Check  p4 is a Product of type Food before casting it to get his bestBefore date
		if (p4 instanceof Food) {
			System.out.println("p4: " + p4.getClass() + " bestBefore date is " + ((Food) p4).getBestBefore());
		}
		
		//With the getBestBefore introduces at Product level, we don't need to cast or verify with instanceof
		System.out.println("p1: " + p1.getClass().getSimpleName() + " bestBefore date is " + ((Product) p1).getBestBefore());
		System.out.println("p2: " + p2.getClass().getSimpleName() + " bestBefore date is " + ((Product) p2).getBestBefore());
		System.out.println("p3: " + p3.getClass().getSimpleName() + " bestBefore date is " + p3.getBestBefore());	
		System.out.println("p4: " + p4.getClass().getSimpleName() + " bestBefore date is " + p4.getBestBefore());
		
		//Test on Rating (enum)
		Rating r0 = Rating.getByName("NOT_RATED"); 
		System.out.println(r0);
		Rating r5 = Rating.valueOfStars(5);
		System.out.println(r5);
		
		//Test new interface Rateable
		Product p10 = p4.applyRating(5);
		System.out.println(p4);
		System.out.println(p10);
		Product p11 = p10.applyRating(Rating.valueOf("THREE_STAR"));
		System.out.println(p11);
		
		//Test multiple reviews on a product and print product report before and after 
		System.out.println(p1);
		pm.prindProductReport();
		
		System.out.println(p1);
		pm.reviewProduct(p1, Rating.ONE_STAR, "This tea is not so good (1)");
		pm.reviewProduct(p1, Rating.TWO_STAR, "This tea is standard (2)");
		pm.reviewProduct(p1, Rating.THREE_STAR, "This tea is good (3)");
		pm.reviewProduct(p1, Rating.FOUR_STAR, "This tea is very good (4)");
		pm.reviewProduct(p1, Rating.FIVE_STAR, "This tea is the best (5)");
		pm.prindProductReport();
	}

}

