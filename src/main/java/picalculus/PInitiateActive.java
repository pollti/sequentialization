package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (a[2..n](s).P) Channel generation over shared a.
 *
 */
public class PInitiateActive extends P {

	/**
	 * Shared channel a.
	 */
	private SharedChannel a;

	/**
	 * Maximal index of elements for s. Other processes are [2..n].
	 */
	private int number;

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
	 * @param number  number of processes (s) [2..number]
	 * @param process process to be executed afterwards
	 * @param session session channel
	 */
	public PInitiateActive(SharedChannel a, int number, Session session, P process) {
		this.a = a;
		this.number = number;
		this.p = process;
		this.s = session;
		subterms.add(p);
	}

	/**
	 * @return shared channel
	 */
	public SharedChannel getA() {
		return a;
	}

	/**
	 * @return number of processes in s ([2..n])
	 */
	public int getProcNumber() {
		return number;
	}

	/**
	 * @return session s
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
		return new PInitiateActive(a, number, s, p.replacedX(replacingElement, replacedElement));
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PInitiateActive(a, number, s, p.replacedVar(replacement));
	}

	@Override
	public String toString() {
		return "‾" + a.toString() + "‾[2.." + Integer.toString(number) + "](" + s.toString() + ")." + p.toString();
	}
	
	@Override
	public String toLatex() {
		return "\\PReq{2.." + Integer.toString(number) + "}{" + p.toLatex() + "}";
	}
	
	@Override
	public int r() {
		return 1;
	}
}
