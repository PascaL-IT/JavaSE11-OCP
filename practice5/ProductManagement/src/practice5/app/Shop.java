package practice5.app;

import java.math.BigDecimal;

import practice5.data.Product;
import practice5.data.Rating;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 1.0
 * 
 */
public class Shop {

	public static void main(String[] args) {
		System.out.println("--- Practice 5 ---"); 
		
		Product p1 = new Product(101, "White Tea", BigDecimal.valueOf(10.99), Rating.FIVE_STAR);
		System.out.println("p1: " + p1);
				
		Product p2 = new Product();
		p2.setId(102);
		p2.setName("Black Tea");
		p2.setPrice(BigDecimal.valueOf(7.65));
		p2.setRating(Rating.THREE_STAR);
		System.out.println("p2: " +p2);

		Product p3 = new Product(103, "Green Tea", BigDecimal.valueOf(9.053));
		System.out.println("p3: " +p3);
		
		Product p4 = p3.applyRating(Rating.TWO_STAR);
		System.out.println("p4: " +p4);
		System.out.println("p3: " +p3); // p3 remains at NOT_RATED - OK
		
		p3 = p3.applyRating(Rating.THREE_STAR);
		System.out.println("p3: " +p3); // p3 change to object reference in heap - OK
		
	}

}
