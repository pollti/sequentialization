package picalculus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.Expression;
import util.Label;
import util.Variable;
import util.X;

/**
 * @author Tim Pollandt
 * 
 *         (s[r1,r2]!l(e).P) Send e from r1 to r2.
 *
 */
public class PSendActive extends P implements PSend {

	/**
	 * Session for current communication.
	 */
	private Session s;

	/**
	 * Role of sender. Modeled as int.
	 */
	private int r1;

	/**
	 * Role of recipient. Modeled as int.
	 */
	private int r2;

	/**
	 * Label sent.
	 */
	private Label l;

	/**
	 * Expression to calculate payload.
	 */
	private List<Expression> e;

	/**
	 * Process to be executed afterwards.
	 */
	private P p;

	/**
	 * Constructor.
	 * 
	 * @param session     session channel
	 * @param sender      role of sender
	 * @param recipient   role of recipient
	 * @param label       label to send with data
	 * @param expressions to calculate payload to be sent
	 * @param process     process to be executed afterwards
	 */
	public PSendActive(Session session, int sender, int recipient, Label label, List<Expression> expressions,
			P process) {
		this.s = session;
		this.r1 = sender;
		this.r2 = recipient;
		this.l = label;
		this.e = expressions;
		this.p = process;
		subterms.add(p);
	}

	/**
	 * @return session
	 */
	public Session getSession() {
		return s;
	}

	/**
	 * @return sender role
	 */
	public int getRole1() {
		return r1;
	}

	/**
	 * @return recipient role
	 */
	public int getRole2() {
		return r2;
	}

	/**
	 * @return label sent with payload
	 */
	public Label getLabel() {
		return l;
	}

	/**
	 * @return expressions to calculate payload value
	 */
	public List<Expression> getExpressions() {
		return e;
	}

	/**
	 * @param replacement variables to replace in the returned expression
	 * @return expressions to calculate payload value
	 */
	public List<Expression> getExpressions(Map<Variable, Variable> replacement) {
		return e.stream().map(itm -> itm.replacedVar(replacement)).collect(Collectors.toList());
	}

	/**
	 * @return program sequence P
	 */
	public P getP() {
		return p;
	}

	/**
	 * Add p.
	 * 
	 * @param p P term
	 */
	@Override
	public void addP(P p) {
		this.p = p;
		subterms.add(p);
		subterms.remove(null);
	}

	@Override
	public P replacedX(P replacingElement, X replacedElement) {
		return new PSendActive(s, r1, r2, l, e, p.replacedX(replacingElement, replacedElement));
	}

	@Override
	@Deprecated
	public P replacedVar(Map<Variable, Variable> replacement) {
		List<Expression> exps = e.stream().map(itm -> itm.replacedVar(replacement)).collect(Collectors.toList());
		return new PSendActive(s, r1, r2, l, exps, p.replacedVar(replacement));
	}

	@Override
	@Deprecated
	public void replaceVar(Map<Variable, Variable> replacement) {
		// e.replaceAll(itm -> itm.replacedVar(replacement));
		List<Expression> newE = new LinkedList<Expression>();
		e.forEach(itm -> newE.add(itm.replacedVar(replacement)));
		e = newE;

		super.replaceVar(replacement);
	}

	@Override
	public String toString() {
		String exps = String.join(",", e.stream().map(itm -> itm.toString()).collect(Collectors.toList()));
		return s.toString() + "[" + Integer.toString(r1) + " , " + Integer.toString(r2) + "]!" + l.toString() + "<"
				+ exps + ">." + p.toString();
	}

	@Override
	public String toLatex() {
		return "\\PSend{\\Role[" + Integer.toString(r1) + "]}{\\Role[" + Integer.toString(r2) + "]}{" + l.getLabel()
				+ "}{(" + String.join(", ", e.stream().map(itm -> itm.toCode()).collect(Collectors.toList())) + ")}{"
				+ p.toLatex() + "}";
	}

	@Override
	public int r() {
		return r1;
	}
}
