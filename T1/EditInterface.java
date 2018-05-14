import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;

public class EditInterface {

  public enum Type {
    GRAMMAR,
    AUTOMATON,
    REGEX
  }

  private void createComponents(JFrame frame, Type type) {
    JTextArea text;
    if (type == Type.GRAMMAR)
      text = new JTextArea("S->aS|a");
    else if (type == Type.AUTOMATON)
      text = new JTextArea("  |  | 0| 1|\n" +
                    			 " >|q0|q1|q0|\n" +
                    			 "* |q1|q0|q1|");
    else
      text = new JTextArea("a?bb*");

    text.setBounds(20, 10, 255, 280);
    text.setFont(new Font("monospaced", Font.PLAIN, 14));
    frame.add(text);

    JButton loadBtn = new JButton("Carregar do disco");
    loadBtn.setBounds(20, 320, 128, 30);
    loadBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        text.setText(Utils.loadFromDisk());
      }
    });
    frame.add(loadBtn);

    JButton saveBtn = new JButton("Gravar no disco");
    saveBtn.setBounds(150, 320, 125, 30);
    saveBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Utils.saveToDisk(text.getText());
      }
    });
    frame.add(saveBtn);
  }

  public EditInterface(Type type) {
    JFrame editFrame = new JFrame();

    editFrame.setSize(300, 400);
    editFrame.setLayout(null);
    editFrame.setVisible(true);

    if (type == Type.GRAMMAR)
      editFrame.setTitle("Editar Gramática Regular");
    else if (type == Type.AUTOMATON)
      editFrame.setTitle("Editar Autômato Finito");
    else
      editFrame.setTitle("Editar Expressão Regular");

    createComponents(editFrame, type);
  }

}
