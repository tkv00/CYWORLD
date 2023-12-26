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
     *
     * @return Connection 데이터베이스 연결 객체
     */
    public static Connection getConnection() {
        try {
            System.out.println("Connecting to the database...");
            Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            System.out.println("Database connected!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();  // 이 부분에서 로깅 프레임워크를 사용할 수도 있습니다.
            return null;
        }
    }
}

