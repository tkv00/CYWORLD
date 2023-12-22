package org.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // 데이터베이스 연결을 위한 기본 설정
    private static final String DATABASE_URL = 
    private static final String DATABASE_USER = 
    private static final String DATABASE_PASSWORD = 
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 데이터베이스 연결을 위한 JDBC 드라이버 로드
     */
    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 연결을 제공
     * @return Connection 데이터베이스 연결 객체
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
