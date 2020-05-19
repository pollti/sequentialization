package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         ((yX) P) Recursion on P replacing X in P.
 *
 */
public class PRecursion extends P {

	/**
	 * Process to replace x in.
	 */
	private P p;

	/**
	 * X to be replaced later.
	 */
	private X x;

	/**
	 * Constructor.
	 * 
	 * @param process process
	 * @param x       symbol in P to be replaced
	 */
	public PRecursion(P process, X x) {
		this.p = process;
		this.x = x;
		subterms.add(p);
	}

	/**
	 * @return program sequence P
	 */
	public P getP() {
		return p;
	}

	/**
	 * @return loop variable x
	 */
	public X getX() {
		return x;
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return new PRecursion(p.replacedX(replacingElement, replacedElement), x);
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PRecursion(p.replacedVar(replacement), x);
	}

	@Override
	public String toString() {
		return "(my " + x.toString() + ") " + p.toString();
	}

	@Override
	public String toLatex() {
		return "\\PRep{" + x.getName() + "}{" + p.toLatex() + "}";
	}
}