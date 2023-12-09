

package org.Utility;

import javax.swing.*;
import java.awt.*;

    public class BubblePanel extends JPanel {
        private String message;
        private String timestamp;
        private boolean isCurrentUser;

        public BubblePanel(String message, String timestamp, boolean isCurrentUser) {
            this.message = message;
            this.timestamp = timestamp;
            this.isCurrentUser = isCurrentUser;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 말풍선 위치와 크기 설정
            int bubbleWidth = 200; // 말풍선의 너비
            int bubbleHeight = 50; // 말풍선의 높이
            int x = isCurrentUser ? getWidth() - bubbleWidth - 10 : 10; // 위치 조정

            // 말풍선 색상 설정
            g.setColor(isCurrentUser ? Color.LIGHT_GRAY : Color.WHITE);
            g.fillRoundRect(x, 5, bubbleWidth, bubbleHeight, 15, 15);

            // 텍스트 및 시간 표시
            g.setColor(Color.BLACK);
            g.drawString(message, x + 10, 20);
            g.drawString(timestamp, x + 10, 40);
        }
    }
