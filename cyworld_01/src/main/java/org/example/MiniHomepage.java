package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MiniHomepage {
    String imagePath = "/cyworldmain.jpg";
    URL imageUrl = getClass().getResource(imagePath);
    private LoginPage loginPage;
    private SignUppage signUpPage;
    private JLabel userIdLabel; // 사용자 ID를 표시할 레이블

    public MiniHomepage() {
        signUpPage = new SignUppage();
        loginPage = new LoginPage(signUpPage, this);
        userIdLabel = new JLabel();
        // 초기 레이블 텍스트 설정 (예시)
        userIdLabel.setText("Welcome, Guest");
    }

    public static void main(String[] args) {
        new MiniHomepage().showLogin();
    }

    private void showLogin() {
        loginPage.show();
    }

    public void showMainPage() {
        JFrame frame = new JFrame("싸이월드");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(888, 598); // 창 크기 설정

        // 이미지 로드 부분
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        if (imageIcon.getIconWidth() == -1) {
            System.err.println("Failed to load image: " + imagePath);
            return;
        }
        Image backgroundImage = imageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(false); // 배경 패널을 투명하게 설정



        // 상단 바 구성
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false); // 투명도 설정
        topBar.add(userIdLabel, BorderLayout.EAST); // 상단 바에 userIdLabel 추가
        backgroundPanel.add(topBar, BorderLayout.NORTH);

        // 메뉴 바 구성
        JPanel menuBar = createMenuBar();
        menuBar.setOpaque(false); // 투명도 설정
        backgroundPanel.add(menuBar, BorderLayout.WEST);

        // 메인 컨텐츠 구성
        JPanel mainContent = createMainContent();
        mainContent.setOpaque(false); // 투명도 설정
        backgroundPanel.add(mainContent, BorderLayout.CENTER);

        // 프레임에 컴포넌트 추가
        frame.add(backgroundPanel);

        // 프레임 보이기
        frame.setVisible(true);
    }


    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));
        JButton boardButton = new JButton("게시판");
        boardButton.addActionListener(e -> new BoardList()); // 게시판 페이지 열기
        menuBar.add(boardButton);

        // 메뉴 바에 추가적인 버튼 추가 가능
        // 예: JButton otherButton = new JButton("다른 메뉴");
        // menuBar.add(otherButton);

        return menuBar;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());

        // 상단 타이틀
        JLabel title = new JLabel("싸이월드", SwingConstants.CENTER);
        title.setFont(new Font("Stylish", Font.PLAIN, 25));
        mainContent.add(title, BorderLayout.NORTH);

        // 메인 컨텐츠 추가
        // 예: JPanel contentPanel = new JPanel();
        // mainContent.add(contentPanel, BorderLayout.CENTER);

        return mainContent;
    }

    public void setUserId(String username) {
        userIdLabel.setText("Welcome, " + username);
    }

    // 기타 필요한 메소드...
}