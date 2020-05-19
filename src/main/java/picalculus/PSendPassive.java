package picalculus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.Label;
import util.Tuple;
import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (s[r2,r1]?{l_i(x_i).P_i}i∈I) Receive l,x for an i from r2 to r1.
 *
 */
public class PSendPassive extends P implements PSend {

	/**
	 * Session for current communication.
	 */
	private Session s;

	/**
	 * Role of recipient. Modeled as int.
	 */
	private int r1;

	/**
	 * Role of sender. Modeled as int.
	 */
	private int r2;

	/**
	 * Map of labels to appropriate variable to be assigned to and following P
	 * sequence.
	 */
	private Map<Label, Tuple<List<Variable>, P>> indexedSet;

	/**
	 * Constructor.
	 * 
	 * @param session   session channel
	 * @param recipient role of recipient
	 * @param sender    role of sender
	 * @param LVPSet    Mapping of labels to Pairs of Variable and P; elements can
	 *                  be added later
	 */
	public PSendPassive(Session session, int recipient, int sender, Map<Label, Tuple<List<Variable>, P>> LVPSet) {
		this.s = session;
		this.r2 = recipient;
		this.r1 = sender;

		this.indexedSet = new HashMap<Label, Tuple<List<Variable>, P>>();
		LVPSet.forEach((key, value) -> {
			Tuple<List<Variable>, P> t = new Tuple<List<Variable>, P>(value.first(), value.second());
			indexedSet.put(key, t);
		});

		addPs();

	}

	/**
	 * Add new element in Set.
	 * 
	 * @param l label
	 * @param v variable
	 * @param p program
	 */
	public void addElement(Label l, List<Variable> v, P p) {
		indexedSet.put(l, new Tuple<List<Variable>, P>(v, p));
		addPs();
	}

	/**
	 * add Ps to subtypes
	 */
	private void addPs() {
		indexedSet.forEach((key, value) -> subterms.add(value.second()));
		subterms.remove(null);
	}

	/**
	 * Add p to single element in mapping.
	 * 
	 * @param p P term
	 */
	@Override
	public void addP(P p) {
		if (indexedSet.size() != 1) {
			throw new RuntimeException("Add position not unique");
		}
		Label l = new Label("Error in PSendPassive.");
		for (Label itm : indexedSet.keySet()) {
			l = itm;
		}
		Tuple<List<Variable>, P> t = indexedSet.get(l);
		t.setSecond(p);
		addPs();
	}

	/**
	 * @return session
	 */
	public Session getSession() {
		return s;
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
	 * @return Map of labels to appropriate variables to be assigned to and
	 *         following Pi calculus sequence
	 */
	public Map<Label, Tuple<List<Variable>, P>> getExpressions() {
		return indexedSet;
	}

	/**
	 * @return variables to given label
	 */
	public List<Variable> getVariables(Label l) {
		return indexedSet.get(l).first();
	}

	/**
	 * @return process to given label
	 */
	public P getProcess(Label l) {
		return indexedSet.get(l).second();
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		Map<Label, Tuple<List<Variable>, P>> newIndexedSet = new HashMap<Label, Tuple<List<Variable>, P>>();
		indexedSet.forEach((key, value) -> {
			newIndexedSet.put(key, new Tuple<List<Variable>, P>(value.first(),
					value.second().replacedX(replacingElement, replacedElement)));
		});
		return new PSendPassive(s, r2, r1, newIndexedSet);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		Map<Label, Tuple<List<Variable>, P>> newIndexedSet = new HashMap<Label, Tuple<List<Variable>, P>>();
		indexedSet.forEach((key, value) -> {
			Map<Variable, Variable> furtherReplacement = new HashMap<Variable, Variable>(replacement);
			for (Variable v : value.first()) {
				if (furtherReplacement.containsKey(v)) {
					furtherReplacement.remove(v);
				}
			}

			Tuple<List<Variable>, P> val = furtherReplacement.isEmpty()
					? new Tuple<List<Variable>, P>(value.first(), value.second())
					: new Tuple<List<Variable>, P>(value.first(), value.second().replacedVar(furtherReplacement));
			newIndexedSet.put(key, val);
		});
		return new PSendPassive(s, r2, r1, newIndexedSet);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public void replaceVar(Map<Variable, Variable> replacement) {
		indexedSet.forEach((key, value) -> {
			Map<Variable, Variable> furtherReplacement = new HashMap<Variable, Variable>(replacement);
			for (Variable v : value.first()) {
				if (furtherReplacement.containsKey(v)) {
					furtherReplacement.remove(v);
				}
			}
			if (!furtherReplacement.isEmpty()) {
				value.second().replaceVar(furtherReplacement);
			}
		});
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
			final Tuple<List<Variable>, P> tuple = indexedSet.get(key);
			String vars = String.join(",",
					tuple.first().stream().map(itm -> itm.toString()).collect(Collectors.toList()));
			set = set + key + "(" + vars + ")." + tuple.second();
		}
		return s.toString() + "[" + Integer.toString(r2) + " , " + Integer.toString(r1) + "]? {" + set + " }";
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
			final Tuple<List<Variable>, P> tuple = indexedSet.get(key);
			String vars = String.join(",",
					tuple.first().stream().map(itm -> itm.getName()).collect(Collectors.toList()));
			set = set + "\\PLab{" + key.getLabel() + "}{" + vars + "}{" + tuple.second().toLatex() + "}";
		}
		return "\\PGet{" + Integer.toString(r2) + "}{" + Integer.toString(r1) + "}{\\Set{" + set + "}}";
	}

	@Override
	public int r() {
		return r2;
	}
}
