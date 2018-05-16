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

	public static Automaton determinize(Automaton NFA) {
		Boolean pass = false;
		LinkedList<String[]> mT = NFA.getTransitions();
		LinkedList<String[]> originalT = new LinkedList<String[]>();

		String[] aa = new String[3];
		aa[0] = "q0"; aa[1] = "0"; aa[2] = "q1";
		originalT.addLast(aa);
		String[] bb = new String[3];
		bb[0] = "q0"; bb[1] = "1"; bb[2] = "q0";
		originalT.addLast(bb);
		String[] cc = new String[3];
		cc[0] = "q1"; cc[1] = "0"; cc[2] = "q2";
		originalT.addLast(cc);
		String[] dd = new String[3];
		dd[0] = "q1"; dd[1] = "1"; dd[2] = "q1";
		originalT.addLast(dd);
		String[] ee = new String[3];
		ee[0] = "q2"; ee[1] = "0"; ee[2] = "q1";
		originalT.addLast(ee);
		String[] ff = new String[3];
		ff[0] = "q2"; ff[1] = "1"; ff[2] = "q0";
		originalT.addLast(ff);

		int index = -1;

		while (!pass) {
			pass = true;
			index = -1;

			for (String[] t: mT) {
				index++;
				if (t[2].contains(",")) {
					pass = false;
					break;
				}
			}

			if (pass)
				break;

//			String newState = "q" + NFA.getStates().size();

			Set<String> previousStates = new HashSet<String>();
			for (String s: mT.get(index)[2].split(",")) {
				previousStates.add(s);
			}
			if (previousStates.size() == 1)
				mT.get(index)[2] = mT.get(index)[2].split(",")[0];
			else {
				String newState = "";
				for (String p: previousStates)
					newState += p;

				// Update final states
				Boolean isFinal = false;
				for (String p: previousStates) {
					if (NFA.getFinalStates().contains(p)) {
						isFinal = true;
						break;
					}
				}
				if (isFinal)
					NFA.getFinalStates().addLast(newState);

				mT.get(index)[2] = newState;

				for (String s: NFA.getSymbols()) {
					Set<String> transitionSet = new HashSet<String>();
					String transition = "";

					for (String p: previousStates) {
						for (String[] t: originalT) {
							if (t[0].equals(p) && t[1].equals(s)) {
								if (!t[2].equals("__")) {
									transitionSet.add(t[2]);
								}

								break;
							}
						}
					}

					// Remove unreachable states
					String[] toRemove = new String[NFA.getStates().size()];
					index = -1;
					for (String p: previousStates) {
						index++;
						Boolean flag = true;
						for (String[] t: mT) {
							if (t[2].equals(p)) {
								flag = false;
								break;
							}
						}
						if (flag)
							toRemove[index] = p;
					}
					for (String s2: toRemove) {
						NFA.getStates().remove(s2);
					}

					if (transitionSet.containsAll(previousStates))
						NFA.addTransition(newState, s, newState);
					else {
						transition = transitionSet.iterator().next();
						Boolean flag = false;
						if (transitionSet.size() > 1)
							for (String t: transitionSet) {
								if (flag)
									transition += "," + t;
								else
									flag = true;
							}

						for (String state: NFA.getStates()) {
							if (state.equals(transition.replace(",", ""))) {
								transition = transition.replace(",", "");
								break;
							}
						}

						NFA.addTransition(newState, s, transition);
					}

				}
			}
		}

		// Remove non-deterministic states
		String[] toRemove = new String[NFA.getStates().size()];
		index = -1;
		for (String s: NFA.getStates()) {
			index++;
			if (s.contains(","))
				toRemove[index] = s;
		}
		for (String s: toRemove) {
			NFA.getStates().remove(s);
		}

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
