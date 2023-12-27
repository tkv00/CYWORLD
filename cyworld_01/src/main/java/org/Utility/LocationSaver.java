package org.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationSaver {


    public void saveLocation(String photoId, String location) {
        // 데이터베이스 연결
        try (Connection conn = DatabaseConfig.getConnection()) {
            // SQL 쿼리 준비
            // photoId는 사진을 식별하는 데 사용되며, location은 위치 정보를 저장합니다.
            String sql = "UPDATE PhotoGallery SET location = ? WHERE PhotoID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // 쿼리 매개변수 설정
                pstmt.setString(1, location);
                pstmt.setString(2, photoId);

                // 쿼리 실행
                int affectedRows = pstmt.executeUpdate();
                System.out.println("Updated location for photo ID " + photoId + ". Affected rows: " + affectedRows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 테스트를 위한 메인 메서드
    public static void main(String[] args) {
        LocationSaver saver = new LocationSaver();
        // 예제로, photo_id '1'에 대한 위치 정보를 업데이트합니다.
        saver.saveLocation("1", "서울특별시 강남구");
    }
}
