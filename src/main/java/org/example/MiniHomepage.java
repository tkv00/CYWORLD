package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.AWTEventMulticaster.add;

public class MiniHomepage {
    private LoginPage loginPage;
    private SignUppage signUpPage;
    private JLabel userIdLabel; // 사용자 ID를 표시할 레이블
    private String username; // 사용자 id을 저장할 변수

    public MiniHomepage() {
        signUpPage = new SignUppage();
        loginPage = new LoginPage(signUpPage,this);
        // 사용자 ID 레이블 초기화
        userIdLabel = new JLabel();
        add(userIdLabel); // 이 부분은 MiniHomepage의 레이아웃에 따라 달라질 수 있음
    }//

    private void add(JLabel userIdLabel) {
    }

    public static void main(String[] args) {
        new MiniHomepage().showLogin(); // 프로그램 시작 시 로그인 페이지를 표시

    }//
    private void showLogin() {
        loginPage.show();
    }
    public void showMainPage(){
        JFrame frame = new JFrame("싸이월드");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900); // 창 크기 설정
        frame.setLayout(new BorderLayout(15, 15));

        // 배경 설정
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBackground(new Color(163, 163, 163)); // 배경색 설정
        frame.add(backgroundPanel);

        // bookcover 패널
        JPanel bookcoverPanel = new JPanel();
        bookcoverPanel.setLayout(new BorderLayout());
        bookcoverPanel.setBackground(Color.GRAY);
        bookcoverPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#738186"), 1));
        bookcoverPanel.setPreferredSize(new Dimension(960, 660));
        backgroundPanel.add(bookcoverPanel, BorderLayout.CENTER);

        // page 패널
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BorderLayout());
        pagePanel.setBackground(Color.WHITE);
        pagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        bookcoverPanel.add(pagePanel);

        // 홈 섹션
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());
        pagePanel.add(homePanel, BorderLayout.CENTER);

        // 상단 타이틀 영역
        JLabel title = new JLabel("싸이월드", SwingConstants.CENTER);
        title.setFont(new Font("Stylish", Font.PLAIN, 25));
        homePanel.add(title, BorderLayout.NORTH);

        // 뉴스 섹션
        JPanel newsSection = new JPanel();
        newsSection.setLayout(new BoxLayout(newsSection, BoxLayout.Y_AXIS));
        JLabel newsTitle = new JLabel("오늘의 뉴스");
        newsSection.add(newsTitle);
        // 뉴스 섹션의 추가적인 구현...

        // 프로필 섹션
        JPanel profileSection = new JPanel();
        profileSection.setLayout(new BoxLayout(profileSection, BoxLayout.Y_AXIS));
        JLabel profileTitle = new JLabel("내 프로필");
        profileSection.add(profileTitle);


        // 메뉴 바
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));
        backgroundPanel.add(menuBar, BorderLayout.WEST);

        // 섹션들을 메인 패널에 추가
        homePanel.add(newsSection, BorderLayout.CENTER);
        homePanel.add(profileSection, BorderLayout.EAST);

        //게시판버튼
        JButton boardButton=new JButton("게시판");
        boardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BoardList();
                // 게시판 페이지 열기
            }
        });
        menuBar.add(boardButton);




        // 프레임 보이기
        frame.setVisible(true);
    }

    // MiniHomepage 클래스 내부
    public void setUserId(String username) {
        this.username=username;
        // userId를 사용하여 메인 페이지에 표시하는 로직
        // 예: JLabel에 userId를 설정
        userIdLabel.setText("Welcome, " + username);
    }

}