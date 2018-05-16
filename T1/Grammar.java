import java.util.*;

public class Grammar {

  public static final String epsilon = "&";

  LinkedList<String[]> productions = new LinkedList<String[]>();
  LinkedList<String> nonTerminalSymbols = new LinkedList<String>();

  public Grammar() {}

  public LinkedList<String[]> getProductions() {
    return productions;
  }

  public LinkedList<String> getNonTerminalSymbols() {
    return nonTerminalSymbols;
  }

  // S->a
  public void addProduction(String X, String x) {
    addProduction(X, x, epsilon);
  }

  // S->aS
  public void addProduction(String X, String x, String Y) {
    if (!nonTerminalSymbols.contains(X))
      nonTerminalSymbols.addLast(X);

    String[] production = new String[3];

    production[0] = X;
    production[1] = x;
    production[2] = Y;

    productions.addLast(production);
  }

  public static Automaton toNFA(Grammar G) {
    LinkedList<String[]> gProductions = G.getProductions();
    LinkedList<String> gNonTerminals = G.getNonTerminalSymbols();
    LinkedList<String> nfaStates = new LinkedList<String>();
    LinkedList<String> nfaFinalStates = new LinkedList<String>();
    Automaton M = new Automaton();
    int index = 0;

    for (String n: gNonTerminals) {
      nfaStates.addLast("q" + index);
      index++;
    }

    M.setInitialState(nfaStates.getFirst());

    String lastX = "";
    String lastx = "";
    String[] lastT = new String[3];

    for (String[] p: gProductions) {
      String X = nfaStates.get(gNonTerminals.indexOf(p[0]));
      String x = p[1];
      String Y = "";

      if (x.equals(Grammar.epsilon) && X.equals(M.getInitialState())) {
    	  nfaFinalStates.addLast(M.getInitialState());
      } else {
    	  if (p[2].equals(Grammar.epsilon)) {
    	        Y = "q" + index;
    	        nfaFinalStates.addLast(Y);
    	        index++;
    	      } else {
    	        Y = nfaStates.get(gNonTerminals.indexOf(p[2]));
    	      }

    	      if (X.equals(lastX) && x.equals(lastx)) {
    	    	  M.getTransitions().get(M.getTransitions().indexOf(lastT))[2] += "," + Y;
    	    	  M.addTransition(X, x, Y);
    	      } else {
    	    	  M.addTransition(X, x, Y);
    	    	  lastT = M.getTransitions().getLast();
    	      }

    	      lastX = X;
    	      lastx = x;
      }
    }

    LinkedList<String[]> toAdd = new LinkedList<String[]>();
    for (String s: M.getSymbols()) {
    	for (String state: M.getStates()) {
    		Boolean exists = false;
    		for (String[] t: M.getTransitions()) {
    			if (t[0].equals(state) && t[1].equals(s)) {
    				exists = true;
    				break;
    			}
    		}
    		if (!exists) {
    			String[] t = {state, s};
    			toAdd.addLast(t);
    		}
    	}
    }
    for (String[] t: toAdd) {
    	M.addTransition(t[0], t[1], "__");
    }

    M.setFinalStates(nfaFinalStates);

    return M;
  }

  public String toString() {
    String ln = System.getProperty("line.separator");
    String str = "";

    for (String s:nonTerminalSymbols) {
      String temp = s + "->";
      Boolean grepFlag = false;

      for (String[] r:productions) {
        if (r[0].equals(s)) {
          if (grepFlag)
            temp += "|";

          temp += r[1];

          if (!r[2].equals(epsilon))
            temp += r[2];

          grepFlag = true;
        }
      }

      str += temp + ln;
    }

    return str;
  }

  public static Grammar readGrammar(String s) {
    String[] lines = s.split(System.getProperty("line.separator"));
    LinkedList<String> prodList = new LinkedList<String>();

    for (String l:lines) {
      String S = String.valueOf(l.charAt(0));

      String sub = l.split(">")[1];
      String prods[] = sub.split("\\|");

      for (String j:prods) {
        String temp = S;
        temp += String.valueOf(j.charAt(0));

        if (j.length() > 1)
          temp += String.valueOf(j.charAt(1));

        prodList.addLast(temp);
      }
    }

    Grammar G = new Grammar();

    for (String l:prodList) {
      String s0 = String.valueOf(l.charAt(0));
      String s1 = String.valueOf(l.charAt(1));

      if (l.length() == 3)
        G.addProduction(s0, s1, String.valueOf(l.charAt(2)));
      else
        G.addProduction(s0, s1);
    }

    return G;
  }

}
