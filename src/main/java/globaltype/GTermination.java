package globaltype;

/**
 * @author Tim Pollandt
 * 
 *         (end) Termination of a MPST.
 *
 */
public class GTermination extends G {

	/**
	 * Constructor.
	 */
	public GTermination() {

	}
	
	@Override
	public String toString() {
		return "end";
	}
	
	@Override
	public String toLatex() {
		return "\\GEnd";
	}
}