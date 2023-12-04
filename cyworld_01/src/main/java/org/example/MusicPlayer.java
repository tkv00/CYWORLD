package org.example;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
public class MusicPlayer {
    private Clip clip;

    public void playMusicFromFile(String filePath) {
        try {
            URL musicUrl = getClass().getResource(filePath);
            if (musicUrl != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicUrl);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.err.println("File not found: " + filePath);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void resumeMusic() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

}
