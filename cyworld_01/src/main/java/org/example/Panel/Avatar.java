package org.example.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar {
    private ImageIcon[] characterImages; // 캐릭터 이미지 배열
    private int currentCharacterFrame; // 현재 캐릭터 프레임
    private String characterDirection; // 캐릭터 방향
    private Point characterPosition; // 캐릭터 위치
    private ImageIcon gifBackground; // 배경 GIF

    public Avatar(String backgroundPath) {
        loadCharacterImages();
        loadBackgroundImage(backgroundPath);
        currentCharacterFrame = 0;
        characterDirection = "front";
        characterPosition = new Point(50, 50); // 초기 캐릭터 위치 설정
    }
    private void loadCharacterImages() {
        characterImages = new ImageIcon[16]; // 총 16개의 이미지
        for (int i = 0; i < 16; i++) {
            characterImages[i] = new ImageIcon(getClass().getResource("/image/character_" + (i + 1) + ".png"));
        }
    }
    private void loadBackgroundImage(String path) {
        gifBackground = new ImageIcon(getClass().getResource(path));
    }
    public ImageIcon getCurrentCharacterImage() {
        int index = getCharacterFrameIndex();
        return characterImages[index];
    }
    void moveAvatar(KeyEvent e, int panelWidth, int panelHeight, int avatarWidth, int avatarHeight) {
        int dx = 0, dy = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                dx = -7; characterDirection = "left"; break;
            case KeyEvent.VK_RIGHT:
                dx = 7; characterDirection = "right"; break;
            case KeyEvent.VK_UP:
                dy = -7; characterDirection = "back"; break;
            case KeyEvent.VK_DOWN:
                dy = 7; characterDirection = "front"; break;
        }
        // 캐릭터의 잠재적인 새 위치 계산

        int newX = characterPosition.x + dx;
        int newY = characterPosition.y + dy;
        characterPosition.x = Math.max(0, Math.min(newX, panelWidth - avatarWidth));
        characterPosition.y = Math.min(newY, panelHeight - avatarHeight);

        // 캐릭터 애니메이션 프레임 업데이트
        currentCharacterFrame = (currentCharacterFrame + 1) % 4; // 각 방향별 4개의 프레임

    }
    public Point getCharacterPosition() {
        return characterPosition;
    }
    private int getCharacterFrameIndex() {
        int baseIndex;
        switch (characterDirection) {
            case "front": baseIndex = 0; break;
            case "left": baseIndex = 4; break;
            case "right": baseIndex = 8; break;
            case "back": baseIndex = 12; break;
            default: baseIndex = 0; break;
        }
        return baseIndex + currentCharacterFrame % 4; // 각 방향별로 4개의 이미지
    }
    public void draw(Graphics g, JPanel panel) {
        // 배경 GIF 그리기
        g.drawImage(gifBackground.getImage(), 0, 0, panel.getWidth(), panel.getHeight(), panel);
        // 아바타 그리기
        Point pos = getCharacterPosition();
        g.drawImage(getCurrentCharacterImage().getImage(), pos.x, pos.y, panel);
    }
}
