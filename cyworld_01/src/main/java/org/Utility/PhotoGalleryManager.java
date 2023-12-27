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
                    String location=rs.getString("location");
                    photos.add(new PhotoGalleryImage(new ImageIcon(imageData), title, uploadTime,location));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }

    public void uploadPhotoWithLocation(String title, String category, String selectedLocation, byte[] imageData) throws SQLException {
        Connection connection = DatabaseConfig.getConnection();
        String uploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String sql = "INSERT INTO PhotoGallery (userId, title, Tags, location, ImageData, UploadTime) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, this.userId);
        statement.setString(2, title);
        statement.setString(3, category);
        statement.setString(4, selectedLocation);
        statement.setBytes(5, imageData); // 이미지 데이터를 파라미터에서 직접 받습니다.
        statement.setString(6, uploadTime);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("사진 및 위치 정보가 성공적으로 업로드되었습니다.");
        }
    }

}

