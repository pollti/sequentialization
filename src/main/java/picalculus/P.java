package picalculus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (P) General version of pi-calculus AST. Can be extended by different
 *         types of pi-calculus terms. Functions may be added if useful for
 *         conversion.
 *
 */
public abstract class P {

	/**
	 * Lists all underlying process flows in grammar.
	 */
	Set<P> subterms = new HashSet<P>();

	public P() {

	}

	/**
	 * Create new P in which PX with X are replaced by replacingElement.
	 * 
	 * @param replacingElement element to be inserted for X
	 * @param replacedElement  element to be replaced
	 * @return new P with replaced X
	 */
	public abstract P replacedX(P replacingElement, X replacedElement);

	/**
	 * Create new P in which variable replacedElement is replaced by
	 * replacingElement. Used for case 6.
	 * 
	 * @param replacement variable mapping for replacing
	 * @return new P with replaced variable
	 */
	@Deprecated
	public abstract P replacedVar(Map<Variable, Variable> replacement);
	
	/**
	 * Create new P in which variable replacedElement is replaced by
	 * replacingElement. Used for case 6. Modifies existing object!
	 * 
	 * @param replacement variable mapping for replacing
	 * @return new P with replaced variable
	 */
	@Deprecated
	public void replaceVar(Map<Variable, Variable> replacement) {
		for(P itm: subterms) {
			itm.replaceVar(replacement);
		}
	}

	@Override
	public abstract String toString();

	public abstract String toLatex();

	/**
	 * Get role in p. Needed for sequentialization. Referred to as r() in paper. Can
	 * only have one role. Throwing exception otherwise.
	 * 
	 * @return set of used roles
	 */
	public int r() {
		for (P itm : subterms) {
			int itmRole = itm.r();
			if (itmRole != -1) {
				return itmRole;
			}
		}
		return -1;
	}
}
