package util;

/**
 * 
 * @author Tim Pollandt
 * 
 *         Variable for pi-calculus and SGP.
 *
 */
public class Variable {
	private String name;
	private String codeName;

	/**
	 * Constructor.
	 * 
	 * @param name     variable name
	 * @param codeName name to use in Promela code
	 */
	public Variable(String name, String codeName) {
		this.name = name;
		this.codeName = codeName;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name     variable name
	 */
	public Variable(String name) {
		this(name, "Error");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeName == null) ? 0 : codeName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Variable other = (Variable) obj;
		if (codeName == null) {
			if (other.codeName != null)
				return false;
		} else if (!codeName.equals(other.codeName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", codeName=" + codeName + "]";
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the codeName
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * @param codeName the codeName to set
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String toCode() {
		return codeName;
	}

}
