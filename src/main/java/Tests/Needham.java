package Tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import globaltype.*;
import picalculus.*;
import util.*;

enum ProcType {
	A, B, S
}

/**
 * @author Tim Pollandt
 *
 *         Model of the Needham-Schroeder pubic key algorithm
 */
public class Needham {

	Tuple<G, Set<P>> needhamFactory(int size) {
		if (size < 1) {
			throw new RuntimeException("Needham-Schroeder public key protocal must have communcation!");
		}

		// Output variable
		List<P> piTerms = new LinkedList<P>();

		GSend[] gParts = new GSend[7];
		// gParts[6] = new GTermination();
		// needed to connect g's parts later
		GSend[] gConnections = new GSend[7];

		P[] pParts = new P[4];
		P[] pConnections = new P[4];

		// Data types
		final U typeNumber = new U("int");

		// Type lists
		final List<U> listN = new LinkedList<U>() {
			private static final long serialVersionUID = 1L;
			{
				add(typeNumber);
			}
		};
		final List<U> listNN = new LinkedList<U>() {
			private static final long serialVersionUID = 1L;
			{
				add(typeNumber);
				add(typeNumber);
			}
		};

		// Process IDs (further generated in for loop
		final int S = processID(ProcType.S, 0);

		// Communication structures
		final SharedChannel a = new SharedChannel(0);
		final Session s = new Session(0);

		// Labels
		final Label labelReq = new Label("req");
		final Label labelPub = new Label("pub");
		final Label labelIni = new Label("ini");
		final Label labelAck = new Label("ack");

		// Variables
		final Variable varV = new Variable("v");
		final Variable varW = new Variable("w");
		final Variable varX = new Variable("x");
		final Variable varY = new Variable("y");
		final Variable varZ = new Variable("z");

		// Variable lists
		final List<Variable> listZ = new LinkedList<Variable>() {
			private static final long serialVersionUID = 1L;
			{
				add(varZ);
			}
		};
		final List<Variable> listXY = new LinkedList<Variable>() {
			private static final long serialVersionUID = 1L;
			{
				add(varX);
				add(varY);
			}
		};
		final List<Variable> listVW = new LinkedList<Variable>() {
			private static final long serialVersionUID = 1L;
			{
				add(varV);
				add(varW);
			}
		};

		// Expressions
		final Expression expW = new Expression("return w's value", new Tuple<String, Variable>("", varW));
		final Expression expX = new Expression("return x's value", new Tuple<String, Variable>("", varX));
		final Expression expY = new Expression("return y's value", new Tuple<String, Variable>("", varY));

		// Expression lists
		final List<Expression> listW = new LinkedList<Expression>() {
			private static final long serialVersionUID = 1L;
			{
				add(expW);
			}
		};

		for (int index = size; index > 0; index--) {
			// further IDs
			final int A = processID(ProcType.A, index);
			final int B = processID(ProcType.B, index);

			// Index specific variables
			final Variable varVn = new Variable("v" + Integer.toString(index));
			final Variable varWn = new Variable("w" + Integer.toString(index));
			final Variable varXn = new Variable("x" + Integer.toString(index));
			final Variable varYn = new Variable("y" + Integer.toString(index));

			// Variable lists
			final List<Variable> listVnWn = new LinkedList<Variable>() {
				private static final long serialVersionUID = 1L;
				{
					add(varVn);
					add(varWn);
				}
			};
			final List<Variable> listXnYn = new LinkedList<Variable>() {
				private static final long serialVersionUID = 1L;
				{
					add(varXn);
					add(varYn);
				}
			};

			// Index specific expressions
			final Expression expA = new Expression("return A's name",
					new Tuple<String, Variable>(Integer.toString(A), null));
			final Expression expB = new Expression("return B's name",
					new Tuple<String, Variable>(Integer.toString(B), null));
			final Expression expIA = new Expression("return A's nonce",
					new Tuple<String, Variable>("I" + Integer.toString(A), null));
			final Expression expIB = new Expression("return B's nonce",
					new Tuple<String, Variable>("I" + Integer.toString(B), null));
			final Expression expYn = new Expression("return Yn", new Tuple<String, Variable>("", varYn));
			final Expression expPKYn = new Expression("return Yn's pubkey",
					new Tuple<String, Variable>("pubKey(", varYn), new Tuple<String, Variable>(")", null));
			final Expression expWn = new Expression("return Wn", new Tuple<String, Variable>("", varYn));
			final Expression expPKWn = new Expression("return Wn's pubkey",
					new Tuple<String, Variable>("pubKey(", varWn), new Tuple<String, Variable>(")", null));

			// Expression lists
			final List<Expression> listAB = new LinkedList<Expression>() {
				private static final long serialVersionUID = 1L;
				{
					add(expA);
					add(expB);
				}
			};
			final List<Expression> listIAA = new LinkedList<Expression>() {
				private static final long serialVersionUID = 1L;
				{
					add(expIA);
					add(expA);
				}
			};
			final List<Expression> listBY = new LinkedList<Expression>() {
				private static final long serialVersionUID = 1L;
				{
					add(expB);
					add(expY);
				}
			};
			final List<Expression> listXIB = new LinkedList<Expression>() {
				private static final long serialVersionUID = 1L;
				{
					add(expX);
					add(expIB);
				}
			};
			final List<Expression> listPKWnWn = new LinkedList<Expression>() {
				private static final long serialVersionUID = 1L;
				{
					add(expPKWn);
					add(expWn);
				}
			};
			final List<Expression> listPKYnYn = new LinkedList<Expression>() {
				private static final long serialVersionUID = 1L;
				{
					add(expPKYn);
					add(expYn);
				}
			};

			// G
			gParts[0] = new GSend(A, S, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelReq, new Tuple<List<U>, G>(listNN, gParts[0]));
				}
			});
			gParts[1] = new GSend(S, A, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelPub, new Tuple<List<U>, G>(listNN, gParts[1]));
				}
			});
			gParts[2] = new GSend(A, B, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelIni, new Tuple<List<U>, G>(listNN, gParts[2]));
				}
			});
			gParts[3] = new GSend(B, S, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelReq, new Tuple<List<U>, G>(listNN, gParts[3]));
				}
			});
			gParts[4] = new GSend(S, B, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelPub, new Tuple<List<U>, G>(listNN, gParts[4]));
				}
			});
			gParts[5] = new GSend(B, A, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelReq, new Tuple<List<U>, G>(listNN, gParts[5]));
				}
			});
			gParts[6] = new GSend(A, B, new HashMap<Label, Tuple<List<U>, G>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelAck, new Tuple<List<U>, G>(listN, gParts[6]));
				}
			});
			// Prepare linking
			if (index == size) {
				for (int i = 0; i < 7; i++) {
					gConnections[i] = gParts[i];
				}
			}

			// PS

			pParts[0] = new PSendPassive(s, S, A, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelReq, new Tuple<List<Variable>, P>(listXnYn, pParts[0]));
				}
			});
			pParts[1] = new PSendActive(s, S, A, labelPub, listPKYnYn, pParts[1]);
			pParts[2] = new PSendPassive(s, S, B, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelReq, new Tuple<List<Variable>, P>(listVnWn, pParts[2]));
				}
			});
			pParts[3] = new PSendActive(s, S, B, labelPub, listPKWnWn, pParts[3]);
			// Prepare linking
			if (index == size) {
				for (int i = 0; i < 4; i++) {
					pConnections[i] = pParts[i];
				}
			}

			// PA

			P PA5 = new PSendActive(s, A, B, labelAck, listW, new PTermination());
			P PA4 = new PSendPassive(s, A, B, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelReq, new Tuple<List<Variable>, P>(listVW, PA5));
				}
			});
			P PA3 = new PSendActive(s, A, B, labelIni, listIAA, PA4);
			P PA2 = new PSendPassive(s, A, S, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelPub, new Tuple<List<Variable>, P>(listXY, PA3));
				}
			});
			P PA1 = new PSendActive(s, A, S, labelReq, listAB, PA2);
			P PA = new PInitiatePassive(a, A, s, PA1);

			// PB

			P PB5 = new PSendPassive(s, B, A, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelAck, new Tuple<List<Variable>, P>(listZ, new PTermination()));
				}
			});
			P PB4 = new PSendActive(s, B, A, labelReq, listXIB, PB5);
			P PB3 = new PSendPassive(s, B, S, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelPub, new Tuple<List<Variable>, P>(listVW, PB4));
				}
			});
			P PB2 = new PSendActive(s, B, S, labelReq, listBY, PB3);
			P PB1 = new PSendPassive(s, B, A, new HashMap<Label, Tuple<List<Variable>, P>>() {
				private static final long serialVersionUID = 1L;
				{
					put(labelIni, new Tuple<List<Variable>, P>(listXY, PB2));
				}
			});
			P PB = new PInitiatePassive(a, B, s, PB1);

			// Gathering data
			// piTerms.add(PA); piTerms.add(PB);
			piTerms.add(new PParallel(PA, PB));
		}

		gConnections[6].addGlobal(new GTermination());
		for (int i = 5; i >= 0; i--) {
			gConnections[i].addGlobal(gParts[i + 1]);
		}
		((PSendActive) pConnections[3]).addP(new PTermination());
		((PSendPassive) pConnections[2]).addP(pParts[3]);
		((PSendActive) pConnections[1]).addP(pParts[2]);
		((PSendPassive) pConnections[0]).addP(pParts[1]);
		// piTerms.add(new PInitiateActive(a, processID(ProcType.B, size), s,
		// pParts[0]));

		// Reducing to a single pi term.
		while (piTerms.size() > 1) {
			piTerms.set(0, new PParallel(piTerms.get(1), piTerms.get(0)));
			piTerms.remove(1);
		}

		piTerms.set(0,
				new PParallel(new PInitiateActive(a, processID(ProcType.B, size), s, pParts[0]), piTerms.get(0)));
		Set<P> set = new HashSet<P>() {
			private static final long serialVersionUID = 1L;
			{
				add(piTerms.get(0));
			}
		};

		return new Tuple<G, Set<P>>(gParts[0], set);
	}

	static int processID(ProcType type, int index) {
		switch (type) {
		case S:
			return 1;
		case A:
			return 2 * index;
		case B:
			return 2 * index + 1;
		}
		return -1;
	}
}
