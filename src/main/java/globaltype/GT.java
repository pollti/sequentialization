package globaltype;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Pollandt
 * 
 *         (t) Recursion on G replacing t in G.
 *
 */
public class GT extends G {

	/**
	 * t to be replaced later.
	 */
	private T t;

	/**
	 * Constructor.
	 * 
	 * @param t symbol to be replaced
	 */
	public GT(T t) {
		this.t = t;
	}

	/**
	 * @return T used for loops
	 */
	public T getT() {
		return t;
	}

	@Override
	public Set<T> getTs() {
		return new HashSet<T>() {
			private static final long serialVersionUID = 1L;
			{
				add(t);
			}
		};
	}
	
	@Override
	public String toString() {
		return t.toString();
	}
	
	@Override
	public String toLatex() {
		return t.getName();
	}
}