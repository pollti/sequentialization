package globaltype;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Pollandt
 * 
 *         (G) General version of Global Type (MPST) AST. Can be extended by
 *         different types of global terms. Functions may be added if useful for
 *         conversion.
 *
 */
public abstract class G {

	/**
	 * Lists all underlying global types in grammar.
	 */
	Set<G> subtypes = new HashSet<G>();

	public G() {

	}

	/**
	 * Get all T elements in G.
	 * 
	 * @return set of T elements that may be contained in G
	 */
	public Set<T> getTs() {
		Set<T> set = new HashSet<>();
		subtypes.forEach(s -> set.addAll(s.getTs()));
		return set;
	}

	/**
	 * Get roles in g. Needed for sequentialization. Referred to as r() in paper.
	 * 
	 * @return set of used roles
	 */
	public Set<Integer> r() {
		Set<Integer> set = new HashSet<Integer>();
		subtypes.forEach(s -> set.addAll(s.r()));
		return set;
	}

	@Override
	public abstract String toString();
	
	public abstract String toLatex();
}
