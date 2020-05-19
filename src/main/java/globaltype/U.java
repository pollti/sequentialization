package globaltype;

/**
 * @author Tim Pollandt
 * 
 *         Sorts for MPST (G).
 *
 */
public class U {
	/**
	 * Data type to be used here. Should be Promela usable name.
	 */
	private String datatype;

	/**
	 * Constructor.
	 * @param Promela usable data type
	 */
	public U(String datatype) {
		this.datatype = datatype;
	}

	/**
	 * @return the datatype
	 */
	public String getDatatype() {
		return datatype;
	}

	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datatype == null) ? 0 : datatype.hashCode());
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
		U other = (U) obj;
		if (datatype == null) {
			if (other.datatype != null)
				return false;
		} else if (!datatype.equals(other.datatype))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "U [datatype=" + datatype + "]";
	}
}
