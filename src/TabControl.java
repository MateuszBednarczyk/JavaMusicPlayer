import javax.swing.*;

public class TabControl extends JTabbedPane {
    TabControl() {
        PlayerPanel playerPanel = new PlayerPanel();
        playerPanel.favouriteTracks.setModel(null);
        SettingsPanel settingsPanel = new SettingsPanel(playerPanel);
        this.addTab("Player", playerPanel.getPlayerPanel());
        this.addTab("Settings", settingsPanel.getSettingsPanel());
    }
}

