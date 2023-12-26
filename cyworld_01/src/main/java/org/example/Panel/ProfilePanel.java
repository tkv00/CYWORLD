
package org.example.Panel;

import org.Utility.ProfileImageUpload;
import org.Utility.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ProfilePanel extends JPanel {
    private Image profileImage;
    private final int DEFAULT_WIDTH = 143;
    private final int DEFAULT_HEIGHT = 190;
    private JLabel imageLabel;

    public ProfilePanel(Image profileImage) {
        this.setLayout(new BorderLayout()); // 레이아웃 설정
        imageLabel = new JLabel(); // 이미지 없이 레이블 초기화
        this.add(imageLabel, BorderLayout.CENTER); // 레이블을 패널에 추가
        setProfileImage(profileImage); // 초기 이미지 설정
    }
    public void loadAndSetUserProfileImage(String userId) {
        if (userId != null && !userId.trim().isEmpty()) {
            ProfileImageUpload imageUpload = new ProfileImageUpload();
            byte[] profileImageData = imageUpload.getLatestProfileImage(userId);

            if (profileImageData != null) {
                // 기본 이미지 제거하고 새 이미지 설정
                setProfileImage(new ImageIcon(profileImageData).getImage());
            } else {
                // 데이터베이스에서 이미지를 찾지 못한 경우 기본 이미지 설정
                setDefaultImage();
            }
        } else {
            // 유효하지 않은 사용자 ID의 경우 기본 이미지 설정
            setDefaultImage();
        }
    }

    public void setDefaultImage() {
        // 기본 이미지 로드
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/image/DefaultImage.jpg"));
        setProfileImage(defaultIcon.getImage());
    }

    public void updateProfileImage(Image newImage) {
        setProfileImage(newImage);
        imageLabel.setIcon(new ImageIcon(profileImage));
        this.revalidate();
        this.repaint();
    }

    private void setProfileImage(Image img) {
        if (img != null) {
            profileImage = scaleImageToDefaultSize(img);
        } else {
            // img가 null인 경우 처리
            profileImage = null;
        }
        updateImageLabel();
    }
    private void updateImageLabel() {
        if (profileImage != null) {
            imageLabel.setIcon(new ImageIcon(profileImage));
        } else {
            imageLabel.setIcon(null); // img가 null이면 기존 아이콘 제거
        }
        this.revalidate();
        this.repaint();
    }

    public Image scaleImageToDefaultSize(Image img) {
        if (img == null) return null;
        // 이미지 크기 조정 로직
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);

        double widthRatio = (double) DEFAULT_WIDTH / imgWidth;
        double heightRatio = (double) DEFAULT_HEIGHT / imgHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (imgWidth * ratio);
        int newHeight = (int) (imgHeight * ratio);

        BufferedImage scaledImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(img, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return scaledImage;
    }

    public void uploadAndResizeImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
            Image image = imageIcon.getImage();

            // 이미지 크기 조정 및 프로필 이미지 변경
            updateProfileImage(image);
        }
    }
}
