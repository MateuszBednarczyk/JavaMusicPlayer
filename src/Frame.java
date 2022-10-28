import javax.swing.*;

public class Frame extends JFrame {
    Frame() {
        var mainPanel = new MainPanel();
        this.setContentPane(mainPanel.getMainPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 500);
        this.setVisible(true);
    }
}
