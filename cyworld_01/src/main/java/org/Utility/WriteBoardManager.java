package org.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WriteBoardManager {

    public String getLatestPostTitle() {
        String latestTitle = ""; // 기본적으로 빈 문자열로 초기화

        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT title FROM WriteBoard ORDER BY time DESC LIMIT 1";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        latestTitle = resultSet.getString("title");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return latestTitle;
    }
}
