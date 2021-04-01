package practice7;

public class Z extends W implements X, Y {
	
	@Override
	public void a() {
		System.out.println("Z: public a()");
	}

	@Override
	public void b() { 
		System.out.println("Z: public b() - fix conflict");
	}

	
//	
//	private void c() {
//		System.out.println("private c()");
//	};
//	
//	public static void d() {
//		System.out.println("static d()");
//	}
	
	@Override
	public String toString() {
		return "Z: class Z extends W implements X, Y where Y is parent of YC";
	}

	@Override
	public void yc() {
		System.out.println("Z: public yc() - Override as from Y interface parent of YC interface");
	};	
	
}