import javax.swing.*;

public class TabControl extends JTabbedPane {
    TabControl() {
        PlayerPanel playerPanel = new PlayerPanel();
        SettingsPanel settingsPanel = new SettingsPanel();
        this.addTab("Player", playerPanel.getPlayerPanel());
        this.addTab("Settings", settingsPanel.getSettingsPanel());
    }
}

