package org.Utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WriteBoardManager {

    private PostClickListener postClickListener;

    public interface PostClickListener {
        void onPostClick(int postId);
    }
    public void setPostClickListener(PostClickListener listener) {
        this.postClickListener = listener;
    }
    public int[] getLatestPostIdAndUserId() {
        int[] latestPostInfo = new int[]{-1, -1}; // 초기값 -1로 설정
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT WriteBoardNum, userId FROM WriteBoard ORDER BY time DESC LIMIT 1";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        latestPostInfo[0] = resultSet.getInt("WriteBoardNum");
                        latestPostInfo[1] = resultSet.getInt("userId");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return latestPostInfo;
    }

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
    public JLabel createClickablePostTitleLabel() {
        JLabel latestPostTitleLabel = new JLabel(getLatestPostTitle());
        latestPostTitleLabel.setForeground(Color.BLACK);
        latestPostTitleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 손가락 커서 설정

        latestPostTitleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (postClickListener != null) {
                    int[] latestPostInfo = getLatestPostIdAndUserId(); // 게시글의 ID와 userId를 가져오는 메서드
                    int postId = latestPostInfo[0]; // 게시글 ID
                    int userId = latestPostInfo[1]; // 사용자 ID

                    // BoardList 클래스의 showPostDetailsInNewWindow 메서드를 호출하여 게시글을 새 창에 표시
                    showPostDetailsInNewWindow(postId);
                }
            }
        });
        return latestPostTitleLabel;
    }
    // 게시글의 ID를 가져오는 메소드
    private int getLatestPostId() {
        int latestPostId = -1; // 기본적으로 설정된 값
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT WriteBoardNum FROM WriteBoard ORDER BY time DESC LIMIT 1";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        latestPostId = resultSet.getInt("WriteBoardNum");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
        }
        return latestPostId;
    }
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
    public void showPostDetailsInNewWindow(int postId) {
        JFrame detailsFrame = new JFrame("게시글 세부 정보");
        detailsFrame.setSize(500, 400);
        detailsFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM WriteBoard WHERE WriteBoardNum= ?";
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

}
