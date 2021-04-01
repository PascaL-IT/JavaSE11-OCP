/** 
 * Test Interface against Quizz
 * <br>Covering abstract, extends, implements, hierarchy, polymorphism 
 */
package practice7;

/**
 * @author User
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Reminder: Z extends W implements X, Y where W is abstract and extends YC

		Z z = new Z();
		System.out.println(z);

		// Cast child Z to parent W and to X and Y interfaces
		W w = (W) z;
		X x = (X) z;
		Y y = (Y) z;

		System.out.println(w);
		w.b(); // polymorphism -> ok
		w.e(); // in parent W

		System.out.println(x);
		x.b(); // polymorphism -> ok
		x.a(); // polymorphism -> ok

		System.out.println(y);
		y.b();
		x.b(); // polymorphism -> ok
		y.e(); // in parent W

		System.out.println(x.getClass());
		System.out.println(y.getClass());
		System.out.println(w.getClass());
		System.out.println(z.getClass());

		X.d();
		Y.d();

		YC yc = (YC) w;
		yc.b();
		yc.e();
		yc.yc();
		yc.yc_d();

		if (yc instanceof YC) {
			System.out.println("yc instanceof YC : " + (YC) yc);
		}

		if (yc instanceof X) {
			System.out.println("yc instanceof X : " + (X) yc);
		}

		if (yc instanceof Z) {
			System.out.println("yc instanceof Z : " + (Z) yc);
		}

		if (yc instanceof W) {
			System.out.println("yc instanceof W : " + (W) yc);
		}

	}

}
