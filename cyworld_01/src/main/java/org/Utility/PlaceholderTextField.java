package org.Utility;

import javax.swing.*;
import java.awt.*;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            g2.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
            g2.dispose();
        }
    }
}
