package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (P1 | P2) Two parallel composed processes.
 *
 */
public class PParallel extends P {

	/**
	 * First process.
	 */
	private P p1;

	/**
	 * Second process.
	 */
	private P p2;

	/**
	 * Constructor.
	 * 
	 * @param process1 first process
	 * @param process2 second process
	 */
	public PParallel(P process1, P process2) {
		this.p1 = process1;
		this.p2 = process2;
		subterms.add(p1);
		subterms.add(p2);
	}

	/**
	 * @return first program sequence P1
	 */
	public P getFirstP() {
		return p1;
	}

	/**
	 * @return second program sequence P2
	 */
	public P getSecondP() {
		return p2;
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return new PParallel(p1.replacedX(replacingElement, replacedElement),
				p2.replacedX(replacingElement, replacedElement));
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PParallel(p1.replacedVar(replacement), p2.replacedVar(replacement));
	}

	@Override
	public String toString() {
		return p1.toString() + " | " + p2.toString();
	}
	
	@Override
	public String toLatex() {
		return "\\PPar{" + p1.toLatex() + "}{" + p2.toLatex() + "}";
	}
}