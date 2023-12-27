package org.example.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar {
    private ImageIcon[] characterImages;
    private int currentCharacterFrame;
    private String characterDirection;
    private Point characterPosition;
    private ImageIcon gifBackground;
    private ImageIcon balloonImage;
    private Point balloonPosition;
    private String speechText; // 말풍선에 표시할 텍스트
    public Avatar(String backgroundPath) {
        loadCharacterImages();
        loadBackgroundImage(backgroundPath);
        balloonImage = new ImageIcon(getClass().getResource("/image/speechbubble_01.png"));
        currentCharacterFrame = 0;
        characterDirection = "front";
        speechText = ""; // 초기값은 빈 문자열로 설정

        characterPosition = new Point(50, 80);
        balloonPosition = new Point(50, 0);
    }
    public void setSpeechText(String text) {
        speechText = text;
    }
    private void loadCharacterImages() {
        characterImages = new ImageIcon[16];
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
                dx = -7;
                characterDirection = "left";
                break;
            case KeyEvent.VK_RIGHT:
                dx = 7;
                characterDirection = "right";
                break;
            case KeyEvent.VK_UP:
                dy = -7;
                characterDirection = "back";
                break;
            case KeyEvent.VK_DOWN:
                dy = 7;
                characterDirection = "front";
                break;
        }

        int newX = characterPosition.x + dx;
        int newY = characterPosition.y + dy;
        characterPosition.x = Math.max(0, Math.min(newX, panelWidth - avatarWidth));
        characterPosition.y = Math.min(newY, panelHeight - avatarHeight);

        currentCharacterFrame = (currentCharacterFrame + 1) % 4;
        // 말풍선 위치 업데이트
        balloonPosition.x = characterPosition.x;
        balloonPosition.y = characterPosition.y - balloonImage.getIconHeight();
    }

    public Point getCharacterPosition() {
        return characterPosition;
    }

    public Point getBalloonPosition() {
        return balloonPosition;
    }

    private int getCharacterFrameIndex() {
        int baseIndex;
        switch (characterDirection) {
            case "front":
                baseIndex = 0;
                break;
            case "left":
                baseIndex = 4;
                break;
            case "right":
                baseIndex = 8;
                break;
            case "back":
                baseIndex = 12;
                break;
            default:
                baseIndex = 0;
                break;
        }
        return baseIndex + currentCharacterFrame % 4;
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(gifBackground.getImage(), 0, 0, panel.getWidth(), panel.getHeight(), panel);
        Point pos = getCharacterPosition();
        g.drawImage(getCurrentCharacterImage().getImage(), pos.x, pos.y, panel);
        Point balloonPos = getBalloonPosition();
        g.drawImage(balloonImage.getImage(), balloonPos.x+20, balloonPos.y+10, panel);
        // 텍스트 그리기
        g.setColor(Color.BLACK); // 텍스트 색상 설정
        g.drawString(speechText, balloonPosition.x + 30, balloonPosition.y +30);
    }
}