package practice7;

public interface X {
	
	void a();
	
	public default void b() { 
		System.out.println("X: default b() callin c()");
		c();
	};
	
	private void c() {
		System.out.println("X: private c()");
	};
	
	public static void d() {
		System.out.println("X: static d()");
	}
	
}