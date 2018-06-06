import java.util.*;

public class Automaton {

	public static final String epsilon = "&";

	LinkedList<String[]> transitions = new LinkedList<String[]>();
	LinkedList<String> states = new LinkedList<String>();
	LinkedList<String> symbols = new LinkedList<String>();
	LinkedList<String> enumN = new LinkedList<String>();

	String initialState = "";
	LinkedList<String> finalStates = new LinkedList<String>();

  // Apenas cria um objeto automato
	public Automaton() {}

	// define o estado inicial do automato
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	// Retorna qual o estado inicial do automato
	public String getInitialState() {
		return initialState;
	}

	// Define o conjunto de estados finais do automato
	public void setFinalStates(LinkedList<String> finalStates) {
		this.finalStates = finalStates;
	}

	// Retorna lista de estados que sao finais
	public LinkedList<String> getFinalStates() {
		return finalStates;
	}

	// Retorna lista de transicoes
	public LinkedList<String[]> getTransitions() {
		return transitions;
	}

	// Retorna uma lista com todos os estados do automato
	public LinkedList<String> getStates() {
		return states;
	}

	// Retorna uma lista com o alfabeto do automato
	public LinkedList<String> getSymbols() {
		return symbols;
	}

	// Adiciona uma transicao ao automato, do tipo d(X, x) = Y
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

	// Converte o automato para uma gramatica e retorna
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

	//  Renomeia as produções
	private static String renameProduction(int index) {
		char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'W', 'Y', 'Z'};

		int quotesAmount = index / alphabet.length;

		String ret = String.valueOf(alphabet[index - (alphabet.length * quotesAmount)]);
		for (int i = 0; i < quotesAmount; i++)
			ret += "'";

		return ret;
	}

	// Verifica se existe uma transição
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

	// Verifica o destino de uma transição
	private String transitionTo(String stateFrom, String symbol) {
		for (String[] t: transitions) {
			if (t[0].equals(stateFrom) && t[1].equals(symbol)) {
				return t[2];
			}
		}

		return null;
	}

	// Remove estados inalcançáveis
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

	// Remove estados mortos
	@SuppressWarnings("unchecked")
	private static Automaton removeDeadStates(Automaton DFA) {
		LinkedList<String> deadStates = (LinkedList<String>) DFA.getStates().clone();
		Set<String> aliveStates = new HashSet<String>();
		for (String s: DFA.getFinalStates()) {
			deadStates.remove(s);
			aliveStates.add(s);
		}

		LinkedList<String> toAdd = new LinkedList<String>();

		Boolean flag = false;
		while (!flag) {
			flag = true;
			toAdd.clear();

			for (String alive: aliveStates) {
				for (String dead: deadStates) {
					if (DFA.existTransition(dead, alive)) {
						toAdd.addLast(dead);
						flag = false;
					}
				}
			}

			for (String s: toAdd) {
				aliveStates.add(s);
				deadStates.remove(s);
			}
		}

		for (String d: deadStates) {
			DFA.getStates().remove(d);

			for (String[] t: DFA.getTransitions()) {
				if (t[2].equals(d)) {
					t[2] = "__";
				}
			}
		}

		return DFA;
	}

	// Remove estados equivalentes
	private static Automaton removeEquivalentStates(Automaton DFA) {
		LinkedList<String[]> pairs = new LinkedList<String[]>();
		LinkedList<String[]> analysis = new LinkedList<String[]>();

		int index = 1;
		for (int i = 0; i < DFA.getStates().size(); i++) {
			for (int j = index; j < DFA.getStates().size(); j++) {
				String[] tmp = {DFA.getStates().get(i), DFA.getStates().get(j), "0"};
				pairs.addLast(tmp);
			}
			index++;
		}

		for (String[] P: pairs) {
			if (DFA.getFinalStates().contains(P[0]) ^ DFA.getFinalStates().contains(P[1])) {
				P[2] = "1";
			}
		}

		for (String[] P: pairs) {
			for (String s: DFA.getSymbols()) {
				String t1 = DFA.transitionTo(P[0], s);
				String t2 = DFA.transitionTo(P[1], s);

				if (t1.equals(t2)) {
					break;
				} else {
					String[] pair = null;
					for (String[] p: pairs) {
						if (p[0].equals(t1) && p[1].equals(t2) || p[0].equals(t2) && p[1].equals(t1)) {
							pair = p;
							break;
						}
					}

					if (t1.equals("__")) {
						break;
					}
					if (t2.equals("__")) {
						break;
					}

					if (pair[2].equals("0")) {
						String[] tmp = {P[0], P[1], t1, t2};
						analysis.addLast(tmp);
					} else {
						P[2] = "1";
						for (String[] p: analysis) {
							if (p[0].equals(P[0]) && p[1].equals(P[1])) {
								for (String[] p2: pairs) {
									if (p2[0].equals(p[2]) && p2[1].equals(p[3])) {
										p2[2] = "1";
									}
								}
							}
						}
						break;
					}
				}


			}
		}

		analysis.clear();
		for (String[] P: pairs) {
			if (P[2].equals("0")) {
				for (String s: DFA.getSymbols()) {
					String t1 = DFA.transitionTo(P[0], s);
					String t2 = DFA.transitionTo(P[1], s);

					if (!t1.equals(t2)) {
						for (String[] p: pairs) {
							if (p[0].equals(t1) && p[1].equals(t2) || p[0].equals(t2) && p[1].equals(t1)) {
								if (p[2].equals("0")) {
									String[] tmp = {t1, t2, P[0], P[1]};
									analysis.addLast(tmp);
								} else {
									P[2] = "1";
									for (String[] p2: analysis) {
										if (p2[0].equals(P[0]) && p2[1].equals(P[1]) || p2[0].equals(P[1]) && p2[1].equals(P[0])) {
											for (String[] p3: pairs) {
												if (p3[0].equals(p2[2]) && p3[1].equals(p2[3]) || p3[0].equals(p2[3]) && p3[1].equals(p2[2])) {
													p3[2] = "1";
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		for (String[] P: pairs) {
			if (P[2].equals("0")) {

				for (String[] t: DFA.getTransitions()) {
					if (t[2].equals(P[1])) {
						t[2] = P[0];
					}
				}

				DFA.getStates().remove(P[1]);
			}
		}

		return DFA;
	}

	// Minimiza
	public static Automaton minimize(Automaton DFA) {
		DFA = removeUnreachableStates(DFA);
		DFA = removeDeadStates(DFA);
		DFA = removeEquivalentStates(DFA);

		return DFA;
	}

	// Determiniza
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

	// Renomeia os estados
	private static Automaton renameStates(Automaton NFA) {
		LinkedList<String> toRename = new LinkedList<String>();
		int index = NFA.getStates().size();

		LinkedList<String> finalStates = NFA.getFinalStates();

		for (String state: NFA.getStates()) {
			if (state.split("q").length > 2) {
				toRename.addLast(state);
			}
		}

		for (String state: toRename) {
			while (NFA.getStates().contains("q" + index)) {
				index++;
			}

			for (String[] t: NFA.getTransitions()) {
				if (t[0].equals(state)) {
					t[0] = "q" + index;
				}
				if (t[2].equals(state)) {
					t[2] = "q" + index;
				}
			}

			finalStates.remove(state);
			finalStates.addLast("q" + index);

			NFA.getStates().set(NFA.getStates().indexOf(state), "q" + index);
		}

		NFA.setFinalStates(finalStates);

		return NFA;
	}

	// Remove estados não-determinísticos
	private static Automaton removeNonDeterministicStates(Automaton NFA) {
		for (int i = 0; i < NFA.getStates().size(); i++) {
			if (NFA.getStates().get(i).contains(",")) {
				NFA.getStates().remove(i);
			}
		}

		return NFA;
	}

	// Determiniza de forma recursiva
	public static Automaton determinize(Automaton NFA) {
		NFA = _determinize(NFA);

		NFA = removeNonDeterministicStates(NFA);
		NFA = renameStates(NFA);

		return NFA;
	}

	// Possíveis sentenças de tamanho n
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

	// Enumera sentenças de tamanho n
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

	// Reconhece uma sentença
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

  // Converte para String
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

	// Lê um autômato
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
