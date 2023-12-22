package org.example;

import javax.swing.*;
import java.awt.*;

public class ProfileTextPanel extends JPanel {
    private String text;

    public ProfileTextPanel(String text) {
        this.text = text;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 텍스트 그리기
        if (text != null && !text.isEmpty()) {
            Font font = new Font("SansSerif", Font.PLAIN, 14);
            FontMetrics metrics = g.getFontMetrics(font);
            int x = 0;  // 텍스트 시작 위치 x좌표
            int y = getHeight() / 2 + metrics.getAscent() / 2;  // 세로 중앙에 텍스트 위치
            g.setColor(Color.BLACK);  // 텍스트 색상
            g.setFont(font);
            g.drawString(text, x, y);
        }
    }
}