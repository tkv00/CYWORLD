package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;


public class PhotoGalleryManager extends Component {

    private Component parentComponent; // For dialog boxes
    private String userId; // The ID of the current user
    private JPanel photoPanel;
    public PhotoGalleryManager(Component parentComponent, String userId) {
        this.parentComponent = parentComponent;
        this.userId = userId;
        System.out.println("PhotoGalleryManager initialized with userId: " + this.userId);
    }

    // Method to open the photo gallery upload dialog
    public void openPhotoGallery() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("사진 선택");
        int result = fileChooser.showOpenDialog(parentComponent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            uploadPhoto(selectedFile);
        }
    }
    public Set<String> retrieveTags() {
        Set<String> tags = new HashSet<>();
        Connection conn = DatabaseConfig.getConnection();
        String sql = "SELECT DISTINCT Tags FROM PhotoGallery WHERE userId = ?"; // 예시 쿼리, 실제 쿼리는 DB 구조에 따라 달라질 수 있음

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.userId); // 현재 사용자 ID 설정
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Collections.addAll(tags, rs.getString("Tags").split(","));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }
    // 특정 태그에 해당하는 사진을 검색하는 메서드
    public List<PhotoGalleryImage> retrievePhotosByTag(String tag) {
        List<PhotoGalleryImage> photos = new ArrayList<>();
        Connection conn = DatabaseConfig.getConnection();
        // 제목과 시간을 포함하여 이미지 데이터 검색
        String sql = "SELECT ImageData, Title, UploadTime FROM PhotoGallery WHERE userId = ? AND Tags LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.userId); // 현재 사용자의 ID
            pstmt.setString(2, "%" + tag + "%"); // 태그 필터
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    byte[] imageData = rs.getBytes("ImageData");
                    String title = rs.getString("Title");
                    String uploadTime = rs.getString("UploadTime");
                    photos.add(new PhotoGalleryImage(new ImageIcon(imageData), title, uploadTime));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }


    // Method to handle photo upload
    void uploadPhoto(File file) throws IOException {
        byte[] imageData = Files.readAllBytes(file.toPath());
        Component parentComponent = this.isDisplayable() ? this : null;
        String title = null;
        String tags = null;

        // 제목을 입력받는 반복 루프
        while (title == null || title.trim().isEmpty()) {
            title = JOptionPane.showInputDialog(parentComponent, "제목을 입력하세요:");
            if (title == null) {
                // 사용자가 취소를 선택한 경우
                JOptionPane.showMessageDialog(parentComponent, "업로드를 취소했습니다.", "업로드 취소", JOptionPane.INFORMATION_MESSAGE);
                return; // 메서드 종료
            } else if (title.trim().isEmpty()) {
                JOptionPane.showMessageDialog(parentComponent, "사진 제목을 입력해야 합니다.", "제목 입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        // 카테고리를 입력받는 반복 루프
        while (tags == null || tags.trim().isEmpty() || tags.contains(",")) {
            TagInputDialog tagDialog = new TagInputDialog(parentComponent);
            tagDialog.setVisible(true);
            tags = tagDialog.getTags();
            if (tags == null) {
                // 사용자가 취소를 선택한 경우
                JOptionPane.showMessageDialog(parentComponent, "업로드를 취소했습니다.", "업로드 취소", JOptionPane.INFORMATION_MESSAGE);
                return; // 메서드 종료
            } else if (tags.trim().isEmpty() || tags.contains(",")) {
                JOptionPane.showMessageDialog(parentComponent, "하나의 카테고리만 입력해야 합니다.", "카테고리 입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        String uploadTime = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(new Date());
        insertPhotoIntoDatabase(userId, title, tags, uploadTime, imageData);
        JOptionPane.showMessageDialog(parentComponent, "사진이 성공적으로 업로드되었습니다.");
    }



    // Method to insert photo into the database
    private void insertPhotoIntoDatabase(String userId, String title, String tags, String uploadTime, byte[] imageData) {
        System.out.println("Inserting photo for userId: " + userId); // 디버깅을 위한 로그
        Connection conn = DatabaseConfig.getConnection();
        String sql = "INSERT INTO PhotoGallery (userId, Title, Tags, UploadTime, ImageData) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, tags);
            pstmt.setString(4, uploadTime);
            pstmt.setBytes(5, imageData);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "데이터베이스 오류가 발생했습니다.", "DB 오류", JOptionPane.ERROR_MESSAGE);
        }
        // Implement any necessary cleanup
    }
}

