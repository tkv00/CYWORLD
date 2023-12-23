package org.Utility;

import org.example.Panel.ProfilePanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;

public class ProfileImageUpload {
    public void uploadProfileImage(File selectedFile ,ImageDetails imageDetails) throws IOException {
        byte[] imageData = Files.readAllBytes(selectedFile.toPath());
        imageDetails.setImageData(imageData); // ImageDetails에 이미지 데이터 설정
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "INSERT INTO profile (userId, image_data, image_name, upload_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, imageDetails.getUserId());
                pstmt.setBytes(2, imageDetails.getImageData());
                pstmt.setString(3, imageDetails.getImageName());
                pstmt.setString(4, imageDetails.getUploadTime());
                pstmt.executeUpdate();

                // 이미지 정보에 자동 생성된 ID를 설정
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
    }
    public byte[] getProfileImage(String userId) {
        byte[] imageData = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT image_data FROM profile WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    imageData = rs.getBytes("image_data");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return imageData;
    }
    public String getProfileImageName(String userId) {
        String imageName = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT image_name FROM profile WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    imageName = rs.getString("image_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return imageName;
    }
    public String getProfileUploadTime(String userId) {
        String uploadTime = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT upload_time FROM profile WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    uploadTime = rs.getString("upload_time");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return uploadTime;
    }
    public boolean isImageChanged(String userId) {
        boolean isChanged = false;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT COUNT(*) AS count FROM profile WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt("count");
                    isChanged = count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return isChanged;
    }
    public byte[] getLatestProfileImage(String userId) {
        byte[] imageData = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT image_data FROM profile WHERE userId = ? ORDER BY upload_time DESC LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    imageData = rs.getBytes("image_data");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return imageData;
    }

}
