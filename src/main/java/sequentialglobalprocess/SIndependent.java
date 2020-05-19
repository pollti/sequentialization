package sequentialglobalprocess;

import java.util.Map;

/**
 * @author Tim Pollandt
 * 
 *         (S1||S2) Two independent SGP.
 *
 */
public class SIndependent extends S {

	/**
	 * Counter for translation to Promela.
	 */
	static int counter = 0;

	/**
	 * First process.
	 */
	private S s1;

	/**
	 * Second process.
	 */
	private S s2;

	/**
	 * Constructor.
	 * 
	 * @param process1 first process
	 * @param process2 second process
	 */
	public SIndependent(S process1, S process2) {
		this.s1 = process1;
		this.s2 = process2;
	}

	/**
	 * @return first process S1, independent of S2
	 */
	public S getFirstS() {
		return s1;
	}

	/**
	 * @return second process S2, independent of S1
	 */
	public S getSecondS() {
		return s2;
	}

	@Override
	public String toString() {
		return s1.toString() + " || " + s2.toString();
	}

	@Override
	public String toLatex() {
		return "\\SPar{" + s1.toLatex() + "}{" + s2.toLatex() + "}";
	}

	@Override
	public String toCode(Map<Integer, S> furtherS, int indentLevel) {
		// Add S terms to map to be handled later.
		furtherS.put(counter, s1);
		furtherS.put(counter + 1, s2);
		// Execute proctypes with respective ID/number.
		return "run(S" + counter++ + "); run(S" + counter++ + ")";
	}
}