import java.util.*;
import java.io.*;
import javax.swing.*;

public class Utils {

  public static void saveToDisk(String s) {
    JFileChooser save = new JFileChooser();
    save.showSaveDialog(null);
    File file = new File(save.getSelectedFile().getAbsolutePath());

    try {
      char buffer[] = new char[s.length()];
      s.getChars(0, s.length(), buffer, 0);

      FileWriter f = new FileWriter(file);

      for (int i = 0; i < buffer.length; i++) {
        f.write(buffer[i]);
      }

      f.close();
    } catch (Exception e) {}
  }

  public static String loadFromDisk() {
    JFileChooser open = new JFileChooser();
    open.showOpenDialog(null);
    File file = new File(open.getSelectedFile().getAbsolutePath());

    String ret = "";

    try {
      JTextArea tmp = new JTextArea();
      tmp.read(new FileReader(file.getAbsolutePath()), null);
      ret = tmp.getText();
    } catch (IOException e) {}

    return ret;
  }

  public static Grammar kleeneClosure(Grammar G1) {
	LinkedList<String[]> productionsG1 = G1.getProductions();

    Grammar G = new Grammar();

    for (String[] s:productionsG1) {
        G.addProduction(s[0], s[1], s[2]);

        if (s[2].equals(Grammar.epsilon))
          G.addProduction(s[0], s[1], productionsG1.getFirst()[0]);
    }

    return G;
  }

  public static Grammar grammarConcatenation(Grammar G1, Grammar G2) {
    LinkedList<String> nonTerminalSymbolsG2 = G2.getNonTerminalSymbols();

    LinkedList<String[]> productionsG1 = G1.getProductions();
    LinkedList<String[]> productionsG2 = G2.getProductions();

    Grammar G = new Grammar();

    // Rename G2 nonTerminalSymbols
    for (String s: nonTerminalSymbolsG2) {
      String X = s + "'";

      for (String[] r:productionsG2) {
        if (r[0].equals(s))
          r[0] = X;

        if (r[2].equals(s))
          r[2] = X;
      }
    }

    // Insert G1 & G2 Productions
    for (String[] s:productionsG1) {
      if (s[2].equals(Grammar.epsilon))
        G.addProduction(s[0], s[1], productionsG2.getFirst()[0]);
      else
        G.addProduction(s[0], s[1], s[2]);
    }
    for (String[] s:productionsG2) {
        G.addProduction(s[0], s[1], s[2]);
    }

    return G;
  }

  public static Grammar grammarUnion(Grammar G1, Grammar G2) {
    LinkedList<String> nonTerminalSymbolsG2 = G2.getNonTerminalSymbols();

    LinkedList<String[]> productionsG1 = G1.getProductions();
    LinkedList<String[]> productionsG2 = G2.getProductions();

    Grammar G = new Grammar();

    String initial = "U";

    // Rename G2 nonTerminalSymbols
    for (String s: nonTerminalSymbolsG2) {
      String X = s + "'";

      for (String[] r:productionsG2) {
        if (r[0].equals(s))
          r[0] = X;

        if (r[2].equals(s))
          r[2] = X;
      }
    }

    // Insert U Productions
    for (String[] s:productionsG1) {
      if (s[0].equals(productionsG1.getFirst()[0]))
        G.addProduction(initial, s[1], s[2]);
    }
    for (String[] s:productionsG2) {
      if (s[0].equals(productionsG2.getFirst()[0]))
        G.addProduction(initial, s[1], s[2]);
    }

    // Insert G1 & G2 Productions
    for (String[] s:productionsG1) {
        G.addProduction(s[0], s[1], s[2]);
    }
    for (String[] s:productionsG2) {
        G.addProduction(s[0], s[1], s[2]);
    }

    return G;
  }

}
