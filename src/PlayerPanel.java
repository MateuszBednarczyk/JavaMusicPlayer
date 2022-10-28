import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PlayerPanel {
    private JPanel playerPanel;
    private JButton playButton;
    private JButton stopButton;
    private JProgressBar musicBar;
    private JButton pauseButton;
    private JButton resumeButton;
    private Clip clip;
    long clipTimePosition;

    PlayerPanel() {
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playMusic();
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
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

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopMusic();
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
        clip.setMicrosecondPosition(clipTimePosition);
        clip.start();
        timer();
    }

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

    private void playMusic() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("/Users/matthew/Downloads/example.wav"));
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        timer();
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

    private void stopMusic() {
        musicBar.setValue(0);
        musicBar.setString("0:00 / 0:00");
        clip.stop();
    }
}
