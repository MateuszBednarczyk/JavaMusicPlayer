import javax.swing.*;

public class MainPanel {
    private JPanel mainPanel;

    MainPanel() {
        mainPanel.setLayout(new OverlayLayout(mainPanel));
        mainPanel.add(new TabControl());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
