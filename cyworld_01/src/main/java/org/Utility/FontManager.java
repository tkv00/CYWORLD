package org.Utility;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontManager {
    public static Font loadFont(String fontPath, int style, float size) {
        try {
            InputStream is = FontManager.class.getResourceAsStream(fontPath);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            return customFont.deriveFont(style, size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.err.println("폰트를 불러오는데 문제가 발생했습니다.");
            return new Font("SansSerif", style, (int) size);
        }
    }
}