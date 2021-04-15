package practice11.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import practice11.data.Food;
import practice11.data.Product;
import practice11.data.ProductManager;
import practice11.data.Rateable;
import practice11.data.Rating;
import practice11.data.Review;

/**
 * {@code Shop} class represents an application that manages products.
 * 
 * @author PascaL
 * @version 11.0
 * 
 */
public class Shop {

	public static void main(String[] args) {
		System.out.println("--- practice11 -- ");

		System.out.println("Supported locale: " + ProductManager.supportedLocale());

		Locale defaultLocale = Locale.getDefault();
		System.out.println("Default locale: " + defaultLocale + " -> " + defaultLocale.toLanguageTag());

		if (!ProductManager.supportedLocale().contains(defaultLocale.toLanguageTag())) {
			System.err.println("Locale is not supported");
			System.exit(1);
		}

		ProductManager pm = new ProductManager(defaultLocale); // Manager to ease creation of products
		// pm.changeLocale("en-US");
		pm.changeLocale("fr-BE");

		// Product p1 = new Drink(101, "White Tea", BigDecimal.valueOf(10.99),
		// Rating.FIVE_STAR);
		Rateable<Product> p1 = pm.getProduct(101, "White Tea", BigDecimal.valueOf(10.99), Rating.FIVE_STAR);
		System.out.println("p1: " + p1);

		Rateable<Product> p2 = pm.getProduct(102, "Black Tea", BigDecimal.valueOf(7.65), Rating.THREE_STAR);
		System.out.println("p2: " + p2);

		Product p3 = pm.getProduct(103, "Green Apple", BigDecimal.valueOf(9.053));
		System.out.println("p3: " + p3);

		Product p4 = p3.applyRating(Rating.TWO_STAR); // return a new Product of same type as p3 with new rating
		System.out.println("p4: " + p4);
		System.out.println("p3: " + p3); // p3 remains at NOT_RATED - OK

		// p3 = p3.applyRating(Rating.THREE_STAR);
		// System.out.println("p3: " +p3); // p3 change to object reference in heap - OK

		Rateable<Product> p6 = pm.getProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		Product p7 = pm.getProduct(105, "Chocolate", BigDecimal.valueOf(3.00), Rating.FIVE_STAR,
				LocalDate.now().plusDays(2));
		Rateable<Product> p8 = pm.getProduct(106, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		System.out.println("p6: " + p6);
		System.out.println("p7: " + p7);
		System.out.println("p8: " + p8);
		System.out.println("Are p6 and p7 same objects? " + (p6.equals(p7))); // answer is false
		System.out.println("Are p6 and p8 same objects? " + (p6.equals(p8))); // answer is true

		Product p9 = p8.applyRating(Rating.ONE_STAR);
		System.out.println("p9: " + p9);

		// Check p4 is a Product of type Food before casting it to get his bestBefore
		// date
		if (p4 instanceof Food) {
			System.out.println("p4: " + p4.getClass() + " bestBefore date is " + ((Food) p4).getBestBefore());
		}

		// With the getBestBefore introduces at Product level, we don't need to cast or
		// verify with instanceof
		System.out.println(
				"p1: " + p1.getClass().getSimpleName() + " bestBefore date is " + ((Product) p1).getBestBefore());
		System.out.println(
				"p2: " + p2.getClass().getSimpleName() + " bestBefore date is " + ((Product) p2).getBestBefore());
		System.out.println("p3: " + p3.getClass().getSimpleName() + " bestBefore date is " + p3.getBestBefore());
		System.out.println("p4: " + p4.getClass().getSimpleName() + " bestBefore date is " + p4.getBestBefore());

		// Test on Rating (enum)
		Rating r0 = Rating.getByName("NOT_RATED");
		System.out.println(r0);
		Rating r5 = Rating.valueOfStars(5);
		System.out.println(r5);

		// Test new interface Rateable
		Product p10 = p4.applyRating(5);
		System.out.println("p4: " + p4);
		System.out.println("p10: " + p10);
		Product p11 = p10.applyRating(Rating.valueOf("THREE_STAR"));
		System.out.println("p11: " +p11);

		// Test multiple reviews on a product and print product report before and after
		System.out.println("p1: " + p1);
		pm.prindProductReport(p1);

		System.out.println(p1);
		p1 = pm.reviewProduct(p1, Rating.ONE_STAR, "This tea is not so good (1)");
		p1 = pm.reviewProduct(p1, Rating.TWO_STAR, "This tea is standard (2)");
		p1 = pm.reviewProduct(p1, Rating.THREE_STAR, "This tea is good (3)");
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "This tea is very good (4)");
		p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "This tea is the best (5)");
		pm.prindProductReport(p1);

		// Show all product and reviews
		System.out.println("\nNbr. of products: " + pm.getProducts().size());

		System.out.println("\nApproach n°1 with Entry :");
		for (final Entry<Product, List<Review>> entry : pm.getProducts().entrySet()) {
			System.out.println("K: " + entry.getKey() + "\n V: " + entry.getValue());
		}

		System.out.println("\nApproach n°2 with lambda & forEach :");
		pm.getProducts()
				.forEach((k, v) -> System.out.println("Key: " + k + "\n Reviews: " + v.size() + " - " + v.toString()));
		pm.getProducts().keySet().forEach((p) -> System.out.println("Product id=" + p.getId() + " -> " + p));

		// Ref.
		// https://blog.developpez.com/todaystip/p12259/dev/java/utilisez-map-entryset
		// https://stackoverflow.com/questions/46898/how-do-i-efficiently-iterate-over-each-entry-in-a-java-map

		// Test find product by id
		Product p12 = pm.findProduct(105);
		System.out.println("p12: " + p12);
		p12 = (Product) pm.reviewProduct(105, Rating.THREE_STAR, "Ok, give 3 stars");
		p12 = (Product) pm.reviewProduct(105, Rating.FOUR_STAR, "Ok, give 4 stars");
		System.out.println("p12: " + p12);
		pm.prindProductReport(105);

		// Test print products sorted by Id (anonymous inner class)
		System.out.println("** 1) List of products sorted by Id (desc)");
		pm.prindProducts(new Comparator<Product>() {
			@Override
			public int compare(Product p0, Product p1) {
				return p1.getId() - p0.getId();
			}
		});

		// Test print products sorted by Rating (lambda expression)
		System.out.println("** 2) List of products sorted by Rating (asc)");
		Comparator<Product> cp = (e1,e2) -> e1.getRating().getRank() - e2.getRating().getRank();
		pm.prindProducts(cp);

		
		// Test print products sorted by Name, Price and Id
		System.out.println("** 3) List of products sorted by Name (alpha), Price, Id");
		pm.prindProducts(new Comparator<Product>() {
			@Override
			public int compare(Product p0, Product p1) {
				int res = p0.getName().compareTo(p1.getName());
				if (res == 0) {
					res = p0.getPrice().compareTo(p1.getPrice());
					if (res == 0) {
						res = p0.getId() - p1.getId();
					}
				}
				return res;
			}
		});
		
		// Test print products sorted by Name, Price and Id
		System.out.println("** 4) List of products sorted by Name (alpha desc), Price desc, Id desc");
		Comparator<Product> cp1 = (pn0,pn1) -> pn1.getName().compareTo(pn0.getName());
		Comparator<Product> cp2 = (pn2,pn3) -> pn3.getPrice().compareTo(pn2.getPrice());
		Comparator<Product> cp3 = (pn4,pn5) -> pn5.getId() - pn4.getId();
		pm.prindProducts(cp1.thenComparing(cp2).thenComparing(cp3));

		
		// Test print products with a filter 
		System.out.println("** 5) List of products sorted by Name (alpha desc) and filtered by price > 5.00€ ");
		Comparator<Product> c5 = (pn0, pn1) -> pn1.getName().compareTo(pn0.getName());
		Predicate<Product> p5 = p -> p.getPrice().floatValue() > 5.00 ;
		pm.prindProducts(c5, p5);
		
		
		// Test get product discounts 
		Map<String, String> discounts = pm.getDiscounts();
		discounts.forEach((rating, discount) -> { System.out.format("%s %s \n", rating, discount); } ); 
		
		
	}

}
