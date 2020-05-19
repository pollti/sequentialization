package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (a(s[r]).P) Incoming channel generation over shared a.
 *
 */
public class PInitiatePassive extends P {

	/**
	 * Shared channel a.
	 */
	private SharedChannel a;

	/**
	 * Role in s.
	 */
	private int r;

	/**
	 * Session to be created.
	 */
	private Session s;

	/**
	 * Process to continue with.
	 */
	private P p;

	/**
	 * Constructor.
	 * 
	 * @param a       shared channel
	 * @param process process to be executed afterwards
	 * @param session session channel
	 */
	public PInitiatePassive(SharedChannel a, int number, Session session, P process) {
		this.a = a;
		this.r = number;
		this.p = process;
		this.s = session;
		subterms.add(p);
	}

	/**
	 * @return first program sequence P1
	 */
	public SharedChannel getFirstSharedChannel() {
		return a;
	}

	/**
	 * @return first program sequence P1
	 */
	public int getRole() {
		return r;
	}

	/**
	 * @return first program sequence P1
	 */
	public Session getSession() {
		return s;
	}

	/**
	 * @return program sequence
	 */
	public P getP() {
		return p;
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return new PInitiatePassive(a, r, s, p.replacedX(replacingElement, replacedElement));
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PInitiatePassive(a, r, s, p.replacedVar(replacement));
	}

	@Override
	public String toString() {
		return "a(" + s.toString() + "[" + Integer.toString(r) + "])." + p.toString();
	}

	@Override
	public String toLatex() {
		return "\\PAcc{" + Integer.toString(r) + "}{" + p.toLatex() + "}";
	}
	
	@Override
	public int r() {
		return r;
	}
}
