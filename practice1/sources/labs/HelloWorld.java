package labs;

public class HelloWorld {
	
	public static void main(String[] args) {
		String version = System.getProperty("java.version");
		System.out.println("Hello " + args[0] + "  on Java version " + version);
	}
	
}
