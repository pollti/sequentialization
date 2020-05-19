package Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import Tests.Execute;
import globaltype.*;
import picalculus.*;
import sequentialglobalprocess.*;
import util.*;

/**
 * 
 * Sequentializes a global type/pi calculus algorithm to SGP. Use convert.
 * 
 * @author Tim Pollandt
 *
 */
public class Sequentializer {

	String inputFilename = "data/inputs";

	/**
	 * Mapping of T to X. Maps Global Type's recursion symbols to new ones for SGP.
	 */
	private Map<T, X> mappingT_X;

	/**
	 * Mapping of Sessions, Roles and Variables to V variables (variables in G).
	 */
	private Map<Session, Map<Integer, Map<Variable, Variable>>> mappingVars;

	/*
	 * Holds session for case 10. Must be modified for several sessions.
	 */
	private Session session;

	/**
	 * Sequentializes a global type/pi calculus algorithm to SGP.
	 * 
	 * @param piInput    List of pi calculus processes
	 * @param globaltype MPST global type
	 * @return sequential global process
	 */
	public S convert(Set<P> piInput, G globaltype) {
		FileExport.clearFiles(inputFilename, "tpt");

		// Create X_t and S
		mappingT_X = new HashMap<T, X>();
		globaltype.getTs().forEach(t -> {
			// Name convention for Promela names defined here.
			mappingT_X.put(t, new X("X representation of <" + t.getName() + ">", t.getName()));
		});

		P[] pis = new P[globaltype.r().size() + 1];

		Indices indices = new Indices();

		for (P p : piInput) {
			int role = p.r();
			if (role >= 0) {
				pis[role] = p;
				indices.add(p, role);
			}
		}

		// Create empty variable mapping
		mappingVars = new HashMap<Session, Map<Integer, Map<Variable, Variable>>>();
		return recursiveConvert(pis, globaltype, indices);
	}

	public S recursiveConvert(P[] pis, G globaltype, Indices indices) {

		if (Execute.DebugInfo) {
			debugLogInput(pis, globaltype);
			System.out.println("Starting sequentialization;");
		}

		// prepareConversion(pis, globaltype);

		// first indices of some types
		int index = -1;

		int sender = -1;
		int receiver = -1;
		if (globaltype instanceof GSend) {
			GSend gs = (GSend) globaltype;
			sender = gs.getRole1();
			receiver = gs.getRole2();
		}

		// Cases according to Tech Paper Def. 10.
		if (globaltype instanceof GTermination) { // case 1
			if (Execute.DebugInfo) {
				debugLogCase(1);
				return debugLogAndReturn(new STermination(), 1);
			}
			Execute.increaseCounter();
			return new STermination();

		} else if (globaltype instanceof GT) { // case 2
			if (Execute.DebugInfo) {
				debugLogCase(2);
				// Use T->X mapping to find recursion base.
				return debugLogAndReturn(new SX(mappingT_X.get(((GT) globaltype).getT())), 2);
			}
			Execute.increaseCounter();
			return new SX(mappingT_X.get(((GT) globaltype).getT()));

		} else if ((index = indices.getRestrictionIndex(sender, receiver)) >= 0) { // case 3
			// Index contains PRestriction element index.
			P element = ((PRestriction) pis[index]).getP();
			pis[index] = element;
			// Add modified element to index sets.
			indices.add(element, index);
			if (Execute.DebugInfo) {
				debugLogCase(3);
				return debugLogAndReturn(recursiveConvert(pis, globaltype, indices), 3);
			}
			Execute.increaseCounter();
			return recursiveConvert(pis, globaltype, indices);

		} else if ((index = indices.getParallelIndex(sender, receiver)) >= 0) { // case 4
			// Index contains PRestriction element index.
			PParallel element = (PParallel) pis[index];
			P p1 = element.getFirstP();
			P p2 = element.getSecondP();
			int role;
			// Add modified elements to index sets.
			if ((role = p1.r()) >= 0) {
				pis[role] = p1;
				indices.add(p1, role);
			}
			if ((role = p2.r()) >= 0) {
				pis[role] = p2;
				indices.add(p2, role);
			}

			if (Execute.DebugInfo) {
				debugLogCase(4);
				return debugLogAndReturn(recursiveConvert(pis, globaltype, indices), 4);
			}
			Execute.increaseCounter();
			return recursiveConvert(pis, globaltype, indices);

		} else if ((index = indices.getRecursionIndex(sender, receiver)) >= 0) { // case 5
			// Index contains PRestriction element index.
			PRecursion recElement = (PRecursion) pis[index];
			P element = recElement.getP().replacedX(recElement, recElement.getX());
			// Add modified element to index sets.
			pis[index] = element;
			indices.add(element, index);
			if (Execute.DebugInfo) {
				debugLogCase(5);
				return debugLogAndReturn(recursiveConvert(pis, globaltype, indices), 5);
			}
			Execute.increaseCounter();
			return recursiveConvert(pis, globaltype, indices);

		} else if (case6condition(pis, globaltype)) { // case 6
			GSend gs = (GSend) globaltype;
			final int r1 = gs.getRole1();
			final int r2 = gs.getRole2();
			// Type check done in condition check.
			final PSendActive pk = (PSendActive) pis[r1];
			final PSendPassive pl = (PSendPassive) pis[r2];
			// Sanity check (skipped for time measures).
			if (Execute.DebugInfo) {
				if (pk.getRole1() != r1 || pk.getRole2() != r2 || pl.getRole1() != r1 || pl.getRole2() != r2) {
					throw new RuntimeException("Global type roles do not match term roles");
				}
			}
			final P furtherPk = pk.getP();
			final Label l = pk.getLabel();
			P furtherPl = pl.getProcess(l);
			final G furtherG = gs.getGlobalType(l);
			// Rewrite process 2, create matching variable.
			List<Variable> c6variableList = pl.getVariables(l);
			Session c6session = pl.getSession();
			// Map<Variable, Variable> varReplacement = new HashMap<Variable, Variable>();

			Map<Integer, Map<Variable, Variable>> mapping2;
			Map<Variable, Variable> mapping3;
			if (mappingVars.containsKey(c6session)) {
				mapping2 = mappingVars.get(c6session);
			} else {
				mapping2 = new HashMap<Integer, Map<Variable, Variable>>();
				mappingVars.put(c6session, mapping2);
			}
			if (mapping2.containsKey(r2)) {
				mapping3 = mapping2.get(r2);
			} else {
				mapping3 = new HashMap<Variable, Variable>();
				mapping2.put(r2, mapping3);
			}
			for (Variable c6variable : c6variableList) {
				Variable newVar;
				if (mapping3.containsKey(c6variable)) {
					newVar = mapping3.get(c6variable);
				} else {
					// Name convention for Promela names defined here.
					newVar = new Variable(
							c6variable.getName() + "@" + c6session.toString() + "[" + Integer.toString(r2) + "]",
							c6variable.getName() + "_" + Integer.toString(r2));
					mapping3.put(c6variable, newVar);
				}
				// varReplacement.put(c6variable, newVar);
			}

			pis[r1] = furtherPk;
			indices.add(furtherPk, r1);
			// Obsolete. Variables are now replaced when creating SGP terms.
			// furtherPl = furtherPl.replacedVar(varReplacement);
			// furtherPl.replaceVar(varReplacement);
			pis[r2] = furtherPl;
			indices.add(furtherPl, r2);

			List<Variable> newVars = new LinkedList<Variable>(c6variableList);
			newVars.replaceAll(itm -> mapping3.get(itm)); // parallel replacement
			List<Expression> newExprs = pk.getExpressions(mapping2.get(r1));
			if (Execute.DebugInfo) {
				debugLogCase(6, newExprs);
				return debugLogAndReturn(new SAssignment(recursiveConvert(pis, furtherG, indices), newVars, newExprs),
						6, newExprs);
			}
			Execute.increaseCounter();
			return new SAssignment(recursiveConvert(pis, furtherG, indices), newVars, newExprs);

		} else if (globaltype instanceof GParallel) { // case 7
			GParallel gp = (GParallel) globaltype;
			G g1 = gp.getFirstG();
			G g2 = gp.getSecondG();
			Indices indices2 = indices.getCopy();
			// Restrict index sets to role sets.
			indices.restrict(g1.r());
			indices2.restrict(g2.r());

			// Using same array. Terms will only be used in one part.
			if (Execute.DebugInfo) {
				debugLogCase(7);
				return debugLogAndReturn(
						new SIndependent(recursiveConvert(pis, g1, indices), recursiveConvert(pis, g2, indices2)), 7);
			}
			Execute.increaseCounter();
			return new SIndependent(recursiveConvert(pis, g1, indices), recursiveConvert(pis, g2, indices2));

		} else if (globaltype instanceof GRecursion) { // case 8
			GRecursion recG = (GRecursion) globaltype;
			if (Execute.DebugInfo) {
				debugLogCase(8, mappingT_X.get(recG.getT()));
				return debugLogAndReturn(
						new SRecursion(recursiveConvert(pis, recG.getG(), indices), mappingT_X.get(recG.getT())), 8,
						mappingT_X.get(recG.getT()));
			}
			Execute.increaseCounter();
			return new SRecursion(recursiveConvert(pis, recG.getG(), indices), mappingT_X.get(recG.getT()));

		} else if ((index = indices.getSessionCreationIndex()) >= 0) { // case 9
			// index should always be 1.
			if (index != 1) {
				throw new RuntimeException("PInitiateActive has role " + index);
			}
			PInitiateActive activeP = (PInitiateActive) pis[index];
			// Store session to use it in case 10.
			session = activeP.getSession();
			P furtherP = (activeP).getP();
			pis[index] = furtherP;
			indices.add(furtherP, index);
			// Unguard PInitiatePassive terms.
			for (int i = index + 1; i < pis.length; i++) {
				furtherP = ((PInitiatePassive) pis[i]).getP();
				pis[i] = furtherP;
				indices.add(furtherP, i);
			}

			if (Execute.DebugInfo) {
				debugLogCase(9);
				return debugLogAndReturn(recursiveConvert(pis, globaltype, indices), 9); // Tau.SGP (...)
			}
			Execute.increaseCounter();
			return recursiveConvert(pis, globaltype, indices);

		} else if ((index = indices.getConditionalIndex(sender, receiver)) >= 0) { // case 10
			// Type check done in getConditionalIndex.
			PConditional element = (PConditional) pis[index];
			// Extract relevant variables.
			P pTrue = element.getProcess(true);
			P pFalse = element.getProcess(false);
			// Compute condition with replaced variables.
			Map<Integer, Map<Variable, Variable>> mapping2;
			// Keeps empty if no mapping found, overwritten otherwise.
			Map<Variable, Variable> mapping3 = new HashMap<Variable, Variable>();
			if (mappingVars.containsKey(session)) {
				mapping2 = mappingVars.get(session);
				if (mapping2.containsKey(index)) {
					mapping3 = mapping2.get(index);
				}
			}
			Condition c = element.getCondition(mapping3);
			// New list and indices for c=false
			P[] pisFalse = new P[pis.length];
			System.arraycopy(pis, 0, pisFalse, 0, pis.length);
			Indices indicesFalse = indices.getCopy();
			// Add terms to the corresponding array.
			pis[index] = pTrue;
			indices.add(pTrue, index);
			pisFalse[index] = pFalse;
			indicesFalse.add(pFalse, index);
			if (Execute.DebugInfo) {
				debugLogCase(10);
				return debugLogAndReturn(new SConditional(c, recursiveConvert(pis, globaltype, indices),
						recursiveConvert(pisFalse, globaltype, indicesFalse)), 10);
			}
			Execute.increaseCounter();
			return new SConditional(c, recursiveConvert(pis, globaltype, indices),
					recursiveConvert(pisFalse, globaltype, indicesFalse));
		}
		System.err.println("> " + globaltype);
		for (P p : pis) {
			System.err.println(p);
		}
		throw new RuntimeException("Converting Pi/G to SGP: No case matched.");
	}

	/**
	 * Checks condition for case 6 (communication). This case is deterministic.
	 * 
	 * @param ps pi calculus list
	 * @param g  global type
	 * @return condition evaluation
	 */
	private boolean case6condition(P[] pis, G g) {
		if (g instanceof GSend) {
			GSend gs = (GSend) g;
			int r1 = gs.getRole1();
			int r2 = gs.getRole2();
			return (pis[r1] != null && pis[r2] != null && pis[r1] instanceof PSendActive
					&& pis[r2] instanceof PSendPassive);
		}
		return false;
	}

	private void debugLogCase(int casenumber, Object... info) {
		System.out.print("Applied case " + casenumber + ". ");
		for (Object itm : info) {
			System.out.print("Additional info: " + itm.toString() + ". ");
		}
		System.out.println((casenumber <= 1) ? "Stopping." : "Continuing.");
	}

	private S debugLogAndReturn(S state, int casenumber, Object... info) {
		// debugLogCase(casenumber, info);
		/*
		 * System.out.println("Current sequentialization progress:");
		 * System.out.println("   " + state.toString());
		 */
		return state;
	}

	private void debugLogInput(P[] pis, G globaltype) {
		try {
			/*
			 * System.out.println("Input data:"); System.out.println("   Global type:");
			 * System.out.println("      " + globaltype.toString());
			 */
			FileExport.writeNextFile(true, inputFilename, "tpt", "Global type:\n   ", globaltype.toString(), "\n\n");
			for (int i = 0; i < pis.length; i++) {
				/*
				 * System.out.println("   Pi term " + i + ":"); System.out.println("      " +
				 * pis.get(i).toString());
				 */
				if (pis[i] != null) {
					FileExport.appendToFile(true, inputFilename, "tpt", "Pi term " + i + ":\n   ", pis[i].toString(),
							"\n\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class Indices {
		private SortedSet<Integer> restrictions = new TreeSet<Integer>();
		private SortedSet<Integer> parallels = new TreeSet<Integer>();
		private SortedSet<Integer> recursions = new TreeSet<Integer>();
		private SortedSet<Integer> conditionals = new TreeSet<Integer>();
		private SortedSet<Integer> sessionCreations = new TreeSet<Integer>();;

		public Indices() {

		}

		public Indices(SortedSet<Integer> restrictions, SortedSet<Integer> parallels, SortedSet<Integer> recursions,
				SortedSet<Integer> conditionals, SortedSet<Integer> sessionCreation) {
			this.restrictions = restrictions;
			this.parallels = parallels;
			this.recursions = recursions;
			this.conditionals = conditionals;
			this.sessionCreations = sessionCreation;
		}

		public Indices getCopy() {
			return new Indices(new TreeSet<Integer>(restrictions), new TreeSet<Integer>(parallels),
					new TreeSet<Integer>(recursions), new TreeSet<Integer>(conditionals), sessionCreations);
		}

		public void add(P pi, int role) {
			if (pi instanceof PRestriction) {
				restrictions.add(role);
				return;
			}
			if (pi instanceof PParallel) {
				parallels.add(role);
				return;
			}
			if (pi instanceof PRecursion) {
				recursions.add(role);
				return;
			}
			if (pi instanceof PConditional) {
				conditionals.add(role);
				return;
			}
			if (pi instanceof PInitiateActive) {
				sessionCreations.add(role);
				return;
			}
		}

		public void restrict(Set<Integer> roles) {
			restrictions.retainAll(roles);
			parallels.retainAll(roles);
			recursions.retainAll(roles);
			conditionals.retainAll(roles);
			sessionCreations.retainAll(roles); // = roles.contains(sessionCreation) ? sessionCreation : -1;
		}

		public int getRestrictionIndex(int sender, int receiver) {
			return getIndex(restrictions, sender, receiver);
		}

		public int getParallelIndex(int sender, int receiver) {
			return getIndex(parallels, sender, receiver);
		}

		public int getRecursionIndex(int sender, int receiver) {
			return getIndex(recursions, sender, receiver);
		}

		public int getConditionalIndex(int sender, int receiver) {
			return getIndex(conditionals, sender, receiver);
		}

		public int getSessionCreationIndex() {
			return getIndex(sessionCreations, -1, -1);
		}

		private int getIndex(SortedSet<Integer> indexSet, int sender, int receiver) {
			if (indexSet.isEmpty()) {
				return -1;
			}
			int result = -1;
			if (indexSet.contains(sender)) {
				result = sender;
			} else if (indexSet.contains(receiver)) {
				result = receiver;
			} else
				result = indexSet.first();
			indexSet.remove(result);
			return result;
		}
	}
}
