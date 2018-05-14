import java.util.*;

public class Automaton {

	public static final String epsilon = "&";

	LinkedList<String[]> transitions = new LinkedList<String[]>();
	LinkedList<String> states = new LinkedList<String>();
	LinkedList<String> symbols = new LinkedList<String>();

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
		if (!states.contains(Y))
			states.addLast(Y);
		if (!symbols.contains(x))
			symbols.addLast(x);

		transition[0] = X;
		transition[1] = x;
		transition[2] = Y;

		transitions.addLast(transition);
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
			symbols.addLast(String.valueOf(r[i].charAt(1)));
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
