package practice7;

public interface Y {
	
	void e();
	
	public default void b() { 
		System.out.println("Y: default b() calling c()");
		c();
	};
	
	private void c() {
		System.out.println("Y: private c()");
	};
	
	public static void d() {
		System.out.println("Y: static d()");
	}
	
}