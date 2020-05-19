package picalculus;

/**
 * @author Tim Pollandt
 * 
 *         Shared pi-claculus channel (a).
 * 
 *
 */
public class SharedChannel {
	int index;

	public SharedChannel(int index) {
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
		SharedChannel other = (SharedChannel) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SharedChannel [index=" + index + "]";
	}

}
