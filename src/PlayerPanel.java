import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PlayerPanel {
    private JPanel playerPanel;
    private JButton playButton;
    private JProgressBar musicBar;
    private JButton pauseButton;
    private JButton resumeButton;
    JTree favouriteTracks;
    private Clip clip;
    long clipTimePosition;

    PlayerPanel() {
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playMusic();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseMusic();
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resume();
            }
        });
    }

    private void pauseMusic() {
        if (clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    private void resume() {
        if (!clip.isRunning()) {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
            timer();
        }
    }

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

    private void playMusic() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        if (clip == null) {
            AudioInputStream audioStream = openFileAndPrepareClip();
            clip.open(audioStream);
        } else {
            clip.close();
            AudioInputStream audioStream = openFileAndPrepareClip();
            clip.open(audioStream);
        }
        clip.start();
        timer();
    }

    private AudioInputStream openFileAndPrepareClip() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(Objects.requireNonNull(favouriteTracks.getSelectionPath()).getLastPathComponent().toString());
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        return audioStream;
    }

    public void loadFiles(String directory) {
        DefaultMutableTreeNode fileTree = new DefaultMutableTreeNode(directory);
        File[] files = new File(directory).listFiles();
        for (File file : files) {
            if (file.getName().contains(".wav")) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
                fileTree.add(node);
            }
        }
        favouriteTracks.setModel(new DefaultTreeModel(fileTree));
    }

    private void timer() {
        Thread timerThread = new Thread() {
            public void run() {
                while (clip.isRunning()) {
                    musicBar.setValue((int) (100 * clip.getMicrosecondPosition() / clip.getMicrosecondLength()));
                    musicBar.setString((int) (clip.getMicrosecondPosition() / (1e6 * 60)) + ":" + (int) ((clip.getMicrosecondPosition() / 1e6) % 60) + " : " + (int) (clip.getMicrosecondLength() / (1e6 * 60)) + ":" + (int) ((clip.getMicrosecondLength() / 1e6) % 60));
                }
            }
        };
        timerThread.start();
    }

    private void progressBarListener() {
        Thread thread = new Thread() {
            public void listen() {
                while (clip != null) {
                    musicBar.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            clip.setMicrosecondPosition((long) (clip.getMicrosecondLength() * musicBar.getValue() / 100));
                        }
                    });
                }
            }
        };
    }
}
