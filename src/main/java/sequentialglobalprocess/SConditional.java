package sequentialglobalprocess;

import java.util.Map;

import util.Condition;

/**
 * @author Tim Pollandt
 * 
 *         (if c then S1 else S2) Conditional execution.
 *
 */
public class SConditional extends S {

	/**
	 * Condition to be evaluated.
	 */
	private Condition condition;

	/**
	 * First process.
	 */
	private S sTrue;

	/**
	 * Second process.
	 */
	private S sFalse;

	/**
	 * Constructor.
	 * 
	 * @param condition to be evaluated
	 * @param sTrue     first process (used if condition evaluates to true)
	 * @param sFalse    second process (used if condition evaluates to false)
	 */
	public SConditional(Condition condition, S sTrue, S sFalse) {
		this.condition = condition;
		this.sTrue = sTrue;
		this.sFalse = sFalse;
	}

	/**
	 * @return condition c (boolean evaluable)
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * @param condValue true/false evaluated condition
	 * @return get process (depending on conditions result)
	 */
	public S getProcess(boolean condValue) {
		return condValue ? sTrue : sFalse;
	}

	@Override
	public String toString() {
		return "if " + condition.toString() + " then " + sTrue.toString() + " else " + sFalse.toString();
	}

	@Override
	public String toLatex() {
		return "\\SCase{(" + condition.toCode() + ")}{" + sTrue.toLatex() + "}{" + sFalse.toLatex() + "}";
	}

	@Override
	public String toCode(Map<Integer, S> furtherS, int indentLevel) {
		String cond = condition.toCode();
		int condLength = cond.length();
		int newIndent = (Math.max(7 + condLength, 11) - 1) / 3 + 1;
		return "if\n" + indent(indentLevel) + ":: " + cond + indent(newIndent - (condLength + 7) / 3 - 1)
				+ ((condLength % 3 == 1) ? " " : ((condLength % 3 == 2) ? "" : "  ")) + " -> "
				+ sTrue.toCode(furtherS, indentLevel + newIndent) + "\n" + indent(indentLevel) + ":: else "
				+ indent(newIndent - 4) + " -> " + sFalse.toCode(furtherS, indentLevel + newIndent) + "\n"
				+ indent(indentLevel) + "fi";
	}
}