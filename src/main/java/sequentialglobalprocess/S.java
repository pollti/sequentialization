package sequentialglobalprocess;

import java.util.Map;

/**
 * @author Tim Pollandt
 * 
 *         (S) General version of sequential global process. Can be extended by
 *         different types of SGP terms. Functions may be added if useful for
 *         conversion.
 *
 */
public abstract class S {
	public S() {

	}

	String indent(int indentLevel) {
		String s = "";
		for (int i = 1; i <= indentLevel; i++) {
			s = s + "   ";
		}
		return s;
	}
	
	@Override
	public abstract String toString();
	
	public abstract String toLatex();

	public abstract String toCode(Map<Integer, S> furtherS, int indentLevel);
}
