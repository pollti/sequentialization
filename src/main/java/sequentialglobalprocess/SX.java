package sequentialglobalprocess;

import java.util.Map;

import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (X) Recursion on S replacing X in S.
 *
 */
public class SX extends S {
	/**
	 * x to be replaced later.
	 */
	private X x;

	/**
	 * Constructor.
	 * 
	 * @param t symbol to be replaced
	 */
	public SX(X x) {
		this.x = x;
	}

	/**
	 * @return T used for loops
	 */
	public X getX() {
		return x;
	}

	@Override
	public String toString() {
		return x.toString();
	}

	@Override
	public String toLatex() {
		return x.toCode();
	}

	@Override
	public String toCode(Map<Integer, S> furtherS, int indentLevel) {
		return "goto L" + x.toCode() + ";";
	}
}
