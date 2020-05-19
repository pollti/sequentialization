package Tests;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import globaltype.*;
import picalculus.*;

import util.*;

/**
 * @author Tim Pollandt
 *
 */
public class Auctioneer {

	// Begin Auctioneer Example

	/**
	 * global type and pi calculus type vars
	 */

	// senders and receivers
	final int A = 1;
	final int B1 = 2;
	final int B2 = 3;

	// labels
	final Label labelBid = new Label("Bid");
	final Label labelI = new Label("I");
	final Label labelNo = new Label("No");
	final Label labelS = new Label("S");

	/**
	 * global type vars
	 */

	// data types for communication
	final U tInt = new U("int");
	final U tEmpty = new U("empty");
	final List<U> typeInt = new LinkedList<U>() {
		private static final long serialVersionUID = 1L;
		{
			add(tInt);
		}
	};
	final List<U> typeEmpty = new LinkedList<U>() {
		private static final long serialVersionUID = 1L;
		{
			add(tEmpty);
		}
	};

	// recursion bases
	final T t = new T("t");

	// mappings for communication
	final Map<Label, Tuple<List<U>, G>> map8 = new HashMap<Label, Tuple<List<U>, G>>() {// identical to 7
		private static final long serialVersionUID = 1L;
		{
			put(labelS, new Tuple<List<U>, G>(typeInt, new GTermination()));
		}
	};
	final Map<Label, Tuple<List<U>, G>> map7 = new HashMap<Label, Tuple<List<U>, G>>() {// identical to 8
		private static final long serialVersionUID = 1L;
		{
			put(labelS, new Tuple<List<U>, G>(typeInt, new GTermination()));
		}
	};
	final Map<Label, Tuple<List<U>, G>> map6 = new HashMap<Label, Tuple<List<U>, G>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelI, new Tuple<List<U>, G>(typeInt, new GT(t)));
		}
	};
	final Map<Label, Tuple<List<U>, G>> map5 = new HashMap<Label, Tuple<List<U>, G>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelBid, new Tuple<List<U>, G>(typeInt, new GSend(A, B2, map6)));
			put(labelNo, new Tuple<List<U>, G>(typeEmpty, new GSend(A, B2, map8)));
		}
	};
	final Map<Label, Tuple<List<U>, G>> map4 = new HashMap<Label, Tuple<List<U>, G>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelI, new Tuple<List<U>, G>(typeInt, new GSend(B1, A, map5)));
		}
	};
	final Map<Label, Tuple<List<U>, G>> map3 = new HashMap<Label, Tuple<List<U>, G>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelBid, new Tuple<List<U>, G>(typeInt, new GSend(A, B1, map4)));
			put(labelNo, new Tuple<List<U>, G>(typeEmpty, new GSend(A, B1, map7)));
		}
	};
	final Map<Label, Tuple<List<U>, G>> map2 = new HashMap<Label, Tuple<List<U>, G>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelI, new Tuple<List<U>, G>(typeInt, new GRecursion(new GSend(B2, A, map3), t))); // second page in
																									// Tech Report
		}
	};
	final Map<Label, Tuple<List<U>, G>> map1 = new HashMap<Label, Tuple<List<U>, G>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelBid, new Tuple<List<U>, G>(typeInt, new GSend(A, B2, map2)));
		}
	};

	/**
	 * pi calculus vars
	 */

	// shared channels
	final SharedChannel a = new SharedChannel(0);

	// sessions
	final Session s = new Session(0);

	// variables
	final Variable b1 = new Variable("b", "b");
	final Variable noVar1 = new Variable("sold", "sold");
	final List<Variable> b = new LinkedList<Variable>() {
		private static final long serialVersionUID = 1L;
		{
			add(b1);
		}
	};
	final List<Variable> noVar = new LinkedList<Variable>() {
		private static final long serialVersionUID = 1L;
		{
			add(noVar1);
		}
	};

	// recursion endpoints
	final X X_A = new X("A", "Error");
	final X X_B1 = new X("B1", "Error");
	final X X_B2 = new X("B2", "Error");

	// expressions
	final Expression e_exprB = new Expression("variable value", new Tuple<String, Variable>("", b1)); // send b's value
	final Expression e_incB1_0 = new Expression("inc_B1(0)", new Tuple<String, Variable>("incB1 + 0", null));
	final Expression e_incB1_b = new Expression("inc_B1(b)", new Tuple<String, Variable>("incB1 + ", b1));
	final Expression e_incB2_b = new Expression("inc_B2(b)", new Tuple<String, Variable>("incB2 + ", b1));
	final Expression e_nothing1 = new Expression("role 1", new Tuple<String, Variable>("1", null));
	final Expression e_nothing2 = new Expression("role 2", new Tuple<String, Variable>("2", null));
	final List<Expression> exprB = new LinkedList<Expression>() {
		private static final long serialVersionUID = 1L;
		{
			add(e_exprB);
		}
	};
	final List<Expression> incB1_0 = new LinkedList<Expression>() {
		private static final long serialVersionUID = 1L;
		{
			add(e_incB1_0);
		}
	};
	final List<Expression> incB1_b = new LinkedList<Expression>() {
		private static final long serialVersionUID = 1L;
		{
			add(e_incB1_b);
		}
	};
	final List<Expression> incB2_b = new LinkedList<Expression>() {
		private static final long serialVersionUID = 1L;
		{
			add(e_incB2_b);
		}
	};
	final List<Expression> nothing1 = new LinkedList<Expression>() {
		private static final long serialVersionUID = 1L;
		{
			add(e_nothing1);
		}
	};
	final List<Expression> nothing2 = new LinkedList<Expression>() {
		private static final long serialVersionUID = 1L;
		{
			add(e_nothing2);
		}
	};

	// conditions
	final Condition incB1bLEQmaxB1 = new Condition("inc_B1(b)<=max_B1", new Tuple<String, Variable>("incB1 + ", b1),
			new Tuple<String, Variable>(" <= maxB1", null));
	final Condition incB2bLEQmaxB2 = new Condition("inc_B2(b)<=max_B2", new Tuple<String, Variable>("incB2 + ", b1),
			new Tuple<String, Variable>(" <= maxB2", null));

	// maps and pi calculus
	Map<Label, Tuple<List<Variable>, P>> mapP5 = new HashMap<Label, Tuple<List<Variable>, P>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelI, new Tuple<List<Variable>, P>(b, new PX(X_B2)));
			put(labelS, new Tuple<List<Variable>, P>(b, new PTermination()));
		}
	};

	final P thirdPFollow2 = new PSendActive(s, B2, A, labelBid, incB2_b, new PSendPassive(s, B2, A, mapP5));
	final P thirdPFollow1 = new PConditional(incB2bLEQmaxB2, thirdPFollow2,
			new PSendActive(s, B2, A, labelNo, nothing1, new PTermination()));

	Map<Label, Tuple<List<Variable>, P>> mapP4 = new HashMap<Label, Tuple<List<Variable>, P>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelI, new Tuple<List<Variable>, P>(b, new PRecursion(thirdPFollow1, X_B2)));
		}
	};
	Map<Label, Tuple<List<Variable>, P>> mapP3 = new HashMap<Label, Tuple<List<Variable>, P>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelI,
					new Tuple<List<Variable>, P>(b,
							new PConditional(incB1bLEQmaxB1, new PSendActive(s, B1, A, labelBid, incB1_b, new PX(X_B1)),
									new PSendActive(s, B1, A, labelNo, nothing2, new PTermination()))));
			put(labelS, new Tuple<List<Variable>, P>(b, new PTermination()));
		}
	};
	Map<Label, Tuple<List<Variable>, P>> mapP2 = new HashMap<Label, Tuple<List<Variable>, P>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelBid, new Tuple<List<Variable>, P>(b, new PSendActive(s, A, B2, labelI, exprB, new PX(X_A))));
			put(labelNo,
					new Tuple<List<Variable>, P>(noVar, new PSendActive(s, A, B2, labelS, exprB, new PTermination())));
		}
	};
	Map<Label, Tuple<List<Variable>, P>> mapP1 = new HashMap<Label, Tuple<List<Variable>, P>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelBid, new Tuple<List<Variable>, P>(b,
					new PSendActive(s, A, B1, labelI, exprB, new PSendPassive(s, A, B1, mapP2))));
			put(labelNo,
					new Tuple<List<Variable>, P>(noVar, new PSendActive(s, A, B1, labelS, exprB, new PTermination())));
		}
	};

	final P firstPFollow2 = new PRecursion(new PSendPassive(s, A, B2, mapP1), X_A);
	final P firstPFollow1 = new PSendPassive(s, A, B1, new HashMap<Label, Tuple<List<Variable>, P>>() {
		private static final long serialVersionUID = 1L;
		{
			put(labelBid, new Tuple<List<Variable>, P>(b, new PSendActive(s, A, B2, labelI, exprB, firstPFollow2)));
		}
	});
	final P secondPFollow1 = new PRecursion(new PSendPassive(s, B1, A, mapP3), X_B1);

	final P firstP = new PInitiateActive(a, 3/* B2 */, s, firstPFollow1);
	P secondP = new PInitiatePassive(a, B1, s, new PSendActive(s, B1, A, labelBid, incB1_0, secondPFollow1));
	P thirdP = new PInitiatePassive(a, B2, s, new PSendPassive(s, B2, A, mapP4));

	/**
	 * whole types
	 */

	final G AuctioneerGlobal = new GSend(B1, A, map1);
	final P AuctioneerPi = new PParallel(firstP, new PParallel(secondP, thirdP));

	// End Auctioneer Example
}
