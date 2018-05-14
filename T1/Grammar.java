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
