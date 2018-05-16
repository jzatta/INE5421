import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;

public class Main {

  private static void createButtons(JFrame frame) {
    int topPosition = 30;

    JButton btn_newGrammar = new JButton("Editar GR");
    btn_newGrammar.setBounds(50, topPosition, 130, 30);
    btn_newGrammar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new EditInterface(EditInterface.Type.GRAMMAR);
      }
    });

    JButton btn_newAutomaton = new JButton("Editar AF");
    btn_newAutomaton.setBounds(185, topPosition, 130, 30);
    btn_newAutomaton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new EditInterface(EditInterface.Type.AUTOMATON);
      }
    });

    JButton btn_newRegex = new JButton("Editar ER");
    btn_newRegex.setBounds(320, topPosition, 130, 30);
    btn_newRegex.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new EditInterface(EditInterface.Type.REGEX);
      }
    });
    topPosition += 40;

    JButton btn_DeSimone = new JButton("ER -> AF (De Simone)");
    btn_DeSimone.setBounds(50, topPosition, 400, 30);
    topPosition += 40;

    JButton btn_GrToAf = new JButton("Transformar GR em AF");
    btn_GrToAf.setBounds(50, topPosition, 400, 30);
    btn_GrToAf.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame GRtoAFFrame = new JFrame();
          GRtoAFFrame.setSize(480, 390);
          GRtoAFFrame.setLayout(null);
          GRtoAFFrame.setVisible(true);
          GRtoAFFrame.setTitle("Transformar GR em AF");

          JTextArea G = new JTextArea("S->aS|a");
          G.setBounds(10, 10, 200, 250);
          G.setFont(new Font("monospaced", Font.PLAIN, 14));
          GRtoAFFrame.add(G);

          JTextArea AF = new JTextArea();
          AF.setBounds(260, 10, 200, 250);
          AF.setFont(new Font("monospaced", Font.PLAIN, 14));
          AF.setEditable(false);
          GRtoAFFrame.add(AF);

          JButton loadG = new JButton("Carregar G");
          loadG.setBounds(10, 270, 200, 30);
          loadG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              G.setText(Utils.loadFromDisk());
            }
          });
          GRtoAFFrame.add(loadG);

          JButton saveAF = new JButton("Salvar");
          saveAF.setBounds(260, 270, 200, 30);
          saveAF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Utils.saveToDisk(AF.getText());
            }
          });
          GRtoAFFrame.add(saveAF);

          JButton toNFA = new JButton("Converter para AF");
          toNFA.setBounds(10, 310, 200, 30);
          toNFA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Grammar Grammar1;
				try {
					Grammar1 = Grammar.readGrammar(G.getText());
					
					AF.setText(Grammar.toNFA(Grammar1).toString());
				} catch (MyException e1) {
					JOptionPane.showMessageDialog(GRtoAFFrame, e1.getMessage(), "Gramática Inválida", JOptionPane.ERROR_MESSAGE);
				}
            }
          });
          GRtoAFFrame.add(toNFA);
        }
      });
    topPosition += 40;

    JButton btn_AfToGr = new JButton("Transformar AF em GR");
    btn_AfToGr.setBounds(50, topPosition, 400, 30);
    btn_AfToGr.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame AFtoGRFrame = new JFrame();
          AFtoGRFrame.setSize(480, 390);
          AFtoGRFrame.setLayout(null);
          AFtoGRFrame.setVisible(true);
          AFtoGRFrame.setTitle("Transformar AF em GR");

          JTextArea AF = new JTextArea("  |  | 0| 1|\n" +
						     		   " >|q0|q1|q0|\n" +
						     		   "* |q1|q0|q1|");
          AF.setBounds(10, 10, 200, 250);
          AF.setFont(new Font("monospaced", Font.PLAIN, 14));
          AFtoGRFrame.add(AF);

          JTextArea G = new JTextArea();
          G.setBounds(260, 10, 200, 250);
          G.setFont(new Font("monospaced", Font.PLAIN, 14));
          G.setEditable(false);
          AFtoGRFrame.add(G);

          JButton loadAF = new JButton("Carregar AF");
          loadAF.setBounds(10, 270, 200, 30);
          loadAF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              AF.setText(Utils.loadFromDisk());
            }
          });
          AFtoGRFrame.add(loadAF);

          JButton saveG = new JButton("Salvar");
          saveG.setBounds(260, 270, 200, 30);
          saveG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Utils.saveToDisk(G.getText());
            }
          });
          AFtoGRFrame.add(saveG);

          JButton toGR = new JButton("Converter para GR");
          toGR.setBounds(10, 310, 200, 30);
          toGR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Automaton M = Automaton.readAutomaton(AF.getText());
            	
            	G.setText(Automaton.toGrammar(M).toString());
            }
          });
          AFtoGRFrame.add(toGR);
        }
      });
    topPosition += 40;

    JButton btn_Det = new JButton("Determinizar AF");
    btn_Det.setBounds(50, topPosition, 195, 30);
    btn_Det.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame GRsDetAFFrame = new JFrame();
          GRsDetAFFrame.setSize(480, 390);
          GRsDetAFFrame.setLayout(null);
          GRsDetAFFrame.setVisible(true);
          GRsDetAFFrame.setTitle("Determinizar AF");

          JTextArea AF = new JTextArea("  |  | 0| 1|\n" +
                                			 " >|q0|q1|q0|\n" +
                                			 "* |q1|q0|q1|");
          AF.setBounds(10, 10, 200, 250);
          AF.setFont(new Font("monospaced", Font.PLAIN, 14));
          GRsDetAFFrame.add(AF);

          JTextArea G = new JTextArea();
          G.setBounds(260, 10, 200, 250);
          G.setFont(new Font("monospaced", Font.PLAIN, 14));
          G.setEditable(false);
          GRsDetAFFrame.add(G);

          JButton loadAF = new JButton("Carregar AF");
          loadAF.setBounds(10, 270, 200, 30);
          loadAF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              AF.setText(Utils.loadFromDisk());
            }
          });
          GRsDetAFFrame.add(loadAF);

          JButton saveG = new JButton("Salvar");
          saveG.setBounds(260, 270, 200, 30);
          saveG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Utils.saveToDisk(G.getText());
            }
          });
          GRsDetAFFrame.add(saveG);

          JButton Determinize = new JButton("Determinizar");
          Determinize.setBounds(10, 310, 200, 30);
          Determinize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Automaton NFA = Automaton.readAutomaton(AF.getText());

              G.setText(Automaton.determinize(NFA).toString());
            }
          });
          GRsDetAFFrame.add(Determinize);
        }
      });

    JButton btn_Min = new JButton("Minimizar AF");
    btn_Min.setBounds(255, topPosition, 195, 30);
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
        GRsUnionFrame.setSize(680, 390);
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

        JButton loadG1 = new JButton("Carregar G1");
        loadG1.setBounds(10, 270, 200, 30);
        loadG1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            G1.setText(Utils.loadFromDisk());
          }
        });
        GRsUnionFrame.add(loadG1);

        JButton loadG2 = new JButton("Carregar G2");
        loadG2.setBounds(220, 270, 200, 30);
        loadG2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            G2.setText(Utils.loadFromDisk());
          }
        });
        GRsUnionFrame.add(loadG2);

        JButton saveG = new JButton("Salvar");
        saveG.setBounds(460, 270, 200, 30);
        saveG.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Utils.saveToDisk(G.getText());
          }
        });
        GRsUnionFrame.add(saveG);

        JButton Union = new JButton("Obter G1 U G2");
        Union.setBounds(10, 310, 410, 30);
        Union.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
			try {
				Grammar Grammar1 = Grammar.readGrammar(G1.getText());
				Grammar Grammar2 = Grammar.readGrammar(G2.getText());
				
				G.setText(Utils.grammarUnion(Grammar1, Grammar2).toString());
			} catch (MyException e1) {
				JOptionPane.showMessageDialog(GRsUnionFrame, e1.getMessage(), "Gramática Inválida", JOptionPane.ERROR_MESSAGE);
			}
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
          GRsConcatenationFrame.setSize(680, 390);
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

          JButton loadG1 = new JButton("Carregar G1");
          loadG1.setBounds(10, 270, 200, 30);
          loadG1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              G1.setText(Utils.loadFromDisk());
            }
          });
          GRsConcatenationFrame.add(loadG1);

          JButton loadG2 = new JButton("Carregar G2");
          loadG2.setBounds(220, 270, 200, 30);
          loadG2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              G2.setText(Utils.loadFromDisk());
            }
          });
          GRsConcatenationFrame.add(loadG2);

          JButton saveG = new JButton("Salvar");
          saveG.setBounds(460, 270, 200, 30);
          saveG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Utils.saveToDisk(G.getText());
            }
          });
          GRsConcatenationFrame.add(saveG);

          JButton Concatenation = new JButton("Obter G1 . G2");
          Concatenation.setBounds(10, 310, 410, 30);
          Concatenation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
			try {
				Grammar Grammar1 = Grammar.readGrammar(G1.getText());
				Grammar Grammar2 = Grammar.readGrammar(G2.getText());
				
				G.setText(Utils.grammarConcatenation(Grammar1, Grammar2).toString());
			} catch (MyException e1) {
				JOptionPane.showMessageDialog(GRsConcatenationFrame, e1.getMessage(), "Gramática Inválida", JOptionPane.ERROR_MESSAGE);
			}
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
          GRsClosureFrame.setSize(480, 390);
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

          JButton loadG1 = new JButton("Carregar G");
          loadG1.setBounds(10, 270, 200, 30);
          loadG1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              G1.setText(Utils.loadFromDisk());
            }
          });
          GRsClosureFrame.add(loadG1);

          JButton saveG = new JButton("Salvar");
          saveG.setBounds(260, 270, 200, 30);
          saveG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Utils.saveToDisk(G.getText());
            }
          });
          GRsClosureFrame.add(saveG);

          JButton KleeneClosure = new JButton("Obter G*");
          KleeneClosure.setBounds(10, 310, 200, 30);
          KleeneClosure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
			try {
				Grammar Grammar1 = Grammar.readGrammar(G1.getText());
				
				G.setText(Utils.kleeneClosure(Grammar1).toString());
			} catch (MyException e1) {
				JOptionPane.showMessageDialog(GRsClosureFrame, e1.getMessage(), "Gramática Inválida", JOptionPane.ERROR_MESSAGE);
			}
            }
          });
          GRsClosureFrame.add(KleeneClosure);
        }
      });
    topPosition += 40;

    JButton btn_AFRecognition = new JButton("Reconhecimento de Sentenças (AF)");
    btn_AFRecognition.setBounds(50, topPosition, 400, 30);
    btn_AFRecognition.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame AFRecognitionFrame = new JFrame();
          AFRecognitionFrame.setSize(450, 390);
          AFRecognitionFrame.setLayout(null);
          AFRecognitionFrame.setVisible(true);
          AFRecognitionFrame.setTitle("Reconhecimento de Sentenças (AF)");

          JTextArea AF = new JTextArea("  |  | 0| 1|\n" +
                                			 " >|q0|q1|q0|\n" +
                                			 "* |q1|q0|q1|");
          AF.setBounds(10, 10, 200, 250);
          AF.setFont(new Font("monospaced", Font.PLAIN, 14));
          AFRecognitionFrame.add(AF);

          JLabel SentenceLbl = new JLabel("Insira a sentença:");
          SentenceLbl.setBounds(220, 10, 200, 30);
          SentenceLbl.setFont(new Font("monospaced", Font.PLAIN, 14));
          AFRecognitionFrame.add(SentenceLbl);

          JTextField Sentence = new JTextField("011$");
          Sentence.setBounds(220, 40, 200, 30);
          Sentence.setFont(new Font("monospaced", Font.PLAIN, 14));
          AFRecognitionFrame.add(Sentence);

          JLabel Result = new JLabel("SENTENÇA RECONHECIDA");
          Result.setBounds(230, 150, 200, 30);
          Result.setFont(new Font("monospaced", Font.BOLD, 14));
          Result.setForeground(new Color(0, 130, 0));
          AFRecognitionFrame.add(Result);

          JButton loadAF = new JButton("Carregar AF");
          loadAF.setBounds(10, 270, 200, 30);
          loadAF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              AF.setText(Utils.loadFromDisk());
            }
          });
          AFRecognitionFrame.add(loadAF);

          JButton Recognition = new JButton("Reconhecer Sentença");
          Recognition.setBounds(10, 310, 410, 30);
          Recognition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Automaton M = Automaton.readAutomaton(AF.getText());

              if (M.recognize(Sentence.getText())) {
                Result.setText("SENTENÇA RECONHECIDA");
                Result.setForeground(new Color(0, 130, 0));
              } else {
                Result.setText("SENTENÇA NÃO RECONHECIDA");
                Result.setForeground(new Color(130, 0, 0));
              }
            }
          });
          AFRecognitionFrame.add(Recognition);
        }
      });
    topPosition += 40;

    JButton btn_AFEnumeration = new JButton("Enumeração de Sentenças de Tamanho N (AF)");
    btn_AFEnumeration.setBounds(50, topPosition, 400, 30);
    btn_AFEnumeration.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame AFEnumerationFrame = new JFrame();
          AFEnumerationFrame.setSize(450, 390);
          AFEnumerationFrame.setLayout(null);
          AFEnumerationFrame.setVisible(true);
          AFEnumerationFrame.setTitle("Enumeração de Sentenças de Tamanho N (AF)");

          JTextArea AF = new JTextArea("  |  | 0| 1|\n" +
                                			 " >|q0|q1|q0|\n" +
                                			 "* |q1|q0|q1|");
          AF.setBounds(10, 10, 200, 250);
          AF.setFont(new Font("monospaced", Font.PLAIN, 14));
          AFEnumerationFrame.add(AF);

          SpinnerModel value = new SpinnerNumberModel(3, 1, 999, 1);
          JSpinner amount = new JSpinner(value);
          amount.setBounds(220, 40, 200, 30);
          AFEnumerationFrame.add(amount);

          JLabel SentenceLbl = new JLabel("Insira o tamanho:");
          SentenceLbl.setBounds(220, 10, 200, 30);
          SentenceLbl.setFont(new Font("monospaced", Font.PLAIN, 14));
          AFEnumerationFrame.add(SentenceLbl);

          JTextArea Result = new JTextArea("");
          Result.setBounds(220, 100, 200, 200);
          Result.setFont(new Font("monospaced", Font.BOLD, 14));
          Result.setEditable(false);
          AFEnumerationFrame.add(Result);

          JButton loadAF = new JButton("Carregar AF");
          loadAF.setBounds(10, 270, 200, 30);
          loadAF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              AF.setText(Utils.loadFromDisk());
            }
          });
          AFEnumerationFrame.add(loadAF);

          JButton Enumeration = new JButton("Enumerar");
          Enumeration.setBounds(10, 310, 200, 30);
          Enumeration.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Automaton M = Automaton.readAutomaton(AF.getText());

              Result.setText("");

              for (String s: M.getEnumN((int) amount.getValue())) {
                Result.append(s);
                Result.append("\n");
              }
            }
          });
          AFEnumerationFrame.add(Enumeration);

          JButton save = new JButton("Salvar");
          save.setBounds(220, 310, 200, 30);
          save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Utils.saveToDisk(Result.getText());
            }
          });
          AFEnumerationFrame.add(save);
        }
      });
    topPosition += 40;

    frame.add(btn_newGrammar);
    frame.add(btn_newAutomaton);
    frame.add(btn_newRegex);
    frame.add(btn_DeSimone);
    frame.add(btn_GrToAf);
    frame.add(btn_AfToGr);
    frame.add(btn_Det);
    frame.add(btn_Min);
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
  }

}
