package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;


public class LoginPage {



    private JFrame frame;
    private SignUppage SignUppage; // 회원가입 페이지 참조
    private JTextField usernameField;
    private JPasswordField passwordField;

    private MiniHomepage miniHomepage;

    //백그라운드 이미지 경로
    String imagePath="/loginbackground.jpg";
    URL imageUrl = getClass().getResource(imagePath);

    public LoginPage(SignUppage signUpPage, MiniHomepage miniHomepage) {
        this.SignUppage = signUpPage;
        this.miniHomepage = miniHomepage;

        // 이미지 로드
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        if (imageIcon.getIconWidth() == -1) {
            System.err.println("Failed to load image: " + imagePath);
            return;
        }
        Image backgroundImage = imageIcon.getImage();

        // 배경 이미지가 있는 패널 생성
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);

        frame = new JFrame("로그인");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 467);
        frame.setContentPane(backgroundPanel); // 배경 패널 설정

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 여백 설정

        // 아이디 라벨 및 텍스트 필드
        JLabel usernameLabel = new JLabel("아이디:");
        usernameLabel.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(usernameField, gbc);

        // 비밀번호 라벨 및 텍스트 필드
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(passwordField, gbc);

        // 로그인 버튼
        JButton loginButton = new JButton("로그인");
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(loginButton, gbc);

        // 회원가입 버튼
        JButton signUpButton = new JButton("회원가입");
        signUpButton.setOpaque(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setBorderPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(signUpButton, gbc);


        //로그인 로직 구현
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
        //회원가입로직
        signUpButton.addActionListener(e -> {
            LoginPage.this.SignUppage.show(); // 회원가입 페이지 표시
            frame.setVisible(false);
        });

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