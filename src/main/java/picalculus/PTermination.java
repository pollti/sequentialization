package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (0) Termination of a pi-calculus process.
 *
 */
public class PTermination extends P {

	/**
	 * Constructor.
	 */
	public PTermination() {

	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return this;
	}

	@Override
	public P replacedVar(Map<Variable, Variable> replacement) {
		return this;
	}

	@Override
	public String toString() {
		return "0";
	}

	@Override
	public String toLatex() {
		return "\\PEnd";
	}
}