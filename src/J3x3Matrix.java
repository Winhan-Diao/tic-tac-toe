import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class J3x3Matrix extends JPanel {
    public ArrayList<JButton> jButtons;

    J3x3Matrix() {
        jButtons = new ArrayList<>();
        setLayout(new GridLayout(3, 3, 5, 5));
        for (int i = 0; i < 9; i++) {
            JButton jButton = new JButton();
            jButton.setPreferredSize(new Dimension(100, 100));
            jButton.setFont(new Font("Calibri", Font.PLAIN, 50));
            jButton.setToolTipText(String.format("Grid %d", i));
            jButtons.add(jButton);
            add(jButton);
        }
    }

    public void setAllEnabled(boolean enabled) {
        for (JButton b : jButtons) {
            b.setEnabled(enabled);
        }
    }
}
