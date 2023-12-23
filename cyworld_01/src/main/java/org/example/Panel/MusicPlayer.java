package org.example.Panel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class MusicPlayer {
    private long currentPlayPosition = 0; // 현재 재생 중인 위치를 저장할 변수
    private List<Clip> clips = new ArrayList<>();
    private List<String> musicPaths = new ArrayList<>(); // 음악 파일 경로 리스트
    private int currentPlayingIndex=-1;
    private JSlider musicSlider; // 음악 재생 위치를 조정하는 슬라이더


    public MusicPlayer(List<String> musicPaths) {
        this.musicPaths = musicPaths;
        loadMusicFiles();
        if (!clips.isEmpty()) {
            currentPlayingIndex = 0; // 첫 번째 곡으로 초기화
        }
    }

    private void loadMusicFiles() {
        for (String path : musicPaths) {
            loadSingleMusicFile(path);
        }
    }
    // 현재 재생 중인 음악의 인덱스를 반환하는 메소드
    public int getCurrentPlayingIndex() {
        return currentPlayingIndex;
    }
    // 현재 인덱스의 음악을 재생하는 메소드
    public void playMusic() {
        if (currentPlayingIndex >= 0 && currentPlayingIndex < clips.size()) {
            Clip clip = clips.get(currentPlayingIndex);
            if (clip.isRunning()) {
                // 이미 재생 중인 경우는 처리하지 않음
            } else {
                clip.setMicrosecondPosition(currentPlayPosition); // 저장된 위치에서 재생 시작
                clip.start();
            }
        }
    }

    public void playMusicAtIndex(int index) {
        if (index >= 0 && index < clips.size()) {
            // 현재 재생 중인 음악을 중지하고, 재생할 음악 인덱스 업데이트
            if (currentPlayingIndex != -1 && currentPlayingIndex < clips.size()) {
                clips.get(currentPlayingIndex).stop();
            }
            currentPlayingIndex = index; // 현재 재생 인덱스 업데이트

            // 새로 재생할 클립을 가져오고 시작
            Clip clip = clips.get(currentPlayingIndex);
            clip.setMicrosecondPosition(0); // 시작 지점으로 리셋
            clip.start();

            if (musicSlider != null) { // musicSlider가 null이 아닐 때만 실행
                long totalDuration = clip.getMicrosecondLength();
                musicSlider.setMaximum((int) (totalDuration / 1000000)); // 마이크로초를 초로 변환
                musicSlider.setValue(0); // 슬라이더 위치 초기화
            }
        }
    }
    public long getDurationOfMusicAtIndex(int index) {
        if (index >= 0 && index < clips.size()) {
            return clips.get(index).getMicrosecondLength();
        }
        return 0;
    }



    public void setPlayPosition(long position) {
        if (currentPlayingIndex != -1 && currentPlayingIndex < clips.size()) {
            Clip clip = clips.get(currentPlayingIndex);
            if (position < clip.getMicrosecondLength()) {
                currentPlayPosition = position; // 현재 재생 위치 업데이트
                clip.setMicrosecondPosition(position); // 노래 재생 위치 설정
            }
        }
    }

    // 이전 곡 재생
    public void playPrevious() {
        if (currentPlayingIndex > 0) { // 첫 번째 곡이 아니라면
            playMusicAtIndex(currentPlayingIndex - 1); // 이전 인덱스의 곡 재생
        }
        else {
            // 리스트의 끝에 도달했을 경우, 첫 번째 곡으로 돌아감
            playMusicAtIndex(0);
        }
    }
    // 다음 곡 재생
    public void playNext() {
        if (currentPlayingIndex < clips.size() - 1) {
            playMusicAtIndex(currentPlayingIndex + 1);
        }
        else {
            // 리스트의 시작점에 도달했을 경우, 마지막 곡으로 돌아감
            playMusicAtIndex(clips.size() - 1);
        }
    }

    // 일시 정지
    public void pauseMusic() {
        if (currentPlayingIndex != -1 && currentPlayingIndex < clips.size()) {
            Clip clip = clips.get(currentPlayingIndex);
            if (clip.isRunning()) {
                currentPlayPosition = clip.getMicrosecondPosition(); // 현재 재생 중인 위치 저장
                clip.stop();
            }
        }
    }

    // 재개
    public void resumeMusic() {
        if (currentPlayingIndex != -1 && currentPlayingIndex < clips.size()) {
            Clip clip = clips.get(currentPlayingIndex);
            if (!clip.isRunning()) {
                clip.start();
            }
        }
    }

    // 현재 재생 시간 가져오기 (마이크로초 단위)
    public long getCurrentPosition() {
        if (currentPlayingIndex != -1 && currentPlayingIndex < clips.size()) {
            Clip clip = clips.get(currentPlayingIndex);
            if (clip.isRunning() || clip.isActive()) {
                return clip.getMicrosecondPosition();
            }
        }
        return 0;
    }
    // 전체 길이 가져오기 (마이크로초 단위)
    public long getTotalDuration() {
        if (currentPlayingIndex != -1 && currentPlayingIndex < clips.size()) {
            return clips.get(currentPlayingIndex).getMicrosecondLength();
        }
        return 0;
    }
    private void loadSingleMusicFile(String path) {
        try {
            URL musicUrl = getClass().getResource(path);
            if (musicUrl != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicUrl);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clips.add(clip);
            } else {
                System.err.println("File not found: " + path);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }



}