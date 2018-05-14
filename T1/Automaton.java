import java.util.*;

public class Automaton {
	
	public static final String epsilon = "&";
	
	LinkedList<String[]> transitions = new LinkedList<String[]>();
	LinkedList<String> states = new LinkedList<String>();
	LinkedList<String> symbols = new LinkedList<String>();
	
	String initialState = "";
	LinkedList<String> finalStates = new LinkedList<String>();

	// TODO Initial and Final States
	public Automaton() {}
	
	String getInitialState() {
		return initialState;
	}
	
	LinkedList<String> getFinalStates() {
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
	
	public String toString() {
		String ln = System.getProperty("line.separator");
		String str = "  |";
		
		for (String s:symbols) {
			str += " " + s + "|";
		}
			
		for (String s:states) {
			str += ln;
			str += s + "|";
			
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
		
		Automaton M = new Automaton();
		
		String[] r = lines[0].split("\\|");
		for (int i = 1; i < r.length; i++) {
			symbols.addLast(String.valueOf(r[i].charAt(1)));
		}
		
		for (int i = 1; i < lines.length; i++) {
			r = lines[i].split("\\|");
			
			String X = r[0];
			
			for (int j = 0; j < symbols.size(); j++) {
				String x = symbols.get(j);
				String Y = r[j+1];
				
				M.addTransition(X, x, Y);
			}
		}
		
		return M;
	}
	
}
