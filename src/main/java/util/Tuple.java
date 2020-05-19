package util;

/**
 * @author Tim Pollandt
 * 
 *         Tuple (set of two elements of generic types)
 *
 */
public class Tuple<A, B> {

	/**
	 * First object in tuple.
	 */
	private A first;

	/**
	 * Second object in tuple.
	 */
	private B second;

	/**
	 * Constructor.
	 */
	public Tuple(A a, B b) {
		this.first = a;
		this.second = b;
	}

	/**
	 * @return first element (data type A)
	 */
	public A first() {
		return first;
	}

	/**
	 * @return second element (data type B)
	 */
	public B second() {
		return second;
	}

	/**
	 * @return set first element (data type A)
	 */
	public void setFirst(A a) {
		first = a;
	}

	/**
	 * @return set second element (data type B)
	 */
	public void setSecond(B b) {
		second = b;
	}

	@Override
	public String toString() {
		return "Tuple [first=" + first + ", second=" + second + "]";
	}

}
