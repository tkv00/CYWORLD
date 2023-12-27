package org.example.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GifPanel extends JPanel {
    private Avatar avatar;

    public GifPanel() {
        this.avatar = new Avatar("/image/MainRoom.gif");
        setPreferredSize(new Dimension(400, 200)); // 패널의 초기 크기 설정
        // KeyListener 추가
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyEvent(e);
            }
        });

        setFocusable(true);  // 키 이벤트를 받기 위해 포커스 가능하도록 설정
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        avatar.draw(g, this); // 아바타와 배경 그리기
    }
    public void handleKeyEvent(KeyEvent e) {
        // 패널과 아바타의 크기를 파라미터로 전달하여 moveAvatar 호출
        avatar.moveAvatar(e, this.getWidth(), this.getHeight(),
                avatar.getCurrentCharacterImage().getIconWidth(),
                avatar.getCurrentCharacterImage().getIconHeight());
        repaint(); // 변경사항을 반영하도록 패널 다시 그리기
    }
}
