package practice6.app;

import java.math.BigDecimal;
import java.time.LocalDate;

import practice6.data.ProductManager;
import practice6.data.Drink;
import practice6.data.Food;
import practice6.data.Product;
import practice6.data.Rating;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 1.0
 * 
 */
public class Shop {

	public static void main(String[] args) {
		System.out.println("--- Practice6 -- ");
		
		// Product p1 = new Drink(101, "White Tea", BigDecimal.valueOf(10.99), Rating.FIVE_STAR);
		Product p1 = ProductManager.getProduct(101, "White Tea", BigDecimal.valueOf(10.99), Rating.FIVE_STAR);
		System.out.println("p1: " + p1);
				
		Product p2 = new Drink();
		p2.setId(102);
		p2.setName("Black Tea");
		p2.setPrice(BigDecimal.valueOf(7.65));
		p2.setRating(Rating.THREE_STAR);
		System.out.println("p2: " +p2);

		Product p3 = ProductManager.getProduct(103, "Green Apple", BigDecimal.valueOf(9.053));
		System.out.println("p3: " +p3);
		
		Product p4 = p3.applyRating(Rating.TWO_STAR); // return a new Product of same type of p3 !
		System.out.println("p4: " +p4);
		System.out.println("p3: " +p3); // p3 remains at NOT_RATED - OK
		
		//p3 = p3.applyRating(Rating.THREE_STAR);
		// System.out.println("p3: " +p3); // p3 change to object reference in heap - OK
		
		Product p6 = ProductManager.getProduct(104, "Chocolate",  BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		Product p7 = ProductManager.getProduct(104, "Chocolate",  BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
		Product p8 = ProductManager.getProduct(104, "Chocolate",  BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
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
		System.out.println("p1: " + p1.getClass().getSimpleName() + " bestBefore date is " + p1.getBestBefore());
		System.out.println("p2: " + p2.getClass().getSimpleName() + " bestBefore date is " + p2.getBestBefore());
		System.out.println("p3: " + p3.getClass().getSimpleName() + " bestBefore date is " + p3.getBestBefore());	
		System.out.println("p4: " + p4.getClass().getSimpleName() + " bestBefore date is " + p4.getBestBefore());
		

	}

}

