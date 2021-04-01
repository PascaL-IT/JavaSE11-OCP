package practice7;

public interface YC extends Y {

	void yc();
	
	default void yc_d() {
		System.out.println("YC: default yc()");
	}; 
	
}
