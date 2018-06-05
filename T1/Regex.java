
import java.util.*;

public class Regex {

  private String regex;
  private Tree t;
  private Automaton a;

  // Intancia um objeto para expressão regular, passando uma string com a expressao.
  public Regex(String regex) {
    this.regex = regex;
    t = null;
    a = null;
  }
  
  // Gera e retorna o automato referente a expressao.
  public Automaton getAutomaton() {
    if (t == null) {
      t = Tree.convert(regex);
//       System.out.println(t);
      if (a == null) {
        a = t.getAutomaton();
      }
    }
    return a;
  }

  // retorna a expressao.
  public String toString() {
    return regex;
  }

}

class Tree {
  private Tree left;
  private Tree seam;
  private Tree right;
  private Tree father;
  private Queue<Character> expression;
  private boolean isTerminal;
  private static final String or = "|";
  private static final String concat = ".";
  private static final String lambda = "#";
  private static final String closure = "*";
  private static final String optional = "?";
  
  // Gera uma arvore para o De Simoni, explicitando as concatenacoes.
  public static Tree convert(String ex) {
    // put dots in expression
    char ch = ex.charAt(0);
    String expanded = Character.toString(ch);
    boolean nonTerminal = false;
    totalTerminal = 1;
    if (ch == '(') {
      nonTerminal = true;
    }
    for (int i = 1; i < ex.length(); i++) {
      ch = ex.charAt(i);
      if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <='9')) {
        if (!nonTerminal)
          expanded = expanded + ".";
        nonTerminal = false;
        expanded = expanded + ch;
      }
      else if (ch == '?' || ch == '*' || ch == ')') {
        nonTerminal = false;
        expanded = expanded + ch;
      }
      else if (ch == '|') {
        nonTerminal = true;
        expanded = expanded + ch;
      }
      else if (ch == '(') {
        if (!nonTerminal)
          expanded = expanded + ".";
        nonTerminal = true;
        expanded = expanded + ch;
      }
    }
    return new Tree(expanded);
  }
  
  // Retorna o caractere referente ao nodo da arvore
  public char getChar() {
    return this.expression.peek();
  }
  
  private static Set<Tree> visited;
  private List<DiSimoneState> states;
  private Automaton DFA;
  
  // Gera o automato a partir da arvore
  public Automaton getAutomaton() {
    if (DFA != null)
      return DFA;
    visited = new HashSet<Tree>();
    
    // Add root Node, the initial state
    DiSimoneState state = DiSimoneState.newDiSimone();
    
    // writes compositions to Di Simone initial state
    this.getDown(state);
    // build other states, based in compositions of initial state
    state.buildStates();
//     System.out.println(DiSimoneState.getReadable());
//     System.out.println("");
//     System.out.println("");
    
    DFA = state.getAutomaton();
//     System.out.println(DFA);
//     System.out.println("");
//     System.out.println("");
    return DFA;
  }
  
  // reinicia os estados visitados para percorrer a arvore
  public void resetVisited() {
    visited = new HashSet<Tree>();
  }
  
  // executa um passo de descida na arvore
  public void getDown(DiSimoneState state) {
    if (visited.contains(this)) {
      return;
    }
    visited.add(this);
    char ch = this.expression.peek();
    switch (ch) {
      case '?':
        left.getDown(state);
        visited.remove(seam);
        seam.getUp(state);
        break;
      case '*':
        left.getDown(state);
        visited.remove(seam);
        seam.getUp(state);
      break;
      case '.':
        left.getDown(state);
      break;
      case '|':
        left.getDown(state);
        right.getDown(state);
      break;
      default:
      // Terminal
        state.insertComp(ch, this);
      break;
    }
  }
  
  // executa um passo de subida na arvore
  public void getUp(DiSimoneState state) {
    if (visited.contains(this)) {
      return;
    }
    visited.add(this);
    char ch = this.expression.peek();
    switch (ch) {
      case '?':
        seam.getUp(state);
      break;
      case '*':
        left.getDown(state);
        visited.remove(seam);
        seam.getUp(state);
        break;
      case '.':
        right.getDown(state);
        break;
      case '|':
        Tree nd = this;
        while (nd.seam == null) {
          nd = nd.right;
        }
        visited.remove(nd);
        nd.getUp(state);
        break;
      case '#':
        // Lambda
        state.setLambda();
        break;
      default:
        // Terminal
        seam.getUp(state);
        break;
    }
  }
  
  // Expande a arvore para que fique apenas um simbolo em cada nodo
  private void expand() {
    this.solveOr();
    this.solveConcatClosureOptional();
  }
  
  // expande as operacoes pois tem menor precedencia
  private void solveOr() {
    Queue<Character> ex;
    Queue<Character> left;
    left = new LinkedList<Character>();
    ex = this.expression;
    while (ex.size() > 1) {
      char ch = ex.remove();
      if (ch == '|') {
        this.expression = new LinkedList<Character>();
        this.expression.add(or.charAt(0));
        this.left = new Tree(this, left);
        this.right = new Tree(this, ex);
        left = new LinkedList<Character>();
        ex = this.expression;
      }
      else if (ch == '(') {
        int level;
        level = 1;
        left.add(ch);
        while (level != 0) {
          ch = ex.remove();
          left.add(ch);
          if (ch == '(') {
            level++;
          }
          else if (ch == ')') {
            level--;
            if (level == 0)
              break;
          }
        }
      } else {
        left.add(ch);
      }
    }
    if (left.size() > 0) {
      left.add(ex.remove());
      this.expression = left;
    }
  }
  
  // expande as outras operacoes com maior precedencia e parenteses
  private void solveConcatClosureOptional() {
    Queue<Character> ex;
    ex = this.expression;
    while (ex.size() > 1) {
      char ch = ex.remove();
      if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') ) {
        this.left = new Tree(this, Character.toString(ch));
      }
      else if (ch == '?') {
        Tree t = new Tree(this, optional);
        t.left = this.left;
        if (this.left != null) {
          this.left.father = t;
        }
        this.left = t;
      }
      else if (ch == '*') {
        Tree t = new Tree(this, closure);
        t.left = this.left;
        if (this.left != null) {
          this.left.father = t;
        }
        this.left = t;
      }
      else if (ch == '.') {
        this.expression = new LinkedList<Character>();
        this.expression.add(concat.charAt(0));
        this.right = new Tree(this, ex);
        ex = this.expression;
      }
      else if (ch == '(') {
        int level;
        Queue tmp = new LinkedList<Character>();
        level = 1;
        while (level != 0) {
          ch = ex.remove();
          if (ch == '(') {
            level++;
          }
          else if (ch == ')') {
            level--;
            if (level == 0)
              break;
          }
          tmp.add(ch);
        }
        if (ex.size() > 0) {
          Tree tl = new Tree(this, tmp);
          this.left = tl;
        } else {
          ex = tmp;
        }
      }
    }
  }
  
  // Retorna o nodo para qual deve ser feita a costura, null caso nao tenha
  private Tree getImediateUpperRight() {
    Tree nodeS = this;
    Tree nodeF = nodeS.father;
    while (nodeF != nodeS) {
      if (nodeS == nodeF.left) {
        return nodeF;
      } else {
        nodeS = nodeF;
        nodeF = nodeS.father;
      }
    }
    return null;
  }
  
  // costura a arvore e cria o 'nodo lambda'
  private void sew() {
    if (this.left != null)
      this.left.sew();
    char ch = this.expression.peek();
    if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '*' || ch == '?') {
      Tree s = this.getImediateUpperRight();
      if (s == null) {
        this.seam = new Tree(lambda);
      } else {
        this.seam = s;
      }
    }
    if (this.right != null)
      this.right.sew();
  }
  
  private int countTerminal;
  private static int totalTerminal = 1;
  
  // Retorna um inteiro referente a enumeracao dos nodos finais
  public int hashCode() {
    return countTerminal;
  }
  
  // Instancia o nodo raiz da arvore, recebendo a expressao expandida
  private Tree(String ex) {
    this.father = this;
    this.expression = new LinkedList<Character>();
    for (int i = 0; i < ex.length(); i++) {
      char ch = ex.charAt(i);
      this.expression.add(ch);
    }
    if (ex.length() != 1) {
      this.isTerminal = false;
      this.expand();
    } else {
      char ch = ex.charAt(0);
      if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <='9') || ch == lambda.charAt(0)) {
        this.isTerminal = true;
        countTerminal = totalTerminal++;
      } else {
        this.isTerminal = false;
      }
    }
    this.sew();
  }
  
  // Instancia um nodo filho para a arvore, recebendo uma expressaoexpandida
  private Tree(Tree father, String ex) {
    this.father = father;
    this.expression = new LinkedList<Character>();
    for (int i = 0; i < ex.length(); i++) {
      char ch = ex.charAt(i);
      this.expression.add(ch);
    }
    if (ex.length() != 1) {
      this.isTerminal = false;
      this.expand();
    } else {
      char ch = ex.charAt(0);
      if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <='9') || ch == lambda.charAt(0)) {
        this.isTerminal = true;
        countTerminal = totalTerminal++;
      } else {
        this.isTerminal = false;
      }
    }
  }
  
  // Instancia um nodo filho para a arvore, recebendo uma lista de caracteres
  private Tree(Tree father, Queue<Character> ex) {
    this.father = father;
    this.expression = ex;
    if (ex.size() != 1) {
      this.isTerminal = false;
      this.expand();
    } else {
      char ch = ex.peek();
      if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <='9') || ch == lambda.charAt(0)) {
        this.isTerminal = true;
        countTerminal = totalTerminal++;
      } else {
        this.isTerminal = false;
      }
    }
  }
  
  // Retora uma string legivel para humanos da arvore para debug
  public String toString() {
    String tmp = "";
    char ch = expression.peek();
    if (true) {
      if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <='9')) {
        tmp = "" + ch + "{/\\" + this.seam.expression.peek() + "}";
      }
      else if (ch == '?' || ch == '*') {
        tmp = "(" + this.left + ")" + ch + "{/\\" + this.seam.expression.peek() + "}";
      }
      else if (ch == '|') {
        tmp = "(" + this.left + ch + this.right + ")";
      }
      else if (ch == '.') {
        tmp = "" + this.left + this.right;
      }
    }
    else {
      if (this.left == null && this.right == null) {
        tmp = "" + expression.peek();
      } else {
        tmp = "(";
        tmp += this.left + "<" + expression.peek() + ">" + this.right + ")";
      }
    }
    return tmp;
  }
}
