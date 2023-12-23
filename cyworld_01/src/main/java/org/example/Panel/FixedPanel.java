package org.example.Panel;

import org.Friend.FriendListManager;
import org.Friend.FriendManager;
import org.Friend.FriendRequestDialog;
import org.Utility.BackgroundPanel;
import org.Utility.UserSession;
import org.example.BoardList;
import org.example.MiniHomepage;
import org.example.Panel.GifPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.example.Panel.GifPanel;

public class FixedPanel extends JPanel {
    private JPanel menuBar;
    private JFrame frame;
    private JButton notificationButton;
    private FriendManager friendManager;
    private FriendListManager friendListManager;
    private String imagePath = "/image/main.jpg";
    private URL imageUrl = getClass().getResource(imagePath);
    private ProfilePanel profilePanel; // 프로필 패널
    private Consumer<List<String>> onNotificationButtonClick;

    public FixedPanel(FriendManager friendManager, FriendListManager friendListManager) {
        this.friendManager = friendManager;
        this.friendListManager = friendListManager;
       // this.onNotificationButtonClick = onNotificationButtonClick;
        initializeComponents(); // 컴포넌트 초기화
    }


    private void initializeComponents() {
        setLayout(new BorderLayout()); // 레이아웃 설정

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // 세로로 컴포넌트를 나열

        // 프로필 사진 추가
        ImageIcon profileImageIcon = new ImageIcon(getClass().getResource("/image/DefaultImage.jpg"));
        Image profileImage = profileImageIcon.getImage();
        JLabel profileLabel = new JLabel(new ImageIcon(profileImage));
        leftPanel.add(profileLabel);

        // 버튼 추가
        JButton changePicButton = new JButton("사진 변경");
        JButton friendRequestButton = new JButton("일촌 신청");
        JButton friendListButton = new JButton("일촌 목록");
        leftPanel.add(changePicButton);
        leftPanel.add(friendRequestButton);
        leftPanel.add(friendListButton);

        // 각 버튼에 리스너 추가
        changePicButton.addActionListener(e -> {
            // 사진 변경 로직 구현
            JOptionPane.showMessageDialog(this, "사진 변경 기능 구현 필요!");
        });

        friendRequestButton.addActionListener(e -> {
            // 일촌 신청 로직 구현
           friendListManager.openFriendSearchDialog();
        });

        friendListButton.addActionListener(e -> {
            // 일촌 목록 보기 로직 구현
            friendListManager.showFriendListDialog();
        });

        add(leftPanel, BorderLayout.WEST);


    }


    private void initializeProfilePanel() {
        // 프로필 사진 로드 및 설정
        ImageIcon profileImageIcon = new ImageIcon(getClass().getResource("/image/DefaultImage.jpg"));
        Image profileImage = profileImageIcon.getImage();
        profilePanel = new ProfilePanel(profileImage);
        profilePanel.setBounds(75, 135, profileImageIcon.getIconWidth(), profileImageIcon.getIconHeight());

        // 버튼 추가: 사진 변경, 일촌 신청, 일촌 목록
        addButton("사진변경", 95, 120 + profileImageIcon.getIconHeight(), e -> profilePanel.uploadAndResizeImage());
        addButton("일촌신청", 95, 240 + profileImageIcon.getIconHeight(), e -> friendListManager.openFriendSearchDialog());
        addButton("일촌목록", 95, 270 + profileImageIcon.getIconHeight(), e -> {
            try {
                friendListManager.showFriendListDialog();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "친구 목록을 불러오는데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addButton(String text, int x, int y, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 100, 20);
        button.addActionListener(actionListener);
        this.add(button);
    }
    private void initializeNotificationButtons() {
        // 알림 버튼 추가
        JButton notificationButton = createIconButton("/image/notification.png", 170, 100, e -> {
            {
                try {
                    // FriendManager를 통해 친구 요청 목록을 가져옵니다.
                    List<String> friendRequests = friendManager.getPendingFriendRequests(UserSession.getInstance().getUserId());

                    // FriendRequestDialog 인스턴스를 생성합니다.
                    onNotificationButtonClick.accept(friendRequests);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "친구 요청을 불러오는데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 메시지 버튼 추가
        JButton messageButton = createIconButton("/image/email.png", 200, 100, e -> {
            // 메시지 버튼 로직
        });

        this.add(notificationButton);
        this.add(messageButton);
    }

    private JButton createIconButton(String iconPath, int x, int y, ActionListener actionListener) {
        URL iconUrl = getClass().getResource(iconPath);
        ImageIcon icon = new ImageIcon(iconUrl);
        JButton button = new JButton(icon);
        button.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(actionListener);
        return button;
    }


    private void setupButtonListeners(JButton changePicButton, JButton friendRequestButton, JButton friendListButton) {
        // 사진 변경 버튼
        changePicButton.addActionListener(e -> profilePanel.uploadAndResizeImage());

        // 일촌신청 버튼
        friendRequestButton.addActionListener(e -> friendListManager.openFriendSearchDialog());

        // 일촌목록 버튼
        friendListButton.addActionListener(e -> {
            try {
                friendListManager.showFriendListDialog();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "친구 목록을 불러오는데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));

        JButton boardButton1 = new JButton("홈화면");
        JButton boardButton2 = new JButton("프로필");
        JButton boardButton3 = new JButton("게시판");
        JButton boardButton4 = new JButton("방명록");
        JButton boardButton5 = new JButton("사진첩");
        menuBar.add(Box.createHorizontalStrut(0)); // 수평 간격 추가
        // 원하는 순서로 버튼을 추가로 변경
        menuBar.add(boardButton1); // 홈화면 버튼을 마지막으로 추가
        menuBar.add(boardButton3); // 게시판 버튼을 먼저 추가
        menuBar.add(boardButton2); // 프로필 버튼을 다음으로 추가
        menuBar.add(boardButton4); // 방명록 버튼을 다음으로 추가
        menuBar.add(boardButton5); // 사진첩 버튼을 다음으로 추가
        // 버튼에 대한 동작 설정
        boardButton1.addActionListener(e -> {
            // 홈화면 버튼 클릭 시 수행할 동작 추가
        });

        boardButton2.addActionListener(e -> {
            // 프로필 버튼 클릭 시 수행할 동작 추가
        });

        boardButton3.addActionListener(e -> new BoardList());
        // 게시판 버튼 클릭 시 수행할 동작 추가

        boardButton4.addActionListener(e -> {
            // 방명록 버튼 클릭 시 수행할 동작 추가
        });

        boardButton5.addActionListener(e -> {
            // 사진첩 버튼 클릭 시 수행할 동작 추가
        });
        // 다른 버튼에 대한 동작 설정
        // 버튼 크기 설정
        Dimension buttonSize = new Dimension(10, 50);
        for (JButton button : Arrays.asList(boardButton1, boardButton2, boardButton3, boardButton4, boardButton5)) {
            button.setPreferredSize(buttonSize);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setAlignmentX(Component.RIGHT_ALIGNMENT);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);
        }

        // 패널 속성 설정
        menuBar.setOpaque(false);

        // 버튼 사이 간격 설정
        int HorizontalStrutHeight = 11;
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));

        return menuBar;
    }
    public JPanel getMenuBar() {
        return menuBar;
    }
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        // 상단 타이틀
        mainContent.setOpaque(false); // 메인 컨텐츠 투명하게 설정
        return mainContent;
    }
}
