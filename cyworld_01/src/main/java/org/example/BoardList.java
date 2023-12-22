package org.example;

import org.Utility.DatabaseConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardList {
    public BoardList() {
        JFrame boardFrame = new JFrame("게시판 목록");
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.setSize(800, 600);
        boardFrame.setLayout(new BorderLayout());
        JButton createPostButton = new JButton("게시글 작성");
        createPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 게시판 작성 클래스로 이동
                new WriteBoard();
            }
        });
        JPanel topPanel = new JPanel(); // 패널 생성
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 패널에 레이아웃 설정
        topPanel.add(createPostButton); // 패널에 버튼 추가

        boardFrame.add(topPanel, BorderLayout.NORTH); // 프레임에 패널 추가






        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS)); // 상하로 정렬되는 레이아웃

        // 데이터베이스 연결
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT WriteBoardNum, userId, time,title  FROM WriteBoard"; // 게시글을 가져오는 쿼리문 (테이블 이름에 맞게 수정 필요)
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // 데이터베이스에서 가져온 결과를 이용하여 버튼 생성
            while (resultSet.next()) {
                String userId = resultSet.getString("userId"); // 게시글 작성자 ID 컬럼에 맞게 수정 필요
                String postTitle = resultSet.getString("title"); // 게시글 제목 컬럼에 맞게 수정 필요
                int WriteBoardNum = resultSet.getInt("WriteBoardNum");
                String time=resultSet.getString("time");

                JButton postButton = new JButton(WriteBoardNum + ". 게시글 작성자: " + userId + " / 제목: " + postTitle+"   "+time);
                postButton.addActionListener(e -> {
                    // 버튼 클릭 시 해당 게시글을 보여주는 동작 수행
                    showPostDetailsInNewWindow(WriteBoardNum);
                });

                boardPanel.add(postButton);
                // 게시판 작성 버튼

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 데이터베이스 연결 실패 또는 쿼리 실행 중 오류가 발생한 경우 처리
        }

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        boardFrame.add(scrollPane, BorderLayout.CENTER);

        boardFrame.setVisible(true);
    }
    // 게시글 세부 정보를 보여주는 새 창
    private void showPostDetailsInNewWindow(int postId) {
        JFrame detailsFrame = new JFrame("게시글 세부 정보");
        detailsFrame.setSize(500, 400);
        detailsFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM WriteBoard WHERE WriteBoardNum = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String postTitle = resultSet.getString("title");
                String userId = resultSet.getString("userId");
                String postDate = resultSet.getString("time");
                String postContent = resultSet.getString("content");

                // 제목 섹션
                JPanel titlePanel = createSectionPanel("제목: " , postTitle);
                mainPanel.add(titlePanel);

                // 작성자 섹션
                JPanel authorPanel = createSectionPanel("작성자: " , userId);
                mainPanel.add(authorPanel);

                // 작성 날짜 섹션
                JPanel datePanel = createSectionPanel("작성 날짜: " , postDate);
                mainPanel.add(datePanel);
                // 작성 내용 섹션
                JPanel contentPanel = createSectionPanel("작성 내용: " , postContent);
                mainPanel.add(contentPanel);


            }
        } catch (SQLException e) {
            e.printStackTrace();
            JPanel errorPanel = createSectionPanel("게시글 세부 정보를 불러오는 데 실패했습니다.","");
            mainPanel.add(errorPanel);
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        detailsFrame.add(scrollPane, BorderLayout.CENTER);
        detailsFrame.setVisible(true);
    }
    // 섹션 패널 생성 도우미 메소드
    private JPanel createSectionPanel(String labelText, String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.GRAY, 1, true));
        JLabel label = new JLabel(labelText);
        label.setBorder(new EmptyBorder(10, 10, 10, 10)); // 상하좌우 10픽셀의 패딩을 추가합니다.
        panel.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10)); // 상하좌우 10픽셀의 패딩을 추가합니다.

        // 내용이 길 경우 스크롤바를 자동으로 나타나게 합니다.
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BoardList());
    }
}