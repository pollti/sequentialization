package picalculus;

import java.util.Map;

import util.Condition;
import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (if c then P1 else P2) Conditional execution.
 *
 */
public class PConditional extends P {

	/**
	 * Condition to be evaluated.
	 */
	private Condition condition;

	/**
	 * First process.
	 */
	private P pTrue;

	/**
	 * Second process.
	 */
	private P pFalse;

	/**
	 * Constructor.
	 * 
	 * @param condition to be evaluated
	 * @param pTrue     first process (used if condition evaluates to true)
	 * @param pFalse    second process (used if condition evaluates to false)
	 */
	public PConditional(Condition condition, P pTrue, P pFalse) {
		this.condition = condition;
		this.pTrue = pTrue;
		this.pFalse = pFalse;
		subterms.add(pTrue);
		subterms.add(pFalse);
	}

	/**
	 * @return condition c (boolean evaluable)
	 */
	public Condition getCondition() {
		return condition;
	}
	
	/**
	 * @param replacement variables to replace in the returned expression
	 * @return condition c (boolean evaluable)
	 */
	public Condition getCondition(Map<Variable,Variable> replacement) {
		return condition.replacedVar(replacement);
	}

	/**
	 * @param condValue true/false evaluated condition
	 * @return get process (depending on conditions result)
	 */
	public P getProcess(boolean condValue) {
		return condValue ? pTrue : pFalse;
	}

	/**
	 * @return the pFalse
	 */
	public P getpFalse() {
		return pFalse;
	}

	/**
	 * @param pFalse the pFalse to set
	 */
	public void setpFalse(P pFalse) {
		this.pFalse = pFalse;
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return new PConditional(condition, pTrue.replacedX(replacingElement, replacedElement),
				pFalse.replacedX(replacingElement, replacedElement));
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PConditional(condition.replacedVar(replacement), pTrue.replacedVar(replacement),
				pFalse.replacedVar(replacement));
	}
	
	@Override
	@Deprecated
	public void replaceVar(Map<Variable, Variable> replacement) {
		condition = condition.replacedVar(replacement);
		super.replaceVar(replacement);
	}

	@Override
	public String toString() {
		return "if " + condition.toString() + " then " + pTrue.toString() + " else " + pFalse.toString();
	}

	@Override
	public String toLatex() {
		return "\\PCond{(" + condition.toCode() + ")}{" + pTrue.toLatex() + "}{" + pFalse.toLatex() + "}";
	}
}