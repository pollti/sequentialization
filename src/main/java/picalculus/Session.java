package picalculus;

/**
 * @author Tim Pollandt
 * 
 *         Sessions for pi-calculus.
 */
public class Session {

	/**
	 * Session ID. Can be replaced by any other data type later.
	 */
	private int index;

	/**
	 * Constructor for session channels.
	 * 
	 * @param index new session channel's index. Should be positive and differ from
	 *              other indices channels.
	 */
	public Session(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Session other = (Session) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [index=" + index + "]";
	}

}
