import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Frame {
    public JFrame jFrame;
    public UIGame uiGame;
    public J3x3Matrix j3x3Matrix;
    public JLabel turnLabel;
    public int humanRepresentInt = 1;

    Frame() {
        jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setTitle("Tic Tac Toe");
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(true);
        jFrame.setLayout(new BorderLayout());

        /*1.Menu*/
        JMenuBar jMenuBar = new JMenuBar();
        jFrame.setJMenuBar(jMenuBar);
        JMenu gameMenu = new JMenu("Game");
        jMenuBar.add(gameMenu);
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        gameMenu.add(newGameMenuItem);
        JRadioButtonMenuItem humanRadioButtonMenuItem = new JRadioButtonMenuItem("Human first");
        gameMenu.add(humanRadioButtonMenuItem);
        humanRadioButtonMenuItem.setSelected(true);
        JRadioButtonMenuItem pcRadioButtonMenuItem = new JRadioButtonMenuItem("PC first");
        gameMenu.add(pcRadioButtonMenuItem);
        ButtonGroup p1ButtonGroup = new ButtonGroup();
        p1ButtonGroup.add(humanRadioButtonMenuItem);
        p1ButtonGroup.add(pcRadioButtonMenuItem);
        gameMenu.addSeparator();
        JCheckBoxMenuItem analyzerCheckBoxMenuItem = new JCheckBoxMenuItem("Analyzer");
        gameMenu.add(analyzerCheckBoxMenuItem);
        gameMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);


        /*2.Main Panel*/
        JPanel mainPanel = new JPanel(new MigLayout("fill"));
        jFrame.add(mainPanel);

        /*2.1.Left Panel*/
        JPanel leftPanel = new JPanel(new MigLayout("al 50% 50%"));
        mainPanel.add(leftPanel, "pushx, grow, id lp");
        leftPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        j3x3Matrix = new J3x3Matrix();
        leftPanel.add(j3x3Matrix, "h 200::, w 200::, x container.w/2-min(container.w/2.5,container.h/2.5), y container.h/2-min(container.w/2.5,container.h/2.5), x2 container.w/2+min(container.w/2.5,container.h/2.5), y2 container.h/2+min(container.w/2.5,container.h/2.5)");

        /*2.1.Right Panel*/
        JPanel rightPanel = new JPanel(new MigLayout("al 50% 50%, flowy, fill"));
        mainPanel.add(rightPanel, "w 200!, grow");
        rightPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
        rightPanel.add(turnLabel, "ax 50%, pushy 2");
        JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        rightPanel.add(jSeparator, "grow");
        JTextArea analyzerTextArea = new JTextArea();
        rightPanel.add(analyzerTextArea, "grow, pushy 3");
        analyzerTextArea.setEditable(false);


        //Example
        j3x3Matrix.jButtons.get(2).setText("O");
        j3x3Matrix.jButtons.get(2).setEnabled(false);
        j3x3Matrix.jButtons.get(6).setText("X");
        j3x3Matrix.jButtons.get(6).setEnabled(false);
        j3x3Matrix.jButtons.get(7).setText("O");
        turnLabel.setText("<html>Turn:<br>HUMAN</html>");
        analyzerTextArea.setText("123456789\n2\n3\n4\n5\n6\n7\n8\n9\n");


        //Listener
        newGameMenuItem.addActionListener(a -> {
            uiGame = new UIGame(humanRepresentInt);
            for (JButton b : j3x3Matrix.jButtons) {
                b.setEnabled(true);
                b.setText(null);
                b.setBackground(null);
            }
            if (humanRepresentInt == -1)uiGame.gameProcess(this, null);
            System.out.println("newGame, human: "+humanRepresentInt);
        });
        humanRadioButtonMenuItem.addActionListener(a -> {
            if (humanRadioButtonMenuItem.isSelected()) humanRepresentInt = 1;
            System.out.println("human1st");
        });
        pcRadioButtonMenuItem.addActionListener(a -> {
            if (pcRadioButtonMenuItem.isSelected()) humanRepresentInt = -1;
            System.out.println("pc1st");
        });
        exitMenuItem.addActionListener(a -> {
            System.exit(0);
        });
        for (int i = 0; i < 9; i++) {
            JButton jButton = j3x3Matrix.jButtons.get(i);
            int finalI = i;
            jButton.addActionListener(a -> {
                if (uiGame != null) uiGame.gameProcess(this, finalI);
            });
        }

        jFrame.pack();

    }

}
