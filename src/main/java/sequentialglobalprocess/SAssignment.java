package sequentialglobalprocess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Expression;
import util.Tuple;
import util.Variable;

/**
 * @author Tim Pollandt
 * 
 *         (v=e.S) Assignment for SGP.
 *
 */
public class SAssignment extends S {

	/**
	 * Remaining process flow s.
	 */
	private S s;

	/**
	 * Variable to be assigned to.
	 * 
	 * Only used for toLatex.
	 */
	private List<Variable> v;

	/**
	 * Expression to calculate value assigned to v.
	 * 
	 * Only used for toLatex.
	 */
	private List<Expression> e;

	/**
	 * Aggregation of v and e.
	 */
	private List<Tuple<Variable, Expression>> assignments;

	/**
	 * Constructor.
	 * 
	 * @param process     remaining process flow
	 * @param variables   variable to be assigned to
	 * @param expressions expression to calculate value assigned to v
	 */
	public SAssignment(S process, List<Variable> variables, List<Expression> expressions) {
		this.s = process;
		this.v = variables;
		this.e = expressions;
		int size = v.size();
		if (size != e.size()) {
			throw new RuntimeException(v.size() + "variables but " + e.size() + "expressions.");
		}
		assignments = new LinkedList<Tuple<Variable, Expression>>();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				assignments.add(new Tuple<Variable, Expression>(v.get(i), e.get(i)));
			}
		}
	}

	/**
	 * @return remaining process flow
	 */
	public S getProcess() {
		return s;
	}

	/**
	 * @return variables v
	 */
	public List<Variable> getVariables() {
		return v;
	}

	/**
	 * @return expressions e
	 */
	public List<Expression> getExpressions() {
		return e;
	}

	/**
	 * @return assignments
	 */
	public List<Tuple<Variable, Expression>> getAssignments() {
		return assignments;
	}

	@Override
	public String toString() {
		String subString = "{";
		for (Tuple<Variable, Expression> itm : assignments) {
			subString = subString + " " + itm.first().toString() + ":=" + itm.second().toString() + ", ";
		}
		return subString.substring(0, subString.length() - 2) + "}." + s.toString();
	}

	@Override
	public String toLatex() {
		String subString1 = "\\SAssign{(";
		String subString2 = ")}{(";
		for (Tuple<Variable, Expression> itm : assignments) {
			subString1 = subString1 + itm.first().toCode() + ", ";
			subString2 = subString2 + itm.second().toCode() + ", ";
		}
		return subString1.substring(0, subString1.length() - 2) + subString2.substring(0, subString2.length() - 2)
				+ ")}{" + s.toLatex() + "}";
	}

	@Override
	public String toCode(Map<Integer, S> furtherS, int indentLevel) {
		String subString = " ";
		for (Tuple<Variable, Expression> itm : assignments) {
			subString = subString + itm.first().toCode() + " := " + itm.second().toCode() + "; ";
		}
		return "atomic {" + subString + "}\n" + indent(indentLevel) + s.toCode(furtherS, indentLevel);
	}
}
