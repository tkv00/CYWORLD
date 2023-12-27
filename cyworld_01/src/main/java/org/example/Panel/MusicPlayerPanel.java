package org.example.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class MusicPlayerPanel extends JPanel {
    private List<String> musicFiles = Arrays.asList(
            "/music/music1.wav",
            "/music/music2.wav",
            "/music/music3.wav",
            "/music/music4.wav"
    );

    private String[][] songInfo = {
            {"Y", "Free Style"},
            {"응급실", "izi"},
            {"밤하늘의 별을","양정승"},
            {"I love U Oh Thank U","MC몽"}
    };

    private MusicPlayer musicPlayer;
    private JLabel playTimeLabel;
    private JLabel songInfoLabel;
    private JSlider musicSlider;
    private Timer playTimeUpdateTimer;

    public MusicPlayerPanel() {
        this.musicPlayer = new MusicPlayer(musicFiles);
        setLayout(new BorderLayout());
        setOpaque(false);

        // 뮤직 슬라이더 설정 및 상단에 배치
        musicSlider = new JSlider();
        musicSlider.setOpaque(false);
        add(musicSlider, BorderLayout.NORTH);

        // 컨트롤 버튼 패널 생성 및 설정
        JPanel controlButtonPanel = new JPanel();
        controlButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlButtonPanel.setOpaque(false);

        // 컨트롤 버튼들 생성 및 설정
        JToggleButton playPauseButton = new JToggleButton("▶", false);
        JButton nextButton = new JButton(">>");
        JButton prevButton = new JButton("<<");
        controlButtonPanel.add(prevButton);
        controlButtonPanel.add(playPauseButton);
        controlButtonPanel.add(nextButton);

        // 재생 시간 및 노래 정보 레이블 설정
        playTimeLabel = new JLabel("00:00");
        songInfoLabel = new JLabel("Y-Free Style");

        // 하단 컨트롤 패널 구성
        JPanel bottomControlPanel = new JPanel(new BorderLayout());
        bottomControlPanel.add(controlButtonPanel, BorderLayout.WEST);
        bottomControlPanel.add(playTimeLabel, BorderLayout.CENTER);
        bottomControlPanel.add(songInfoLabel, BorderLayout.EAST);
        bottomControlPanel.setOpaque(false);

        // 전체 패널에 하단 컨트롤 패널 추가
        add(bottomControlPanel, BorderLayout.SOUTH);

        // 액션 리스너 설정
        playPauseButton.addActionListener(e -> togglePlayPause(playPauseButton));
        nextButton.addActionListener(e -> playNextSong());
        prevButton.addActionListener(e -> playPreviousSong());

        // 음악 재생 시간을 업데이트하는 타이머 초기화
        initializePlayTimeUpdater();
    }

    private void togglePlayPause(JToggleButton playPauseButton) {
        if (playPauseButton.isSelected()) {
            playPauseButton.setText("||");
            if (musicPlayer.getCurrentPlayingIndex() == -1) {
                musicPlayer.playMusicAtIndex(0); // 첫 번째 곡 재생
            } else {
                musicPlayer.playMusic(); // 계속 재생
            }
        } else {
            playPauseButton.setText("▶");
            musicPlayer.pauseMusic(); // 일시 정지
        }
    }

    private void playNextSong() {
        musicPlayer.playNext();
        int newIndex = musicPlayer.getCurrentPlayingIndex();
        songInfoLabel.setText(songInfo[newIndex][0] + " - " + songInfo[newIndex][1]);
    }

    private void playPreviousSong() {
        musicPlayer.playPrevious();
        int newIndex = musicPlayer.getCurrentPlayingIndex();
        if (newIndex >= 0 && newIndex < songInfo.length) {
            songInfoLabel.setText(songInfo[newIndex][0] + " - " + songInfo[newIndex][1]);
        } else {
            System.out.println("유효하지 않은 인덱스입니다: " + newIndex);
        }
    }

    private void initializePlayTimeUpdater() {
        playTimeUpdateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlayTime();
            }
        });
        playTimeUpdateTimer.start();
    }

    private void updatePlayTime() {
        if (musicPlayer != null) {
            long microSeconds = musicPlayer.getCurrentPosition();
            long totalMicroSeconds = musicPlayer.getTotalDuration(); // 전체 재생 길이 가져오기
            long seconds = microSeconds / 1000000;
            long totalSeconds = totalMicroSeconds / 1000000;
            long min = seconds / 60;
            long sec = seconds % 60;
            playTimeLabel.setText(String.format("%02d:%02d", min, sec));

            if (!musicSlider.getValueIsAdjusting() && totalSeconds > 0) {
                int sliderValue = (int) (seconds * 100 / totalSeconds);
                musicSlider.setValue(sliderValue);
            }
        }
    }
}
