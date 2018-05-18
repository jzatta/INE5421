public class Regex {

  private String regex = "";
  private Tree t;

  public Regex(String regex) {
    this.regex = regex;
    t = Tree.convert(regex);
  }
  
  public Automaton getAutomaton() {
    Automaton a = new Automaton();
    return a;
  }

  public String toString() {
    return regex;
  }

}

class Tree {
  private Tree left;
  private Tree right;
  private Tree father;
  private String expression;
  private static final String or = "|";
  private static final String concat = ".";
  private static final String lambda = "#";
  private static final String closure = "*";
  private static final String optional = "?";
  
  public static Tree convert(String ex) {
    // put dots in expression
    char ch = ex.charAt(0);
    String expanded = Character.toString(ch);
    boolean nonTerminal = false;
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
      else if (ch == '?' || ch == '*' || ch == ')' || nonTerminal) {
        nonTerminal = false;
        expanded = expanded + ch;
      }
      else if (ch == '|') {
        nonTerminal = true;
        expanded = expanded + ch;
      }
      else if (ch == '(') {
        nonTerminal = true;
        expanded = expanded + "." + ch;
      }
    }
//     System.out.println(expanded);
    return new Tree(expanded);
  }
  
  private void expand() {
    String ex;
    ex = this.expression;
    int level = 0;
    for (int i = 0; i < ex.length(); i++) {
      char ch = ex.charAt(i);
      System.out.println(ch);
      if (ch >= 'a' && ch <= 'z') {
        this.left = new Tree(Character.toString(ch));
      }
      else if (ch == '?') {
        Tree t = new Tree(this, optional);
        if (this.left != null) {
          this.left.father = t;
          this.left = t;
        }
      }
      else if (ch == '*') {
        Tree t = new Tree(this, closure);
        if (this.left != null) {
          this.left.father = t;
          this.left = t;
        }
      }
      else if (ch == '|') {
        Tree tf = new Tree(this, null);
        Tree tl = new Tree(this, optional);
        
      }
    }
  }
  
  private Tree(String ex) {
    this.father = this;
    this.expression = ex;
//     while (ex.length() != 1)
      this.expand();
  }
  
  private Tree(Tree father, String ex) {
    this.father = father;
    this.expression = ex;
  }
  
  public void setLeft(Tree left) {
    this.left = left;
  }
  
  public void setRight(Tree left) {
    this.right = right;
  }
  
  public Tree getLeft() {
    return this.left;
  }
  
  public Tree getRight() {
    return this.right;
  }
  
  public Tree getFather() {
    return this.father;
  }
}
