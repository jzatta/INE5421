import java.util.*;

public class Automaton {

	public static final String epsilon = "&";

	LinkedList<String[]> transitions = new LinkedList<String[]>();
	LinkedList<String> states = new LinkedList<String>();
	LinkedList<String> symbols = new LinkedList<String>();
	LinkedList<String> enumN = new LinkedList<String>();

	String initialState = "";
	LinkedList<String> finalStates = new LinkedList<String>();

	public Automaton() {}

	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	public String getInitialState() {
		return initialState;
	}

	public void setFinalStates(LinkedList<String> finalStates) {
		this.finalStates = finalStates;
	}

	public LinkedList<String> getFinalStates() {
		return finalStates;
	}

	public LinkedList<String[]> getTransitions() {
		return transitions;
	}

	public LinkedList<String> getStates() {
		return states;
	}

	public LinkedList<String> getSymbols() {
		return symbols;
	}

	public void addTransition(String X, String x, String Y) {
		String[] transition = new String[3];

		if (!states.contains(X))
			states.addLast(X);
		if (!states.contains(Y) && !Y.equals("__"))
			states.addLast(Y);
		if (!symbols.contains(x))
			symbols.addLast(x);

		transition[0] = X;
		transition[1] = x;
		transition[2] = Y;

		transitions.addLast(transition);
	}

	public static Grammar toGrammar(Automaton M) {
		LinkedList<String[]> transitions = M.getTransitions();
		Grammar G = new Grammar();

		for (String[] t: transitions) {
			if (!t[2].equals("__")) {
				Boolean flag = false; //Verify if there is any transition starting from the next state
				String Y = t[2];
				for (String[] t2: transitions) {
					if (t2[0].equals(Y) && !t2[2].equals("__")) {
						flag = true;
						break;
					}
				}

				int index0 = Integer.valueOf(t[0].substring(1));
				int index2 = Integer.valueOf(t[2].substring(1));

				if (!M.getFinalStates().contains(t[2])) {
					t[0] = renameProduction(index0);
					t[2] = renameProduction(index2);
					G.addProduction(t[0], t[1], t[2]);
				}
				if (M.getFinalStates().contains(t[2]) && flag) {
					t[0] = renameProduction(index0);
					t[2] = renameProduction(index2);
					G.addProduction(t[0], t[1], t[2]);
					G.addProduction(t[0], t[1]);
				}
				if (M.getFinalStates().contains(t[2]) && !flag) {
					t[0] = renameProduction(index0);
					t[2] = renameProduction(index2);
					G.addProduction(t[0], t[1]);
				}
			}
		}

		return G;
	}

	private static String renameProduction(int index) {
		char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'W', 'Y', 'Z'};

		int quotesAmount = index / alphabet.length;

		String ret = String.valueOf(alphabet[index - (alphabet.length * quotesAmount)]);
		for (int i = 0; i < quotesAmount; i++)
			ret += "'";

		return ret;
	}

	private Boolean existTransition(String a, String b) {
		Boolean flag = false;
		for (String[] t: transitions) {
			if (t[0].equals(a) && t[2].equals(b)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	private static Automaton removeUnreachableStates(Automaton DFA) {
		LinkedList<String> unreachableStates = (LinkedList<String>) DFA.getStates().clone();
		Set<String> reachableStates = new HashSet<String>();
		unreachableStates.remove(DFA.getInitialState());
		reachableStates.add(DFA.getInitialState());

		LinkedList<String> toAdd = new LinkedList<String>();

		Boolean flag = false;
		while (!flag) {
			flag = true;
			toAdd.clear();

			for (String state: reachableStates) {
				for (String unState: unreachableStates) {
					if (DFA.existTransition(state, unState)) {
						toAdd.addLast(unState);
						flag = false;
					}
				}
			}

			for (String s: toAdd) {
				reachableStates.add(s);
				unreachableStates.remove(s);
			}
		}

		for (String u: unreachableStates) {
			DFA.getStates().remove(u);
		}

		return DFA;
	}

	private static Automaton removeDeadStates(Automaton DFA) {

		return DFA;
	}

	private static Automaton removeEquivalentStates(Automaton DFA) {

		return DFA;
	}

	public static Automaton minimize(Automaton DFA) {
		DFA = removeUnreachableStates(DFA);
		DFA = removeDeadStates(DFA);
		DFA = removeEquivalentStates(DFA);

		return DFA;
	}

	private static Automaton _determinize(Automaton NFA) {
		Boolean change = false;
		LinkedList<String[]> transitions = NFA.getTransitions();
		Set<String> newStatesList = new HashSet<String>();

		for (String[] t: transitions) {
			if (t[2].contains(",")) {
				change = true;

				String newState = t[2].replace(",", "");

				Set<String> unique = new HashSet<String>();
				String[] tmp = newState.split("q");

				for (int i = 1; i < tmp.length; i++) {
					unique.add("q" + tmp[i]);
				}
				tmp = unique.toArray(new String[unique.size()]);
				Arrays.sort(tmp);

				newState = "";
				for (String s: tmp) {
					newState += s;
				}

				t[2] = newState;
				newStatesList.add(newState);
			}
		}

		for (String state: newStatesList) {
			Boolean isFinal = false;
			String[] s = state.split("q");
			for (int i  = 1; i < s.length; i++) {
				if (NFA.getFinalStates().contains("q" + s[i])) {
					isFinal = true;
					break;
				}
			}
			if (isFinal) {
				LinkedList<String> nfaFinalStates = NFA.getFinalStates();
				nfaFinalStates.addLast(state);
				NFA.setFinalStates(nfaFinalStates);
			}

			for (String symbol: NFA.getSymbols()) {
				String[] tmp = state.split("q");

				String transition = "";

				for (int i = 1; i < tmp.length; i++) {
					for (String[] t: transitions) {
						if (t[0].equals("q" + tmp[i]) && t[1].equals(symbol) && !t[2].equals("__")) {
							if (!transition.isEmpty()) {
								transition += ",";
							}

							transition += t[2];
						}
					}
				}

				if (transition.isEmpty())
					break;

				Set<String> unique = new HashSet<String>();
				String[] t = transition.replace(",", "").split("q");
				for (int i = 1; i < t.length; i++) {
					unique.add(t[i]);
				}
				t = unique.toArray(new String[unique.size()]);
				Arrays.sort(t);

				transition = "";
				for (int i = 0; i < t.length; i++) {
					if (!transition.isEmpty()) {
						transition += ",";
					}

					transition += "q" + t[i];
				}

				if (transition.replace(",", "").equals(state)) {
					NFA.addTransition(state, symbol, state);
				} else if (NFA.getStates().contains(transition.replace(",", ""))) {
					int index = NFA.getStates().indexOf(transition.replace(",", ""));
					transition = NFA.getStates().get(index);
					NFA.addTransition(state, symbol, transition);
				} else {
					NFA.addTransition(state, symbol, transition);
				}
			}
		}

		if (change)
			NFA = _determinize(NFA);

		return NFA;
	}

	private static Automaton renameStates(Automaton NFA) {
		LinkedList<String> states = NFA.getStates();
		LinkedList<String[]> transitions = NFA.getTransitions();
		LinkedList<String> newFinalStates = new LinkedList<String>();

		int index = 0;

		for (int i = 0; i < states.size(); i++) {
			for (int j = 0; j < transitions.size(); j++) {
				if (transitions.get(j)[0].equals(states.get(i))) {
					String s = new String("q" + index);
					transitions.get(j)[0] = s;
				}
				if (transitions.get(j)[2].equals(states.get(i))) {
					String s = new String("q" + index);
					transitions.get(j)[2] = s;
				}
			}

			String s = new String("q" + index);

			if (NFA.getFinalStates().contains(states.get(i))) {
				newFinalStates.addLast(s);
			}

			states.set(i, s);

			index++;
		}

		NFA.setFinalStates(newFinalStates);

		return NFA;
	}

	private static Automaton removeNonDeterministicStates(Automaton NFA) {
		for (int i = 0; i < NFA.getStates().size(); i++) {
			if (NFA.getStates().get(i).contains(",")) {
				NFA.getStates().remove(i);
			}
		}

		return NFA;
	}

	public static Automaton determinize(Automaton NFA) {
		NFA = _determinize(NFA);

		NFA = removeNonDeterministicStates(NFA);
//		NFA = renameStates(NFA);

		return NFA;
	}

	public void possibleStrings(int maxLength, char[] chars, String curr) {
		if (curr.length() == maxLength)
			enumN.addLast(curr);
		else {
			for (char c: chars) {
				String oldCurr = curr;
				curr += c;
				possibleStrings(maxLength, chars, curr);
				curr = oldCurr;
			}
		}
	}

	public LinkedList<String> getEnumN(int n) {
		enumN.clear();

		char[] temp = new char[symbols.size()];
		for (int i = 0; i < symbols.size(); i++)
			temp[i] = symbols.get(i).toCharArray()[0];

		possibleStrings(n, temp, "");

		LinkedList<String> sentences = new LinkedList<String>();

		for (String s: enumN) {
			if (recognize(s))
				sentences.addLast(s);
		}

		return sentences;
	}

	public Boolean recognize(String sentence) {
		String currentState = initialState;

		char[] str = sentence.toCharArray();

		for (char c: str) {
			if (String.valueOf(c).equals("$")) break;

			Boolean error = true;
			for (String[] s: transitions) {
				if (s[0].equals(currentState) && s[1].equals(String.valueOf(c))) {
					currentState = s[2];
					error = false;
					break;
				}
			}

			if (error)
				return false;
		}

		if (finalStates.contains(currentState))
			return true;

		return false;
	}

	public String toString() {
		String ln = System.getProperty("line.separator");
		String str = "  |  |";

		for (String s:symbols) {
			str += " " + s + "|";
		}

		for (String s:states) {
			str += ln;

			if (finalStates.contains(s))
				str += "*";
			else
				str += " ";

			if (initialState.equals(s))
				str += ">";
			else
				str += " ";

			str += "|" + s + "|";

			for (String r:symbols) {
				for (String[] t:transitions) {
					if (t[0].equals(s))
						if (t[1].equals(r)) {
							if (t[2].isEmpty())
								t[2] = "__";
							str += t[2] + "|";
							break;
						}
				}
			}
		}


		return str;
	}

	public static Automaton readAutomaton(String s) {
		String[] lines = s.split(System.getProperty("line.separator"));
		LinkedList<String> symbols = new LinkedList<String>();
		LinkedList<String> finalStates = new LinkedList<String>();

		Automaton M = new Automaton();

		String[] r = lines[0].split("\\|");
		for (int i = 2; i < r.length; i++) {
			try {
				symbols.addLast(String.valueOf(r[i].charAt(1)));
			} catch (StringIndexOutOfBoundsException e) {
				symbols.addLast(String.valueOf(r[i].charAt(0)));
			}
		}

		for (int i = 1; i < lines.length; i++) {
			r = lines[i].split("\\|");

			if (String.valueOf(r[0].charAt(0)).equals("*"))
				finalStates.addLast(r[1]);

			if (String.valueOf(r[0].charAt(1)).equals(">"))
				M.setInitialState(r[1]);

			String X = r[1];

			for (int j = 0; j < symbols.size(); j++) {
				String x = symbols.get(j);
				String Y = r[j+2];

				M.addTransition(X, x, Y);
			}
		}

		M.setFinalStates(finalStates);

		return M;
	}

}
