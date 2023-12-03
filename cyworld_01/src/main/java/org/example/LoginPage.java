package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class LoginPage {



    private JFrame frame;
    private SignUppage SignUppage; // 회원가입 페이지 참조
    private JTextField usernameField;
    private JPasswordField passwordField;

    private MiniHomepage miniHomepage;

    public LoginPage(SignUppage signUpPage, MiniHomepage miniHomepage) {
        this.SignUppage = signUpPage;
        this.miniHomepage = miniHomepage;
        frame = new JFrame("로그인");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        frame.add(new JLabel("아이디:"));
        usernameField = new JTextField();
        frame.add(usernameField);
        frame.add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticate(username, password)) {
                miniHomepage.setUserId(username);
                miniHomepage.showMainPage(); // 메인 페이지 표시
                frame.setVisible(false); // 로그인 창 숨김
            } else {
                JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }

        });
        frame.add(loginButton);

        JButton signUpButton = new JButton("회원가입");
        signUpButton.addActionListener(e -> {
            LoginPage.this.SignUppage.show(); // 회원가입 페이지 표시
            frame.setVisible(false);
        });
        frame.add(signUpButton);
    }

    public void show() {
        frame.setVisible(true);
    }
    private boolean authenticate(String username, String password) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 실제 애플리케이션에서는 비밀번호를 해시하여 비교해야 함
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 로그인 성공 시 MiniHomepage에 사용자 ID 전달
                miniHomepage.setUserId(username);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


}