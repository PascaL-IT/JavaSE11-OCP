package practice4.app;

import java.math.BigDecimal;

import practice4.data.Product;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 1.0
 * 
 */
public class Shop {

	public static void main(String[] args) {

		Product p1 = new Product();
		p1.setId(101);
		p1.setName("Tea"); // add/remove comment with Ctrl+7
		p1.setPrice(BigDecimal.valueOf(0.0)); // 1.99
		System.out.println(p1.getId() + " - " + p1.getName() + " - " + p1.getPrice() + " - " + p1.getPriceDiscount());

	}

}
