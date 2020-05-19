package picalculus;

import java.util.Map;

import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         ((vs) P) Restriction of session channel s to P.
 *
 */
public class PRestriction extends P {

	/**
	 * Process. Scope for s.
	 */
	private P p;

	/**
	 * Session channel to be restricting.
	 */
	private Session s;

	/**
	 * Constructor.
	 * 
	 * @param process process, scope for s
	 * @param session session to be restricted
	 */
	public PRestriction(P process, Session session) {
		this.p = process;
		this.s = session;
		subterms.add(p);
	}

	/**
	 * @return scope program sequence P
	 */
	public P getP() {
		return p;
	}

	/**
	 * @return session S to be scoped
	 */
	public Session getS() {
		return s;
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return new PRestriction(p.replacedX(replacingElement, replacedElement), s);
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		return new PRestriction(p.replacedVar(replacement), s);
	}

	@Override
	public String toString() {
		return "(v " + s.toString() + ") " + p.toString();
	}

	@Override
	public String toLatex() {
		return "\\PRes{s" /* + Integer.toString(s.getIndex()) */ + "}{" + p.toLatex() + "}";
	}
}