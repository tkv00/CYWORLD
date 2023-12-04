package org.example;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

public class MusicPlayer {
    private Clip clip;

    public void playMusicFromURL(String musicURL) {
        try {
            URL url = new URL(musicURL);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
