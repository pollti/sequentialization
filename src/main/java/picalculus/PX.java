package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (X) Recursion element to be later replaced in P.
 *
 */
public class PX extends P {

	/**
	 * X to be replaced later.
	 */
	private X x;

	/**
	 * Constructor.
	 * 
	 * @param x symbol to be replaced
	 */
	public PX(X x) {
		this.x = x;
	}

	/**
	 * @return X used for loops
	 */
	public X getX() {
		return x;
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return x == replacedElement ? replacingElement : this;
	}

	@Override
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PX(x);
	}

	@Override
	public String toString() {
		return x.toString();
	}
	
	@Override
	public String toLatex() {
		return x.getName();
	}
}