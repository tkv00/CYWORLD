package org.example;

import org.Friend.FriendListManager;
import org.Friend.FriendManager;
import org.Friend.FriendRequestDialog;
import org.Utility.*;
import org.example.Panel.GifPanel;
import org.example.Panel.MusicPlayerPanel;
import org.example.Panel.ProfilePanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
public class MiniHomepage extends JFrame {
    private PhotoGalleryManager photoGalleryManager;
    private FriendManager friendManager;
    private PhotoGalleryWindow photoGalleryWindow;
    private JFrame frame;
    String imagePath = "/image/main.jpg";
    private int DEFAULT_WIDTH = 143;
    private int DEFAULT_HEIGHT = 190;
    URL imageUrl = getClass().getResource(imagePath);
    private LoginPage loginPage;
    ProfileImageUpload profileImageUpload;
    private SignUppage signUpPage;
    private FriendListManager friendListManager; // 친구 목록 관리자 추가
    private JPanel friendsPanel;
    private ProfilePanel profilePanel;
    private String userId;

    //버튼 변수 설정
    //사진변경
    private JButton changeImageButton;
    //일촌신청
    private JButton newButton2;
    //일촌목록
    private JButton newButton3;
    //홈화면 버튼
    private JButton boardButton1;
    //우측 게시판
    private JButton boardButton3;
    //프로필
    private JButton boardButton2;
    //방명록
    private JButton boardButton4;
    //사진첩
    private JButton boardButton5;
    //알림버튼
    private JButton notificationButton;
    //메세지버튼
    private JButton messageButton;




    private WriteBoardManager writeBoardManager;

    public MiniHomepage() {
        //버튼 초기화
        changeImageButton=new JButton("사진 변경");
        newButton2=new JButton("일촌신청");
        newButton3=new JButton("일촌목록");
        boardButton1=new JButton("홈화면");
        boardButton3=new JButton("게시판");
        messageButton=new JButton(new ImageIcon(getClass().getResource("/image/email.png")));
        boardButton2=new JButton("프로필");
        boardButton4=new JButton("방명록");
        boardButton5=new JButton("사진첩");
        notificationButton=new JButton(new ImageIcon(getClass().getResource("/image/notification.png")));


        // 기본 프레임 설정
        this.friendManager = new FriendManager();
        photoGalleryManager=new PhotoGalleryManager(this,this.userId);
        profileImageUpload=new ProfileImageUpload();
        loginPage=new LoginPage(signUpPage,this);
        // FriendListManager 인스턴스 생성
        signUpPage = new SignUppage(loginPage);

        this.friendsPanel = new JPanel();
        this.friendsPanel.setLayout(new BoxLayout(this.friendsPanel, BoxLayout.Y_AXIS));
        loginPage = new LoginPage(signUpPage, this);
        this.friendListManager = new FriendListManager(this, friendManager, friendsPanel, notificationButton);
        // JScrollPane로 감싸서 스크롤 기능 추가
        JScrollPane friendsScrollPane = new JScrollPane(friendsPanel);
        friendsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // 창을 화면에 표시
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치
        this.writeBoardManager = new WriteBoardManager();
    }
    // 로그인 성공 후 userId를 설정하는 메서드


    public static void main(String[] args) {
        new MiniHomepage().showLogin();

    }

    private void showLogin() {
        loginPage.show();
    }

    // 수평 패널을 생성하는 메서드
    public void showMainPage() {
        frame = new JFrame("싸이월드");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        // 이미지 로드 부분
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        if (imageIcon.getIconWidth() == -1) {
            System.err.println("이미지 로딩 실패: " + imagePath);
            return; // return 문이 없을 경우 에러가 나므로, 일단 null을 반환하도록 변경
        }
        Image backgroundImage = imageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setBounds(0, 0, 888, 588);
        backgroundPanel.setOpaque(false); // 배경 이미지 패널 투명도 설정

        // JLayeredPane 생성 및 설정
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(888, 588));
        // 음악 플레이어 패널 생성 및 추가
        MusicPlayerPanel musicPlayerPanel = new MusicPlayerPanel();
        musicPlayerPanel.setBounds(480, 0, 400, 53); // 위치와 크기 조정

        layeredPane.add(musicPlayerPanel, Integer.valueOf(500)); // 레이어 설정


        // 배경 패널 추가
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        // 프로필 이미지 로드 및 패널 설정
        profilePanel = new ProfilePanel(null);
        profilePanel.setBounds(75, 135, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        layeredPane.add(profilePanel, Integer.valueOf(500));
        // If there is a user logged in, load their profile image
        if (userId != null && !userId.trim().isEmpty()) {
            profilePanel.loadAndSetUserProfileImage(userId);
        } else {
            // If no user is logged in, set a default image or handle accordingly
            profilePanel.setDefaultImage();
        }


        changeImageButton.setBounds(95, 340, 100, 20);
        layeredPane.add(changeImageButton, Integer.valueOf(600));
        changeImageButton.addActionListener(e -> uploadAndSetNewProfileImage());


        newButton2.setBounds(95, 480, 100, 20); // 위치 설정 (가로: 100, 세로: 30)
        layeredPane.add(newButton2, Integer.valueOf(501)); // 새로운 버튼을 적절한 레이어에 추가
        newButton2.addActionListener(e -> friendListManager.openFriendSearchDialog());

        newButton3.setBounds(95, 500, 100, 20); // 위치 설정 (가로: 100, 세로: 30)
        layeredPane.add(newButton3, Integer.valueOf(501)); // 새로운 버튼을 적절한 레이어에 추가
        newButton3.addActionListener(e -> {
            try {
                friendListManager.showFriendListDialog(); // FriendListManager를 사용하여 친구 목록 다이얼로그 표시
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "친구 목록을 불러오는데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton rightButton1 = new JButton("사진첩");
        rightButton1.setBounds(520, 120, 100, 20);
        layeredPane.add(rightButton1, Integer.valueOf(501)); // 새로운 버튼을 적절한 레이어에 추가
        rightButton1.setOpaque(true);  // 배경색을 투명으로 설정
        rightButton1.setContentAreaFilled(false);  // 내용 영역을 투명으로 설정
        rightButton1.setBorderPainted(false);  // 테두리를 숨김

        JButton rightButton2 = new JButton("방명록");
        rightButton2.setBounds(520, 155, 100, 20);
        layeredPane.add(rightButton2, Integer.valueOf(501)); // 새로운 버튼을 적절한 레이어에 추가
        rightButton2.setOpaque(true);  // 배경색을 투명으로 설정
        rightButton2.setContentAreaFilled(false);  // 내용 영역을 투명으로 설정

        rightButton2.setBorderPainted(false);  // 테두리를 숨김

        // gifPanel 및 아바타 초기화 및 추가
        GifPanel gifPanel = new GifPanel(); // GifPanel 인스턴스 생성
        gifPanel.setBounds(380, 250, gifPanel.getPreferredSize().width, gifPanel.getPreferredSize().height);
        layeredPane.add(gifPanel, Integer.valueOf(300));

        // 메뉴 바 패널을 생성합니다.
        JPanel menuBar = createMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBounds(700, 75, 200, 190);
        layeredPane.add(menuBar, JLayeredPane.MODAL_LAYER); // 메뉴 바를 적절한 레이어에 추가

        // 최근 게시물 패널 초기화
        JPanel recentPostPanel = new JPanel();
        recentPostPanel.setBounds(255, 86, 150, 150);
        recentPostPanel.setOpaque(false); // 패널의 불투명성을 비활성화


        // 최근 게시물 패널을 생성하고 추가합니다.
        JPanel recentPost = createRecentPostPanel(recentPostPanel);
        layeredPane.add(recentPost, JLayeredPane.MODAL_LAYER);

        // 레이아웃 매니저 설정 (세로 방향으로 정렬)
        recentPost.setLayout(new BoxLayout(recentPost, BoxLayout.Y_AXIS));

        // 최근 게시물 텍스트
        JLabel recentPostLabel = new JLabel("최근 게시물");
        recentPostLabel.setOpaque(false); // 레이블의 배경을 투명하게 설정
        recentPost.add(recentPostLabel);

        // 수평 여백 추가
        recentPost.add(Box.createVerticalStrut(5));

        // 메인 컨텐츠를 gifPanel 내부에 추가
        JPanel mainContent = createMainContent();
        gifPanel.add(mainContent);
        mainContent.setBounds(0, 0, gifPanel.getWidth(), gifPanel.getHeight());
        mainContent.setOpaque(false); // 메인 컨텐츠를 투명하게 설정


        notificationButton.setContentAreaFilled(false);  // 내용 영역을 투명으로 설정
        notificationButton.setBorderPainted(false);  // 테두리를 숨김
        notificationButton.setBounds(170, 100, 20, 20);
        notificationButton.addActionListener(e -> {
            try {
                // FriendManager를 통해 친구 요청 목록을 가져옵니다.
                List<String> friendRequests = friendManager.getPendingFriendRequests(userId);

                // FriendRequestDialog 인스턴스를 생성합니다.
                FriendRequestDialog requestDialog = new FriendRequestDialog(MiniHomepage.this, userId, friendRequests);

                // FriendRequestDialog를 표시합니다.
                requestDialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MiniHomepage.this, "친구 요청을 불러오는데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        layeredPane.add(notificationButton, Integer.valueOf(JLayeredPane.POPUP_LAYER));

        //쪽지 버튼 추가
        URL messageUrl = getClass().getResource("/image/email.png");
        ImageIcon messageIcon = new ImageIcon(messageUrl);
        JButton messageButton = new JButton(messageIcon);

        messageButton.setContentAreaFilled(false);  // 내용 영역을 투명으로 설정
        messageButton.setBorderPainted(false);  // 테두리를 숨김

        messageButton.setBounds(200, 100, 20, 20);
        layeredPane.add(messageButton, Integer.valueOf(JLayeredPane.POPUP_LAYER));
        // 메세지 버튼에 액션 리스너 추가
        messageButton.addActionListener(e -> {
            try {
                // FriendListManager 인스턴스가 이미 있어야 합니다.
                friendListManager.showMessagesDialog();  // 쪽지 패널을 표시하는 메소드 호출
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "쪽지를 불러오는 데 문제가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

// 메세지 버튼을 레이어드 패인 또는 메인 프레임에 추가
        layeredPane.add(messageButton, Integer.valueOf(JLayeredPane.POPUP_LAYER));
        frame.setLayeredPane(layeredPane); // 프레임에 레이어드 판을 설정
        frame.setVisible(true);

        gifPanel.requestFocusInWindow();

        // 동영상 패널 생성 및 추가
        JPanel videoPanel = new JPanel();
        JLabel videoLabel = new JLabel("동영상");
        videoLabel.setForeground(Color.BLACK); // 텍스트 색상을 하얀색으로 설정
        videoPanel.add(videoLabel);
        recentPost.add(videoPanel);
        videoPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        recentPost.add(Box.createVerticalStrut(10));

        // 게시판 패널 생성 및 추가
        JPanel boardPanel = new JPanel(new BorderLayout());
        JLabel boardTextLabel = new JLabel("게시판");
        boardTextLabel.setForeground(Color.BLACK); // 텍스트 색상을 설정
        boardPanel.add(boardTextLabel);
        // 최근 게시물 제목 가져오기
        String latestPostTitle = writeBoardManager.getLatestPostTitle();

        // 최근 게시물 제목 레이블
        JLabel recentPostTitleLabel = new JLabel("최근 게시물: " + latestPostTitle);
        recentPostTitleLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
        // 각 레이블을 패널에 추가
        boardPanel.add(boardTextLabel, BorderLayout.WEST);
        boardPanel.add(recentPostTitleLabel, BorderLayout.EAST);
        recentPostPanel.add(recentPostTitleLabel); // 최근 게시물 레이블을 패널에 추가
        recentPost.add(boardPanel);
        boardPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        recentPost.add(Box.createVerticalStrut(10));
        // 최근 게시물을 클릭할 수 있는 버튼 생성
        JButton recentPostButton = new JButton();
        recentPostButton.setLayout(new BorderLayout());
        recentPostButton.add(recentPostTitleLabel, BorderLayout.CENTER);
        recentPostButton.setContentAreaFilled(false);
        recentPostButton.setBorderPainted(false);
        recentPostButton.addActionListener(e -> {
            int[] latestPostInfo = writeBoardManager.getLatestPostIdAndUserId(); // 게시글의 ID와 userId를 가져오는 메서드
            int postId = latestPostInfo[0]; // 게시글 ID

            // BoardList 클래스의 showPostDetailsInNewWindow 메서드를 호출하여 게시글을 새 창에 표시
            writeBoardManager.showPostDetailsInNewWindow(postId);
        });

        // 최근 게시물 버튼을 최근 게시물 패널에 추가
        recentPost.add(recentPostButton);

        // 갤러리 패널 생성 및 추가
        JPanel galleryPanel = new JPanel();
        JLabel galleryLabel = new JLabel("갤러리");
        galleryLabel.setForeground(Color.BLACK); // 텍스트 색상을 하얀색으로 설정
        galleryPanel.add(galleryLabel);
        recentPost.add(galleryPanel);
        galleryPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        recentPost.add(Box.createVerticalStrut(10));

        // 사진첩 패널 생성 및 추가
        JPanel photoGalleryPanel = new JPanel();
        JLabel photoGalleryLabel = new JLabel("사진첩");
        photoGalleryLabel.setForeground(Color.BLACK); // 텍스트 색상을 하얀색으로 설정
        photoGalleryPanel.add(photoGalleryLabel);
        recentPost.add(photoGalleryPanel);
        photoGalleryPanel.setOpaque(false); // 패널의 불투명성을 비활성화
    }

    private void uploadAndSetNewProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("새 프로필 이미지 선택");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Image newProfileImage = new ImageIcon(selectedFile.getAbsolutePath()).getImage();
                profilePanel.updateProfileImage(newProfileImage); // 프로필 이미지 변경
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "이미지 로딩 실패", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 사진첩 다이얼로그를 열기 위한 메서드
    private void openPhotoGalleryWindow() {
        if (this.userId == null || this.userId.trim().isEmpty()) {
            System.err.println("User ID is null or empty in openPhotoGalleryWindow");
            return;  // Handle this situation appropriately
        }

        // Pass the userId to the PhotoGalleryWindow or Manager
        PhotoGalleryWindow galleryWindow = new PhotoGalleryWindow(photoGalleryManager, userId,true);
        galleryWindow.setVisible(true);
    }
    private JPanel createRecentPostPanel(JPanel recentPostPanel) {
        return recentPostPanel;
    }
    // 사진첩 창을 열기 위한 메서드
    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));

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

        boardButton5.addActionListener(e ->openPhotoGalleryWindow());
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

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false); // 메인 컨텐츠 투명하게 설정
        return mainContent;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        photoGalleryManager = new PhotoGalleryManager(this, this.userId);
    }
    //버튼 초기값 설정 (true)
    //프로필버튼
    public JButton getProfileButton() {
        return boardButton2;
    }
    //게시판 버튼
    public JButton getBoardButton() {
        return boardButton3;
    }
    //사진첩버튼
    public JButton getPhotoGalleryButton() {
        return boardButton5;
    }
    //방명록버튼
    public JButton getCommentButton() {
        return boardButton4;
    }
    //메세지버튼
    public JButton getMessageButton(){
        return messageButton;
    }
    //사진변경
    public JButton getChangeImageButton(){
        return changeImageButton;
    }
    //알림 버튼
    public JButton getNotificationButton() {
        return notificationButton;
    }
    //일촌신청
    public JButton getFriend(){
       return newButton2;
    }
    //일촌목록
    public JButton getFriendList(){
        return newButton3;
    }
    // PhotoGalleryManager 인스턴스를 반환하는 메서드
    public PhotoGalleryManager getPhotoGalleryManager() {
        return photoGalleryManager;
    }
    public void setPhotoGalleryWindow(PhotoGalleryWindow window) {
        this.photoGalleryWindow = window;
    }
    // 현재 열려있는 PhotoGalleryWindow 인스턴스를 반환하는 메서드
    public PhotoGalleryWindow getPhotoGalleryWindow() {
        return this.photoGalleryWindow;
    }
}