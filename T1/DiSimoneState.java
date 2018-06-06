
import java.util.*;

public  class DiSimoneState {
  private boolean hasLambda;
  private int state;
  private Map<Character, Set<Tree>> composition;
  private Map<Character, DiSimoneState> transitions;
  private static int stateCount;
  private static Set<Character> terminals;
  private static List<DiSimoneState> toEval;
  private static Set<DiSimoneState> cleared;
  
  // Cria uma instancia do Di Simone, inicializando as estruturas usadas.
  public static DiSimoneState newDiSimone() {
    stateCount = 0;
    terminals = new HashSet<Character>();
    toEval = new LinkedList<DiSimoneState>();
    cleared = new HashSet<DiSimoneState>();
    return new DiSimoneState();
  }
  
  // Constroi um objeto que representa um estado do Di Simone.
  private DiSimoneState() {
    composition = new HashMap<Character, Set<Tree>>();
    transitions = new HashMap<Character, DiSimoneState>();
    hasLambda = false;
    state = stateCount++;
  }
  
  // Deriva todos os outros estados a partir do estado inicial
  public void buildStates() {
    toEval.add(this);
    while (toEval.size() > 0) {
      DiSimoneState thisState = toEval.get(0);
      toEval.remove(thisState);
      Set<Character> keys = thisState.composition.keySet();
//       System.out.println(thisState.state + ":" + keys);
      for (char k: keys) {
        terminals.add(k);
        Set<Tree> comp = thisState.composition.get(k);
        DiSimoneState s = new DiSimoneState();
        for (Tree t: comp) {
          t.resetVisited();
          t.getUp(s);
        }
        boolean equal = false;
        for (DiSimoneState ds: cleared) {
          if (s.isEquivalent(ds)) {
            s = ds;
            equal = true;
            break;
          }
        }
        if (!equal) {
          for (DiSimoneState ds: toEval) {
            if (s.isEquivalent(ds)) {
              s = ds;
              equal = true;
              break;
            }
          }
        }
        if (equal) {
//           System.out.println(thisState.state + ":EQ:" + s.state);
        } else {
//           System.out.println(thisState.state + ":NE:" + s.state);
          toEval.add(s);
        }
//         System.out.println(thisState.state + "--" + k + "-->" + s.state);
        thisState.transitions.put(k, s);
      }
      cleared.add(thisState);
    }
  }
  
  // Define estado como final se encontrar um lambda nas composicoes
  public void setLambda() {
    hasLambda = true;
  }
  
  // Adiciona uma composicao na lista de composiscoes do estado
  void insertComp(Character ch, Tree terminal) {
    Set<Tree> t = composition.get(ch);
    if (t == null) {
      t = new HashSet<Tree>();
      composition.put(ch, t);
    }
    t.add(terminal);
  }
  
  // Verifica se as composicoes sao iguais, para definir como estado equivalente
  boolean isEquivalent(DiSimoneState s) {
    if (this.hasLambda != s.hasLambda) {
      return false;
    }
    Set<Character> k1 = this.composition.keySet();
    Set<Character> k2 =    s.composition.keySet();
    if (k1 == k2) {
//       System.out.println("k" + this.state + " == k" + s.state);
      return true;
    }
    if (k1 == null) {
//       System.out.println("k" + this.state + " != k" + s.state);
      return false;
    }
    if (!(k1.containsAll(k2) && k2.containsAll(k1))) {
//       System.out.println("k" + this.state + " != k" + s.state);
      return false;
    }
    for (Character k: k1) {
      Set<Tree> c1 = this.composition.get(k);
      Set<Tree> c2 =    s.composition.get(k);
      if (!(c1.containsAll(c2) && c2.containsAll(c1))) {
//         System.out.println("k" + this.state + " != k" + s.state);
        return false;
      }
    }
//     System.out.println("k" + this.state + " == k" + s.state);
    return true;
  }
  
  // Gera o automato a partir dos estados criados no De Simone
  public Automaton getAutomaton() {
    Automaton DFA = new Automaton();
    
    LinkedList<String> finals = new LinkedList<String>();
    
    for (DiSimoneState s: cleared) {
      for (Character t: terminals) {
        DiSimoneState to = s.transitions.get(t);
        if (to == null) {
          DFA.addTransition("q" + s.state, Character.toString(t), "__");
        } else {
          DFA.addTransition("q" + s.state, Character.toString(t), "q" + to.state);
        }
      }
      if (s.state == 0) {
        DFA.setInitialState("q" + s.state);
      }
      if (s.hasLambda) {
        finals.add("q" + s.state);
      }
    }
    DFA.setFinalStates(finals);
    return DFA;
  }
  
  // Gera dados legiveis para humanos, usado para conferir a geracao de estados
  public static String getReadable() {
    String s = "s  |";
    for (Character t: terminals) {
      s += "  " + t + "  |";
    }
    s += "  Comp\n";
    for (DiSimoneState n: cleared) {
      if (n.state == 0) {
        s += ">";
      } else {
        s += " ";
      }
      if (n.hasLambda) {
        s += "*";
      } else {
        s += " ";
      }
      s += n.state + "|";
      for (Character t: terminals) {
        DiSimoneState a = n.transitions.get(t);
        s += "  " + ((a == null)?"-":a.state) + "  |";
      }
      for (Character t: n.composition.keySet()) {
        for (Tree a: n.composition.get(t)) {
          s += a.hashCode() + "-" + a.getChar() + ",";
        }
      }
      if (n.hasLambda)
        s += "#";
      s += "\n";
    }
    return s;
  }
}
