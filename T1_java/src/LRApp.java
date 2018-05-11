
public class LRApp {
  public static void main(String[] argv) {
    Terminal t = new Terminal('o');
    NonTerminal nt = new NonTerminal('Z');
    System.out.println("T: " + t + " NT: " + nt);
  }
}
