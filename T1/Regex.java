
import java.util.*;

public class Regex {

  private String regex = "";
  private Tree t;

  public Regex(String regex) {
    this.regex = regex;
    t = Tree.convert(regex);
    System.out.println(t);
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
  private Queue<Character> expression;
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
    System.out.println(expanded);
    return new Tree(expanded);
  }
  
  private void expand() {
    Queue<Character> ex;
    ex = this.expression;
    while (!ex.isEmpty()) {
      char ch = ex.remove();
      System.out.print(ch);
      if (ch >= 'a' && ch <= 'z') {
        this.left = new Tree(this, Character.toString(ch));
      }
      else if (ch == '?') {
        Tree t = new Tree(this.father, optional);
        if (this.left != null) {
          this.left.father = t;
        }
        this.left = t;
      }
      else if (ch == '*') {
        Tree t = new Tree(this.father, closure);
        if (this.left != null) {
          this.left.father = t;
        }
        this.left = t;
      }
      else if (ch == '|') {
        Tree t = new Tree(this.father, or);
        this.father = t;
        t.left = this;
        t.right = new Tree(t, ex);
      }
      else if (ch == '.') {
        Tree t = new Tree(this.father, concat);
        this.father = t;
        t.left = this;
        t.right = new Tree(t, ex);
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
        Tree tl = new Tree(this, tmp);
        this.left = tl;
      }
    }
    
    System.out.println();
  }
  
  private Tree(String ex) {
    this.father = this;
    this.expression = new LinkedList<Character>();
    for (int i = 0; i < ex.length(); i++) {
      char ch = ex.charAt(i);
      this.expression.add(ch);
    }
//     while (ex.length() != 1)
      this.expand();
  }
  
  private Tree(Tree father, String ex) {
    this.father = father;
    this.expression = new LinkedList<Character>();
    for (int i = 0; i < ex.length(); i++) {
      char ch = ex.charAt(i);
      this.expression.add(ch);
    }
    while (ex.length() != 1)
      this.expand();
  }
  
  private Tree(Tree father, Queue<Character> ex) {
    this.father = father;
    this.expression = ex;
//     while (ex.length() != 1)
      this.expand();
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
  
  public String toString() {
    String tmp = "> ";
    tmp += this.left + "-" + expression.element() + "-" + this.right + " <";
    return tmp;
  }
}
