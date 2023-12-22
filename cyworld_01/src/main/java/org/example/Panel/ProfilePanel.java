package org.example.Panel;

import org.Utility.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ProfilePanel extends BackgroundPanel {
    private Image profileImage;
    private final int DEFAULT_WIDTH = 143; // DefaultImage의 가로 크기
    private final int DEFAULT_HEIGHT = 190; // DefaultImage의 세로 크기

    public ProfilePanel(Image profileImage) {
        super(profileImage);
        this.profileImage = scaleImageToDefaultSize(profileImage);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (profileImage != null) {
            // 이미지의 크기
            int imageWidth = profileImage.getWidth(this);
            int imageHeight = profileImage.getHeight(this);

            // 이미지를 그릴 위치 계산
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 2;

            g.drawImage(profileImage, x, y, this);
        }
    }

    // 프로필 이미지 변경 메서드
    public void changeProfileImage(Image newProfileImage) {
        this.profileImage = scaleImageToDefaultSize(newProfileImage);
        repaint(); // 변경된 이미지를 다시 그리도록 repaint() 호출
    }
    // DefaultImage의 크기에 맞춰 이미지 크기 조정
    private Image scaleImageToDefaultSize(Image img) {
        if (img == null) {
            return null;
        }

        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);

        double widthRatio = (double) DEFAULT_WIDTH / imgWidth;
        double heightRatio = (double) DEFAULT_HEIGHT / imgHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (imgWidth * ratio);
        int newHeight = (int) (imgHeight * ratio);

        BufferedImage scaledImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();

        // 이미지의 중앙을 기준으로 그리기 시작할 좌표 계산
        int x = (DEFAULT_WIDTH - newWidth) / 2;
        int y = (DEFAULT_HEIGHT - newHeight) / 2;

        g2d.drawImage(img, x, y, newWidth, newHeight, null);
        g2d.dispose();

        return scaledImage;
    }

    // 이미지 업로드 및 크기 조정 로직 추가
    public void uploadAndResizeImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
            Image image = imageIcon.getImage();

            // 이미지 크기 조정
            Image resizedImage = scaleImageToDefaultSize(image);

            // 프로필 이미지 변경
            changeProfileImage(resizedImage);
        }
    }
}