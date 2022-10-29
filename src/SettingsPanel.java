import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel {
    private JPanel settingsPanel;
    public JTextField filesDirectory;
    private JButton confirmButton;

    SettingsPanel(PlayerPanel playerPanel) {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPanel.loadFiles(filesDirectory.getText());
            }
        });
    }

    public JPanel getSettingsPanel() {
        return settingsPanel;
    }

}
