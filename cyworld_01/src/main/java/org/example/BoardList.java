package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardList {
    public BoardList() {
        JFrame boardFrame = new JFrame("게시판 목록");
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.setSize(800, 600);
        boardFrame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS)); // 상하로 정렬되는 레이아웃

        // 데이터베이스 연결 정보
        String url = "jdbc:mysql://cyworld.cji1ftocbium.ap-northeast-2.rds.amazonaws.com:3306/cyworld";
        String username = "tkv00";
        String password = "rlaehdus00";

        // 데이터베이스 연결
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT WriteBoardNum, userId, title FROM WriteBoard"; // 게시글을 가져오는 쿼리문 (테이블 이름에 맞게 수정 필요)
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // 데이터베이스에서 가져온 결과를 이용하여 버튼 생성
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId"); // 게시글 ID 컬럼에 맞게 수정 필요
                String postTitle = resultSet.getString("title"); // 게시글 제목 컬럼에 맞게 수정 필요
                int WriteBoardNum = resultSet.getInt("WriteBoardNum");

                JButton postButton = new JButton(WriteBoardNum + ". 게시글 작성자: " + userId + " / 제목: " + postTitle);
                postButton.addActionListener(e -> {
                    // 버튼 클릭 시 해당 게시글을 보여주는 동작 수행
                    // postId 등을 이용하여 해당 게시글을 가져오는 로직 구현
                    // 여기에 필요한 처리를 추가하세요
                    JOptionPane.showMessageDialog(null, "게시글 작성자: " + userId + "\n게시글 제목: " + postTitle);
                });

                boardPanel.add(postButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 데이터베이스 연결 실패 또는 쿼리 실행 중 오류가 발생한 경우 처리
        }

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        boardFrame.add(scrollPane, BorderLayout.CENTER);

        boardFrame.setVisible(true);
    }
}
