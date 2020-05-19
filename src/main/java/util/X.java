package util;

/**
 * @author Tim Pollandt
 * 
 *         X for pi-calculus and SGP.
 *
 */
public class X extends RecursionBase {

	String codeName;

	/**
	 * @param name     recursion base name
	 * @param codeName name to use in Promela code
	 */
	public X(String name, String codeName) {
		super(name);
		this.codeName = codeName;
	}

	public String toCode() {
		return codeName;
	}

}