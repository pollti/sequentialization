package globaltype;

import java.util.Set;

/**
 * @author Tim Pollandt
 * 
 *         ((yt) G) Recursion on G replacing t in G.
 *
 */
public class GRecursion extends G {

	/**
	 * Process to replace t in.
	 */
	private G g;

	/**
	 * t to be replaced later.
	 */
	private T t;

	/**
	 * Constructor.
	 * 
	 * @param g G type
	 * @param t symbol in g to be replaced
	 */
	public GRecursion(G g, T t) {
		this.g = g;
		this.t = t;
		subtypes.add(this.g);
	}

	/**
	 * @return MPST g
	 */
	public G getG() {
		return g;
	}

	/**
	 * @return loop variable t
	 */
	public T getT() {
		return t;
	}

	@Override
	public Set<T> getTs() {
		Set<T> set = super.getTs();
		set.add(t);
		return set;
	}

	@Override
	public String toString() {
		return "(my " + t.toString() + ")" + g.toString();
	}
	
	@Override
	public String toLatex() {
		return "\\GRec{\texttt{" + t.getName() + "}}{" + g.toLatex() + "}";
	}

}