package sequentialglobalprocess;

import java.util.Map;

/**
 * @author Tim Pollandt
 * 
 *         (0) Termination of a SGP.
 *
 */
public class STermination extends S {

	/**
	 * Constructor.
	 */
	public STermination() {

	}

	@Override
	public String toString() {
		return "0";
	}

	@Override
	public String toLatex() {
		return "\\SEnd";
	}

	@Override
	public String toCode(Map<Integer, S> furtherS, int indentLevel) {
		return "goto LEnd;";
	}
}