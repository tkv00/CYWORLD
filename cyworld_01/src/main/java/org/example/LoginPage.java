package org.example;

import org.Friend.FriendSearchDialog;
import org.Utility.BackgroundPanel;
import org.Utility.DatabaseConfig;
import org.Utility.UserSession;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;


public class LoginPage {

    private JFrame frame;
    private SignUppage SignUppage; // 회원가입 페이지 참조
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MiniHomepage miniHomepage;
    private FriendSearchDialog friendSearchDialog;

    //백그라운드 이미지 경로
    String imagePath= "/image/loginpage.png";
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

        usernameField = new JTextField(10);
        usernameField.setOpaque(false);
        usernameLabel.setFont(new Font("Nanum Font", Font.PLAIN, 10));

        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(usernameField, gbc);

        // 비밀번호 라벨 및 텍스트 필드
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passwordLabel, gbc);

        passwordField = new JPasswordField(10);
        passwordLabel.setFont(new Font("Nanum Font", Font.PLAIN, 10));

        passwordField.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(passwordField, gbc);
        // 로그인 버튼 텍스트용 레이블
        JLabel loginLabel = new JLabel("로그인");
        loginLabel.setFont(new Font("Nanum Font", Font.BOLD, 13));
        loginLabel.setForeground(Color.WHITE); // 텍스트 색상을 흰색으로 설정
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가로 정렬을 중앙으로 설정
        loginLabel.setVerticalAlignment(SwingConstants.CENTER); // 세로 정렬을 중앙으로 설정

        // 로그인 버튼
        JButton loginButton = new JButton();
        loginButton.setLayout(new BorderLayout());
        loginButton.add(loginLabel, BorderLayout.CENTER);
        loginButton.setOpaque(true);
        loginButton.setBackground(new Color(255, 102, 6)); // 배경 색상을 오렌지 색으로 설정
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 6))); // 테두리 색상을 주황색으로 설정

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(loginButton, gbc);

        // 회원가입 버튼
        JButton signUpButton = new JButton("회원가입");
        signUpButton.setFont(new Font("Nanum Font", Font.BOLD, 13));
        signUpButton.setForeground(new Color(255, 102, 6)); // 텍스트 색상을 주황색으로 설정
        signUpButton.setOpaque(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setBorderPainted(true); // 테두리를 그림
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 6))); // 테두리 색상을 주황색으로 설정
        loginButton.setPreferredSize(signUpButton.getPreferredSize()); // 회원가입 버튼의 크기를 기준으로 설정
        gbc.gridx = 1;
        gbc.gridy = 3;
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
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "아이디와 비밀번호를 모두 입력해주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            return false; // 로그인 차단
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 실제 애플리케이션에서는 비밀번호를 해시하여 비교해야 함
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 로그인 성공 시 MiniHomepage에 사용자 ID 전달
                miniHomepage.setUserId(username);
                // UserSession에 사용자 ID 저장
                UserSession.getInstance().setUserId(username);

                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


}