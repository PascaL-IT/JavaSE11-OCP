package practice6.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductManager {

	public static Food getProduct(int id, String name, BigDecimal price) {
		return new Food(id, name, price);
	}
	
	public static Food getProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		return new Food(id, name, price, rating, bestBefore);
	}

	public static Drink getProduct(int id, String name, BigDecimal price, Rating rating) {
		return new Drink( id,  name,  price,  rating);
	}
		
}
