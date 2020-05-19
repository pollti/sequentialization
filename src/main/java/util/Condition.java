package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tim Pollandt
 * 
 *         Condition for pi-calculus and SGP. Can be evaluated to decide which
 *         process to follow.
 *
 */

public class Condition extends Conditional {

	/**
	 * Constructor 1.
	 * 
	 * @param function description of the conditional's function
	 * @param vars     mappings of stings and variables to build Promela code
	 */
	@SafeVarargs
	public Condition(String function, Tuple<String, Variable>... vars) {
		super(function, vars);
	}

	/**
	 * Constructor 2.
	 * 
	 * @param function description of the conditional's function
	 * @param vars     list of stings and variables to build Promela code
	 */
	public Condition(String function, List<Tuple<String, Variable>> vars) {
		super(function, vars);
	}

	/**
	 * Returns conditional in which a variable is replaced.
	 * 
	 * @param replacingElement element to replace with
	 * @param replacedElement  element to be replaced
	 * @return condition with replaced variable
	 */
	@Override
	public Condition replacedVar(Map<Variable, Variable> replacement) {
		List<Tuple<String, Variable>> newVars = new ArrayList<Tuple<String, Variable>>(this.variables);
		newVars.replaceAll(itm -> {
			if (itm.second() != null && replacement.containsKey(itm.second())) {
				return new Tuple<String, Variable>(itm.first(), replacement.get(itm.second()));
			}
			return itm;
		});
		return new Condition(this.desc, newVars);
	}

}
