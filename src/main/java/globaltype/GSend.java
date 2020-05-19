package globaltype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import util.Label;
import util.Tuple;

/**
 * @author Tim Pollandt
 * 
 *         (r1->r2:{l_i(U_i).G_i}i∈I) Receive l,U for an i from r2 to r1.
 *
 */
public class GSend extends G {

	/**
	 * Role of sender. Modeled as int.
	 */
	private int r1;

	/**
	 * Role of recipient. Modeled as int.
	 */
	private int r2;

	/**
	 * Map of labels to appropriate sorts to be assigned to and following G
	 * sequence.
	 */
	private Map<Label, Tuple<List<U>, G>> indexedSet;

	/**
	 * Constructor.
	 * 
	 * @param session   session channel
	 * @param sender    role of sender
	 * @param recipient role of recipient
	 * @param LVPSet    Mapping of labels to Pairs of sorts and G; elements can be
	 *                  added later
	 */
	public GSend(int sender, int recipient, Map<Label, Tuple<List<U>, G>> LVPSet) {
		this.r1 = sender;
		this.r2 = recipient;
		this.indexedSet = new HashMap<Label, Tuple<List<U>, G>>();
		LVPSet.forEach((key, value) -> {
			Tuple<List<U>, G> t = new Tuple<List<U>, G>(value.first(), value.second());
			indexedSet.put(key, t);
		});
		addGs();
	}

	/**
	 * Add new element in Set.
	 * 
	 * @param l label
	 * @param u sorts
	 * @param g MPST
	 */
	public void addElement(Label l, List<U> u, G g) {
		indexedSet.put(l, new Tuple<List<U>, G>(u, g));
		addGs();
	}

	/**
	 * Add g to single element in mapping.
	 * 
	 * @param g G term
	 */
	public void addGlobal(G g) {
		if (indexedSet.size() != 1) {
			throw new RuntimeException("Add position not unique");
		}
		Label l = new Label("Error in GSend.");
		for (Label itm : indexedSet.keySet()) {
			l = itm;
		}
		Tuple<List<U>, G> t = indexedSet.get(l);
		t.setSecond(g);
		addGs();
	}

	/**
	 * @return sender role
	 */
	public int getRole1() {
		return r1;
	}

	/**
	 * @return recipient role
	 */
	public int getRole2() {
		return r2;
	}

	/**
	 * @return Map of labels to appropriate sorts and following Global Type sequence
	 */
	public Map<Label, Tuple<List<U>, G>> getExpressions() {
		return indexedSet;
	}

	/**
	 * @return sort to given label
	 */
	public List<U> getSort(Label l) {
		return indexedSet.get(l).first();
	}

	/**
	 * @return global type part to given label
	 */
	public G getGlobalType(Label l) {
		return indexedSet.get(l).second();
	}

	/**
	 * @return whether mapping contains label l
	 */
	public boolean containsLabel(Label l) {
		return indexedSet.containsKey(l);
	}

	/**
	 * add Gs to subtypes
	 */
	private void addGs() {
		indexedSet.forEach((key, value) -> subtypes.add(value.second()));
		subtypes.remove(null);
	}

	@Override
	public Set<Integer> r() {
		Set<Integer> rs = super.r();
		rs.add(r1);
		rs.add(r2);
		return rs;
	}

	@Override
	public String toString() {
		String set = null;
		for (Label key : indexedSet.keySet()) {
			if (set == null) {
				set = "";
			} else {
				set = set + ", ";
			}
			final Tuple<List<U>, G> tuple = indexedSet.get(key);
			String datatypes = String.join(",",
					tuple.first().stream().map(itm -> itm.toString()).collect(Collectors.toList()));
			set = set + key + "<" + datatypes + ">." + tuple.second();
		}
		return Integer.toString(r1) + "->" + Integer.toString(r2) + ": {" + set + "}";
	}

	@Override
	public String toLatex() {
		String set = null;
		for (Label key : indexedSet.keySet()) {
			if (set == null) {
				set = "";
			} else {
				set = set + ", ";
			}
			final Tuple<List<U>, G> tuple = indexedSet.get(key);
			String datatypes = String.join("], Sort[",
					tuple.first().stream().map(itm -> itm.getDatatype()).collect(Collectors.toList()));
			set = set + "\\GLab{\\Label[" + key.getLabel() + "]}{\\Sort[" + datatypes + "]}{" + tuple.second().toLatex()
					+ "}";
		}
		return "\\GCom{\\Role[" + Integer.toString(r1) + "]}{\\Role[" + Integer.toString(r2) + "]}{\\\\Set{" + set + "}}";
	}
}
