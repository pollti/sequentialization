package sequentialglobalprocess;

import java.util.Map;

import util.X;

/**
 * @author Tim Pollandt
 * 
 *         ((yX) S) Recursion on S replacing X in S.
 *
 */
public class SRecursion extends S {

	/**
	 * Process to replace x in.
	 */
	private S s;

	/**
	 * X to be replaced later.
	 */
	private X x;

	/**
	 * Constructor.
	 * 
	 * @param process process
	 * @param x       symbol in S to be replaced
	 */
	public SRecursion(S process, X x) {
		this.s = process;
		this.x = x;
	}

	/**
	 * @return program sequence S
	 */
	public S getS() {
		return s;
	}

	/**
	 * @return loop variable x
	 */
	public X getX() {
		return x;
	}

	@Override
	public String toString() {
		return "(my " + x.toString() + ") " + s.toString();
	}

	@Override
	public String toLatex() {
		return "\\SRep{" + x.toCode() + "}{" + s.toLatex() + "}";
	}

	@Override
	public String toCode(Map<Integer, S> furtherS, int indentLevel) {
		return "\n" + indent(indentLevel - 1) + "L" + x.toCode() + ":\n" + indent(indentLevel)
				+ s.toCode(furtherS, indentLevel);
	}
}