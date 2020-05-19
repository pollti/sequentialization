package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Tim Pollandt
 * 
 *         Conditional for pi-calculus and SGP. Can be evaluated and translated
 *         to Promela code.
 *
 */

public abstract class Conditional {
	/**
	 * Description of the conditional.
	 */
	String desc;

	/**
	 * Variables used in conditional.
	 */
	List<Tuple<String, Variable>> variables;

	/**
	 * Constructor 1.
	 * 
	 * @param function description of the conditional's function
	 * @param vars     mappings of stings and variables to build Promela code
	 */
	@SafeVarargs
	public Conditional(String function, Tuple<String, Variable>... vars) {
		this.desc = function;
		this.variables = Arrays.asList(vars);
	}

	/**
	 * Constructor 2.
	 * 
	 * @param function description of the conditional's function
	 * @param vars     list of stings and variables to build Promela code
	 */
	public Conditional(String function, List<Tuple<String, Variable>> vars) {
		this.desc = function;
		this.variables = new ArrayList<Tuple<String, Variable>>(vars);
	}

	/**
	 * Returns conditional in which a variable is replaced.
	 * 
	 * @param replacingElement element to replace with
	 * @param replacedElement  element to be replaced
	 * @return condition with replaced variable
	 */
	public abstract Conditional replacedVar(Map<Variable, Variable> replacement);

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the variables
	 */
	public List<Tuple<String, Variable>> getVariables() {
		return variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(List<Tuple<String, Variable>> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [desc=" + desc + ", variables=" + variables + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Condition other = (Condition) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}

	public String toCode() {
		String code = "";
		for (Tuple<String, Variable> itm : variables) {
			code = code + itm.first() + ((itm.second() == null) ? "" : itm.second().toCode());
		}
		return code;
	}
}
