package org.example;

import org.Utility.DatabaseConfig;
import org.Utility.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class WriteBoard {
    //현재 로그인한 유저
    String userId = UserSession.getInstance().getUserId();

    private JFrame frame;
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextArea commentArea;
    private JSplitPane splitPane;


    // 사용자 아이디를 설정하는 메서드
    public WriteBoard(String userId){
        this.userId = userId;
        initialize();
    }

    public WriteBoard() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("게시판");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // 상단 제목 입력 부분
        JPanel titlePanel = new JPanel(new BorderLayout());
        titleField = new JTextField();
        titlePanel.add(titleField, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createTitledBorder("제목"));

        // 중간 내용 입력 부분
        contentArea = new JTextArea();
        contentArea.setBorder(BorderFactory.createTitledBorder("내용"));
        //게시글 제출 버튼
        JButton submitButton=new JButton("제출");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPost();
            }
        });

        // 하단 댓글 입력 부분
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentArea = new JTextArea(5, 20);
        commentPanel.add(new JScrollPane(commentArea), BorderLayout.CENTER);
        commentPanel.setBorder(BorderFactory.createTitledBorder("댓글"));
        commentPanel.add(submitButton);

        // SplitPane을 이용하여 내용과 댓글 부분 분리
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(contentArea),
                commentPanel);
        splitPane.setResizeWeight(0.8); // 내용 영역이 댓글 영역보다 크게 설정
        splitPane.setDividerSize(10); // 구분선의 크기 설정

        // 각 패널을 프레임에 추가
        frame.add(submitButton,BorderLayout.SOUTH);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        // 프레임을 보이도록 설정
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new WriteBoard());
    }




    private void submitPost() {
        // 사용자 입력 가져오기
        String title = titleField.getText();
        String content = contentArea.getText();
        // 입력 검증
        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "제목과 내용을 모두 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);

        }

        // 데이터베이스 연결 및 쿼리 실행
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO WriteBoard (title, content,userId,time) VALUES (?, ?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setString(3, userId);
                pstmt.setString(4, String.valueOf(Timestamp.from(Instant.now())));// 현재 사용자 ID
                pstmt.executeUpdate();

                // 성공 메시지
                JOptionPane.showMessageDialog(frame, "게시글이 성공적으로 저장되었습니다.");

                // 입력 필드 초기화
                titleField.setText("");
                contentArea.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "데이터베이스 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}