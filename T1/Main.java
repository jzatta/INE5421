import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;

public class Main {

  private static void createButtons(JFrame frame) {
    int topPosition = 30;

    JButton btn_newGrammar = new JButton("Editar Gramática");
    btn_newGrammar.setBounds(50, topPosition, 130, 30);
    btn_newGrammar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        EditInterface eI = new EditInterface(EditInterface.Type.GRAMMAR);
      }
    });

    JButton btn_newAutomaton = new JButton("Editar Autômato F.");
    btn_newAutomaton.setBounds(185, topPosition, 130, 30);
    btn_newAutomaton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        EditInterface eI = new EditInterface(EditInterface.Type.AUTOMATON);
      }
    });

    JButton btn_newRegex = new JButton("Editar Expressão R.");
    btn_newRegex.setBounds(320, topPosition, 130, 30);
    btn_newRegex.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        EditInterface eI = new EditInterface(EditInterface.Type.REGEX);
      }
    });
    topPosition += 40;

    JButton btn_DeSimone = new JButton("ER -> AF (De Simone)");
    btn_DeSimone.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_GrToAf = new JButton("Transformar GR em AF");
    btn_GrToAf.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_AfToGr = new JButton("Transformar AF em GR");
    btn_AfToGr.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_DetMin = new JButton("Determinizar e Minimizar AF");
    btn_DetMin.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_LRsIntersection = new JButton("Intersecção de LRs -> AF");
    btn_LRsIntersection.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_LRsDiference = new JButton("Diferença de LRs -> AF");
    btn_LRsDiference.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_LRsReverse = new JButton("Reverso de LRs -> AF");
    btn_LRsReverse.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_GRsUnion = new JButton("União de GRs -> GR");
    btn_GRsUnion.setBounds(50, topPosition, 400, 30);
    btn_GRsUnion.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFrame GRsUnionFrame = new JFrame();
        GRsUnionFrame.setSize(680, 350);
        GRsUnionFrame.setLayout(null);
        GRsUnionFrame.setVisible(true);
        GRsUnionFrame.setTitle("União de GRs -> GR");

        JTextArea G1 = new JTextArea("S->aS|a");
        G1.setBounds(10, 10, 200, 250);
        G1.setFont(new Font("monospaced", Font.PLAIN, 14));
        GRsUnionFrame.add(G1);

        JTextArea G2 = new JTextArea("A->aA|a");
        G2.setBounds(220, 10, 200, 250);
        G2.setFont(new Font("monospaced", Font.PLAIN, 14));
        GRsUnionFrame.add(G2);

        JTextArea G = new JTextArea();
        G.setBounds(460, 10, 200, 250);
        G.setFont(new Font("monospaced", Font.PLAIN, 14));
        G.setEditable(false);
        GRsUnionFrame.add(G);

        JButton Union = new JButton("Obter G1 U G2");
        Union.setBounds(10, 270, 410, 30);
        Union.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Grammar Grammar1 = Grammar.readGrammar(G1.getText());
            Grammar Grammar2 = Grammar.readGrammar(G2.getText());

            G.setText(Utils.grammarUnion(Grammar1, Grammar2).toString());
          }
        });
        GRsUnionFrame.add(Union);
      }
    });
    topPosition += 40;

    JButton btn_GRsConcatenation = new JButton("Concatenação de GRs -> GR");
    btn_GRsConcatenation.setBounds(50, topPosition, 400, 30);
    btn_GRsConcatenation.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame GRsConcatenationFrame = new JFrame();
          GRsConcatenationFrame.setSize(680, 350);
          GRsConcatenationFrame.setLayout(null);
          GRsConcatenationFrame.setVisible(true);
          GRsConcatenationFrame.setTitle("Concatenação de GRs -> GR");

          JTextArea G1 = new JTextArea("S->aS|a");
          G1.setBounds(10, 10, 200, 250);
          G1.setFont(new Font("monospaced", Font.PLAIN, 14));
          GRsConcatenationFrame.add(G1);

          JTextArea G2 = new JTextArea("A->aA|a");
          G2.setBounds(220, 10, 200, 250);
          G2.setFont(new Font("monospaced", Font.PLAIN, 14));
          GRsConcatenationFrame.add(G2);

          JTextArea G = new JTextArea();
          G.setBounds(460, 10, 200, 250);
          G.setFont(new Font("monospaced", Font.PLAIN, 14));
          G.setEditable(false);
          GRsConcatenationFrame.add(G);

          JButton Concatenation = new JButton("Obter G1 . G2");
          Concatenation.setBounds(10, 270, 410, 30);
          Concatenation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Grammar Grammar1 = Grammar.readGrammar(G1.getText());
              Grammar Grammar2 = Grammar.readGrammar(G2.getText());

              G.setText(Utils.grammarConcatenation(Grammar1, Grammar2).toString());
            }
          });
          GRsConcatenationFrame.add(Concatenation);
        }
      });
    topPosition += 40;

    JButton btn_GRsClosure = new JButton("Fechamento de GR -> GR");
    btn_GRsClosure.setBounds(50, topPosition, 400, 30);
    btn_GRsClosure.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame GRsClosureFrame = new JFrame();
          GRsClosureFrame.setSize(480, 350);
          GRsClosureFrame.setLayout(null);
          GRsClosureFrame.setVisible(true);
          GRsClosureFrame.setTitle("Fechamento de GR -> GR");

          JTextArea G1 = new JTextArea("S->aS|a");
          G1.setBounds(10, 10, 200, 250);
          G1.setFont(new Font("monospaced", Font.PLAIN, 14));
          GRsClosureFrame.add(G1);

          JTextArea G = new JTextArea();
          G.setBounds(260, 10, 200, 250);
          G.setFont(new Font("monospaced", Font.PLAIN, 14));
          G.setEditable(false);
          GRsClosureFrame.add(G);

          JButton KleeneClosure = new JButton("Obter G*");
          KleeneClosure.setBounds(10, 270, 200, 30);
          KleeneClosure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Grammar Grammar1 = Grammar.readGrammar(G1.getText());

              G.setText(Utils.kleeneClosure(Grammar1).toString());
            }
          });
          GRsClosureFrame.add(KleeneClosure);
        }
      });
    topPosition += 40;

    JButton btn_AFRecognition = new JButton("Reconhecimento de Sentenças (AF)");
    btn_AFRecognition.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_AFEnumeration = new JButton("Enumeração de Sentenças de Tamanho N (AF)");
    btn_AFEnumeration.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    frame.add(btn_newGrammar);
    frame.add(btn_newAutomaton);
    frame.add(btn_newRegex);
    frame.add(btn_DeSimone);
    frame.add(btn_GrToAf);
    frame.add(btn_AfToGr);
    frame.add(btn_DetMin);
    frame.add(btn_LRsIntersection);
    frame.add(btn_LRsDiference);
    frame.add(btn_LRsReverse);
    frame.add(btn_GRsUnion);
    frame.add(btn_GRsConcatenation);
    frame.add(btn_GRsClosure);
    frame.add(btn_AFRecognition);
    frame.add(btn_AFEnumeration);
  }

  private static void setUpInterface(JFrame frame) {
    createButtons(frame);

    frame.setSize(500, 600);
    frame.setLayout(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("INE5421 - Linguagens Formais e Compiladores");
  }

  public static void main(String[] args) {
	try {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (InstantiationException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
		e.printStackTrace();
	}

    JFrame home = new JFrame();

    setUpInterface(home);

    String str = "  |  | 0| 1|\n" +
    			 " >|q0|q1|q0|\n" +
    			 "* |q1|q0|q1|";

    System.out.println(Automaton.readAutomaton(str).toString());
  }

}
