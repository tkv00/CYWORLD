package org.example;
<<<<<<< HEAD
=======

import org.Friend.FriendListManager;
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
import org.Friend.FriendManager;
import org.Friend.FriendRequestDialog;
import org.Utility.*;
import org.example.Panel.GifPanel;
import org.example.Panel.MusicPlayerPanel;
import org.example.Panel.ProfilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
<<<<<<< HEAD

import javax.swing.JPanel;


public class MiniHomepage {
    private JButton notificationButton;
=======
public class MiniHomepage extends JFrame {
    private PhotoGalleryManager photoGalleryManager;
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
    private FriendManager friendManager;
    private PhotoGalleryWindow photoGalleryWindow;
    private JFrame frame;
<<<<<<< HEAD


    private Timer playTimeUpdateTimer; // 재생 시간 업데이트 타이머
    String imagePath = "/main.jpg";
=======
    String imagePath = "/image/main.jpg";
    private int DEFAULT_WIDTH = 143;
    private int DEFAULT_HEIGHT = 190;
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
    URL imageUrl = getClass().getResource(imagePath);
    private LoginPage loginPage;
    ProfileImageUpload profileImageUpload;
    private SignUppage signUpPage;
    private FriendListManager friendListManager; // 친구 목록 관리자 추가
    private JPanel friendsPanel;
    private ProfilePanel profilePanel;
    private String userId;
    private JPanel commentPanel; // 방명록 목록 보여주는 패널
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




<<<<<<< HEAD
    public MiniHomepage() {
        this.friendManager = new FriendManager();
        signUpPage = new SignUppage();
        notificationButton=new JButton();
        loginPage = new LoginPage(signUpPage, this);


        userIdLabel = new JLabel();
        // 재생 시간 레이블
        playTimeLabel = new JLabel("00:00");
        musicSlider = new JSlider();
        // 초기 레이블 텍스트 설정 (예시)
        userIdLabel.setText("Welcome, Guest");
        musicPlayer = new MusicPlayer(musicFiles);
        // 움직이는 GIF 및 아바타 초기화
        initializeGifAndAvatar();

        initializePlayTimeUpdater();
    }


    private void initializeGifAndAvatar() {
        // 움직이는 GIF 불러오기
        gifIcon = new ImageIcon(getClass().getResource("/MainRoom.gif")); // GIF 파일 경로 수정 필요

        // 캐릭터 이미지 로드
        characterImages = new ImageIcon[16]; // 총 16개의 이미지
        for (int i = 0; i < 16; i++) {
            characterImages[i] = new ImageIcon(getClass().getResource("/character_" + (i + 1) + ".png"));
        }

        // 아바타 초기화 추가
        avatarLabel = new JLabel();
        avatarLabel.setSize(new Dimension(50, 50)); // 예시 크기, 필요에 따라 조정
        characterPosition = new Point(50, 50); // 초기 캐릭터 위치 설정
        // GIF 패널 초기화
        gifPanel = new JPanel(null) { // 위치 수동 조정을 위한 레이아웃 설정
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (this.getWidth() - gifIcon.getIconWidth()) / 2;
                int y = 0; // 상단에 위치시키기 위해 y 좌표는 0으로 설정
                g.drawImage(gifIcon.getImage(), x, y, this);

                //캐릭터그리기
                int characterFrameIndex = getCharacterFrameIndex();
                g.drawImage(characterImages[characterFrameIndex].getImage(), characterPosition.x, characterPosition.y, this);
=======
    private WriteBoardManager writeBoardManager;
    private UserSession userSession;
    private String username;
    private ProfileEditor profileEditor; // 클래스 레벨에 profileEditor 선언
    public static class FontManager {
        public  static Font loadFont(String fontPath, int style, float size) {
            try {
                InputStream is = org.Utility.FontManager.class.getResourceAsStream(fontPath);
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                return customFont.deriveFont(style, size);
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
                System.err.println("폰트를 불러오는데 문제가 발생했습니다.");
                return new Font("SansSerif", style, (int) size);
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
            }
        }
    }
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
        boardButton4.addActionListener(e -> showCommentsUI());
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
        setVisible(true);
        Image defaultImage = new ImageIcon(getClass().getResource("/image/DefaultImage.jpg")).getImage();
        // profilePanel 생성 및 추가
        profilePanel = new ProfilePanel(defaultImage);
        profilePanel.setBounds(75, 135, DEFAULT_WIDTH, DEFAULT_HEIGHT); // 위치 및 크기 설정
        friendsScrollPane.add(profilePanel, Integer.valueOf(500));
        this.writeBoardManager = new WriteBoardManager();
        this.profileEditor = new ProfileEditor(this.username);
    }
    // 로그인 성공 후 userId를 설정하는 메서드


    public static void main(String[] args) {
        new MiniHomepage().showLogin();
    }
    private void showLogin() {
        loginPage.show();
    }
<<<<<<< HEAD

    //음악재생 관련 메소드 모음
    private void initializePlayTimeUpdater() {
        playTimeUpdateTimer = new Timer(1000, e -> updatePlayTime());
        playTimeUpdateTimer.start();
    }

    private void updatePlayTime() {
        if (musicPlayer != null) {
            long microSeconds = musicPlayer.getCurrentPosition();
            long totalMicroSeconds = musicPlayer.getTotalDuration(); // 전체 재생 길이 가져오기
            long seconds = microSeconds / 1000000;
            long totalSeconds = totalMicroSeconds / 1000000;
            long min = seconds / 60;
            long sec = seconds % 60;
            playTimeLabel.setText(String.format("%02d:%02d", min, sec));

// 슬라이더의 값을 현재 재생 위치에 따라 업데이트
            if (!musicSlider.getValueIsAdjusting() && totalSeconds > 0) {
                int sliderValue = (int) (seconds * 100 / totalSeconds);
                musicSlider.setValue(sliderValue);
            }
        }
    }
    // 최근 게시물 패널을 생성하는 메서드


    private JPanel createTopBar() {
        // 음악 컨트롤 패널 설정
        JPanel musicControlPanel = new JPanel();
        musicControlPanel.setLayout(new BoxLayout(musicControlPanel, BoxLayout.X_AXIS)); // 세로 정렬
        musicControlPanel.setOpaque(false);

        // 상단 컨트롤 버튼 패널 생성
        JPanel controlButtonPanel = new JPanel();
        controlButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 버튼 간격 설정
        controlButtonPanel.setOpaque(false);

        // 컨트롤 버튼들 생성 및 설정
        JToggleButton playPauseButton = new JToggleButton("▶", false); // 재생/일시정지 토글 버튼
        JButton nextButton = createTransparentButton(">>");
        JButton prevButton = createTransparentButton("<<");
        controlButtonPanel.add(prevButton);
        controlButtonPanel.add(playPauseButton);
        controlButtonPanel.add(nextButton);

        // 재생 시간 레이블 설정
        playTimeLabel = new JLabel("00:00");
        playTimeLabel.setHorizontalAlignment(JLabel.LEFT);
        playTimeLabel.setOpaque(false);

        // 노래 정보 레이블 초기화 및 설정
        songInfoLabel = new JLabel("Y-Free Style");
        songInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
        songInfoLabel.setOpaque(false);

        // 뮤직 슬라이더 설정
        musicSlider = new JSlider();
        musicSlider.setOpaque(false);

        // 컨트롤 패널에 컴포넌트들 추가
        musicControlPanel.add(controlButtonPanel);
        musicControlPanel.add(playTimeLabel);
        musicControlPanel.add(musicSlider);
        musicControlPanel.add(songInfoLabel);

        // musicControlPanel을 가운데 정렬하기 위한 래퍼 패널
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(musicControlPanel);



        playPauseButton.addActionListener(e -> {
            if (playPauseButton.isSelected()) {
                playPauseButton.setText("||");
                if (musicPlayer.getCurrentPlayingIndex() == -1) {
                    musicPlayer.playMusicAtIndex(0); // 첫 번째 곡 재생
                } else {
                    musicPlayer.playMusic(); // 계속 재생
                }
            } else {
                playPauseButton.setText("▶");
                musicPlayer.pauseMusic(); // 일시 정지
            }
            gifPanel.requestFocusInWindow();
        });

        nextButton.addActionListener(e -> {
            // 다음 곡 버튼이 눌렸을 때의 동작
            musicPlayer.playNext();
            int newIndex = musicPlayer.getCurrentPlayingIndex();
            songInfoLabel.setText(songInfo[newIndex][0] + " - " + songInfo[newIndex][1]);
            gifPanel.requestFocusInWindow();
            // 필요한 경우 여기에 추가 동작을 구현하세요.
        });
        prevButton.addActionListener(e -> {
            // 이전 곡 버튼이 눌렸을 때의 동작
            musicPlayer.playPrevious();
            int newIndex = musicPlayer.getCurrentPlayingIndex();
            if (newIndex >= 0 && newIndex < songInfo.length) {
                songInfoLabel.setText(songInfo[newIndex][0] + " - " + songInfo[newIndex][1]);
            } else {
                // 인덱스가 유효하지 않은 경우에 대한 처리
                System.out.println("유효하지 않은 인덱스입니다: " + newIndex);
            }
            gifPanel.requestFocusInWindow();
            // 필요한 경우 여기에 추가 동작을 구현하세요.
        });




        // 상단 바 패널 생성 및 래퍼 패널 추가
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        // 상단 바의 선호 크기 설정
        topBar.setPreferredSize(new Dimension(888, 30)); // 이 값을 적절하게 조정
        topBar.add(wrapperPanel, BorderLayout.NORTH);

        // 상단 바 패널 반환
        return topBar;


    }

    private JButton createTransparentButton(String text) {
        JButton button = new JButton(text);

        // 버튼의 속성 설정
        button.setOpaque(false); // 버튼을 불투명하지 않게 설정
        button.setContentAreaFilled(false); // 버튼 영역을 채우지 않도록 설정
        button.setBorderPainted(false); // 버튼의 테두리를 그리지 않도록 설정
        button.setFocusPainted(false); // 포커스 표시를 그리지 않도록 설정
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 오버 시 커서 변경

        // 필요에 따라 폰트, 크기, 색상 등을 추가로 설정할 수 있음
        // button.setFont(new Font("SansSerif", Font.BOLD, 12));
        // button.setForeground(Color.WHITE);

        return button;
    }
=======
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
    // 수평 패널을 생성하는 메서드
    public void showMainPage() {
        frame = new JFrame("싸이월드");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
<<<<<<< HEAD

=======
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
        // 이미지 로드 부분
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        if (imageIcon.getIconWidth() == -1) {
            System.err.println("이미지 로딩 실패: " + imagePath);
            return; // return 문이 없을 경우 에러가 나므로, 일단 null을 반환하도록 변경
        }
        Image backgroundImage = imageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
<<<<<<< HEAD
        backgroundPanel.setBounds(0, 0, 888, 598);
        backgroundPanel.setOpaque(false); // 배경 이미지 패널 투명도 설정

=======
        backgroundPanel.setBounds(0, 0, 888, 588);
        backgroundPanel.setOpaque(false); // 배경 이미지 패널 투명도 설정
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
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

<<<<<<< HEAD
        // 상단 바 패널을 생성합니다.
        JPanel topBar = createTopBar();
        topBar.setOpaque(false); // 상단 바 패널 투명도 설정
        // 상단 바를 레이어드 패널에 추가
        layeredPane.add(topBar, Integer.valueOf(JLayeredPane.PALETTE_LAYER));
        topBar.setBounds(0, 0, 888, 30); // 위치와 크기를 지정합니다.

=======
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
        // 메뉴 바 패널을 생성합니다.
        JPanel menuBar = createMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBounds(700, 75, 200, 190);
        layeredPane.add(menuBar, JLayeredPane.MODAL_LAYER); // 메뉴 바를 적절한 레이어에 추가

<<<<<<< HEAD
        // 최근 게시물 패널 초기화
        JPanel recentPostPanel = new JPanel();
        recentPostPanel.setBounds(250, 89, 150, 150);
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

        // ...
=======
        //게 시 물 패널 초기화
        JPanel postPanel = new JPanel();
        postPanel.setBounds(265, 88, 150, 150);
        postPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        //게 시 물 패널을 생성하고 추가합니다.
        JPanel Post = PostPanel(postPanel);
        layeredPane.add(Post, JLayeredPane.MODAL_LAYER);

        // 레이아웃 매니저 설정 (세로 방향으로 정렬)
        Post.setLayout(new BoxLayout(Post, BoxLayout.Y_AXIS));

        //게 시 물 텍스트
        JLabel postLabel = new JLabel("게 시 물");
        postLabel.setOpaque(false); // 레이블의 배경을 투명하게 설정
        Post.add(postLabel);

        // 수평 여백 추가
        Post.add(Box.createVerticalStrut(5));
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2

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
<<<<<<< HEAD



        // 프레임에 레이어드 패인 추가 및 표시
=======
        // 메세지 버튼을 레이어드 패인 또는 메인 프레임에 추가
        layeredPane.add(messageButton, Integer.valueOf(JLayeredPane.POPUP_LAYER));
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
        frame.setLayeredPane(layeredPane); // 프레임에 레이어드 판을 설정
        frame.setVisible(true);

        gifPanel.requestFocusInWindow();
<<<<<<< HEAD

        // 동영상 패널 생성 및 추가
        JPanel videoPanel = new JPanel();
        JLabel videoLabel = new JLabel("동영상");
        videoLabel.setForeground(Color.WHITE); // 텍스트 색상을 하얀색으로 설정
        videoPanel.add(videoLabel);
        recentPost.add(videoPanel);
        videoPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        recentPost.add(Box.createVerticalStrut(10));

        // 게시판 패널 생성 및 추가
        JPanel boardPanel = new JPanel();
        JLabel boardLabel = new JLabel("게시판");
        boardLabel.setForeground(Color.WHITE); // 텍스트 색상을 하얀색으로 설정
        boardPanel.add(boardLabel);
        recentPost.add(boardPanel);
        boardPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        recentPost.add(Box.createVerticalStrut(10));

        // 갤러리 패널 생성 및 추가
        JPanel galleryPanel = new JPanel();
        JLabel galleryLabel = new JLabel("갤러리");
        galleryLabel.setForeground(Color.WHITE); // 텍스트 색상을 하얀색으로 설정
        galleryPanel.add(galleryLabel);
        recentPost.add(galleryPanel);
        galleryPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        recentPost.add(Box.createVerticalStrut(10));

        // 사진첩 패널 생성 및 추가
        JPanel photoGalleryPanel = new JPanel();
        JLabel photoGalleryLabel = new JLabel("사진첩");
        photoGalleryLabel.setForeground(Color.WHITE); // 텍스트 색상을 하얀색으로 설정
        photoGalleryPanel.add(photoGalleryLabel);
        recentPost.add(photoGalleryPanel);
        photoGalleryPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 여기에 추가적으로 작성할 부분이 있는 경우 계속해서 코드를 보완하실 수 있습니다.
    }
    //쪽지 다이얼로그 띄우기
    private void showMessageListDialog() throws SQLException {
        JDialog messageListDialog = new JDialog(frame, "쪽지 목록", true);
        messageListDialog.setLayout(new BorderLayout());
=======
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2

        // 급상승 패널 생성 및 추가
        JPanel UpPanel = new JPanel();
        JLabel UpLabel = new JLabel("급상승");
        UpLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
        Post.add(UpPanel);
        UpPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        Post.add(Box.createVerticalStrut(10));

        // 추천 패널 생성 및 추가
        JPanel RecommendPanel = new JPanel();
        JLabel RecommendLabel = new JLabel("추 천");
        RecommendLabel.setForeground(Color.BLACK); // 텍스트 색상을 설정
        Post.add(RecommendPanel);
        RecommendPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 인기 패널 생성 및 추가
        JPanel HotPanel = new JPanel();
        JLabel HotLabel = new JLabel("인 기");
        HotLabel.setForeground(Color.BLACK); // 텍스트 색상
        Post.add(HotPanel);
        HotPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        Post.add(Box.createVerticalStrut(10));

        // 신규 패널 생성 및 추가
        JPanel NewPanel = new JPanel();
        JLabel NewLabel = new JLabel("신 규");
        NewLabel.setForeground(Color.BLACK); // 텍스트 색상
        Post.add(NewPanel);
        NewPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 최근 게시물 제목 가져오기
        String latestPostTitle = writeBoardManager.getLatestPostTitle();
        int latestPostId = writeBoardManager.getLatestPostId();
        // 각 패널에 게시글 제목을 표시하고 클릭 이벤트를 처리하는 코드 추가
        JPanel[] panels = {UpPanel, RecommendPanel, HotPanel, NewPanel};
        String[] labelsText = {"급상승", "추 천", "인 기", "신 규"};

        for (int i = 0; i < panels.length; i++) {
            JPanel panel = panels[i];
            JLabel label = new JLabel(labelsText[i]);
            label.setForeground(Color.BLACK);
            panel.add(label);

            JLabel postTitleLabel = new JLabel(latestPostTitle);
            postTitleLabel.setForeground(Color.BLUE);
            postTitleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서를 손 모양으로 변경
            panel.add(postTitleLabel);
            // 클릭 이벤트
            postTitleLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    writeBoardManager.showPostDetailsInNewWindow(latestPostId);
                }
            });
            panel.setOpaque(false);
            Post.add(panel);
        }
        // 최근 게시물 제목 레이블
        JLabel recentPostTitleLabel = new JLabel(latestPostTitle);
        recentPostTitleLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
        // 각 레이블을 패널에 추가
        postPanel.add(postLabel, BorderLayout.WEST);
        UpPanel.add(recentPostTitleLabel, BorderLayout.EAST);
        postPanel.add(recentPostTitleLabel); // 최근 게시물 레이블을 패널에 추가
        Post.add(UpPanel);
        UpPanel.setOpaque(false); // 패널의 불투명성을 비활성화

        // 수직 여백 추가
        Post.add(Box.createVerticalStrut(10));
        //게시물을 클릭할 수 있는 버튼 생성
        JButton PostButton = new JButton();
        PostButton.setLayout(new BorderLayout());
        PostButton.add(recentPostTitleLabel, BorderLayout.CENTER);
        PostButton.setContentAreaFilled(false);
        PostButton.setBorderPainted(false);

        // 프로필 한줄평 추가할 패널 생성
        String profileComment = profileEditor.getProfileComment(); // 프로필 한 줄평 가져오기
        System.out.println("프로필 한 줄평: " + profileComment); // 콘솔에 출력해서 값이 올바른지 확인
        JPanel profileTextPanel = new JPanel();
        profileTextPanel.setBounds(75, 370, 150, 50);
        profileTextPanel.setOpaque(false); // 패널의 불투명성을 비활성화
        layeredPane.add(profileTextPanel, JLayeredPane.MODAL_LAYER);

        JPanel ProfiletextPanel = new JPanel();
        JLabel ProfiletextLabel = new JLabel(profileComment);
        Font f1 = FontManager.loadFont("/Font/KOTRA HOPE.ttf", Font.PLAIN, 15);
        ProfiletextLabel.setFont(f1);

        ProfiletextPanel.add(ProfiletextLabel);
        profileTextPanel.add(ProfiletextPanel);
        ProfiletextPanel.setOpaque(false); // 패널의 불투명성을 비활성화
    }
    private void showCommentsUI() {
        // 새 다이얼로그 생성
        JDialog dialog = new JDialog(this, "방명록", true);
        dialog.setLayout(new BorderLayout());

        // 방명록 목록을 위한 패널
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(commentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 데이터베이스에서 방명록 데이터 로드
        loadCommentsFromDatabase(commentPanel);

        // 댓글 추가를 위한 텍스트 필드와 버튼
        JTextField textField = new JTextField(20);
        JButton addButton = new JButton("추가");
        addButton.addActionListener(e -> {
            String comment = textField.getText();
            if (!comment.isEmpty()) {
                saveCommentToDatabase(comment); // 데이터베이스에 저장
                commentPanel.add(new JLabel(comment)); // UI 업데이트
                commentPanel.revalidate();
                textField.setText(""); // 텍스트 필드 초기화
            }
        });

        // 컴포넌트를 다이얼로그에 추가
        JPanel inputPanel = new JPanel();
        inputPanel.add(textField);
        inputPanel.add(addButton);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(inputPanel, BorderLayout.SOUTH);

        // 다이얼로그 사이즈 설정 및 표시
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this); // 부모 프레임 중앙에 위치
        dialog.setVisible(true);
    }

    private void loadCommentsFromDatabase(JPanel commentPanel) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DatabaseConfig.getConnection(); // 데이터베이스 연결
            stmt = conn.createStatement();
            String sql = "SELECT userId, comment FROM guestBook WHERE friendId = '" + this.userId + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String userId = rs.getString("userId");
                String comment = rs.getString("comment");
                commentPanel.add(new JLabel(comment));
            }
            commentPanel.revalidate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 로딩 실패", "오류", JOptionPane.ERROR_MESSAGE);
        } finally {
            // 리소스 정리
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    private void saveCommentToDatabase(String comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            String sql = "INSERT INTO guestBook (friendId, userId, comment) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, this.userId); // friendId 설정
            pstmt.setString(2, this.userId); // 현재 사용자 ID를 설정해야 함
            pstmt.setString(3, comment);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("방명록에 댓글이 추가되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 저장 실패", "오류", JOptionPane.ERROR_MESSAGE);
        } finally {
            // 리소스 정리
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    private void uploadAndSetNewProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("새 프로필 이미지 선택");
        int result = fileChooser.showOpenDialog(this);

<<<<<<< HEAD
    private JPanel createRecentPostPanel(JPanel recentPostPanel) {
        return recentPostPanel;
    }


=======
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // 파일에서 이미지 데이터를 읽어옵니다.
                byte[] imageData = Files.readAllBytes(selectedFile.toPath());

                // ImageDetails 객체를 생성하고 정보를 설정합니다.
                ImageDetails imageDetails = new ImageDetails();
                imageDetails.setUserId(this.userId); // 사용자 ID 설정
                imageDetails.setImageData(imageData); // 이미지 데이터 설정
                imageDetails.setImageName(selectedFile.getName()); // 이미지 파일 이름 설정
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                imageDetails.setUploadTime(currentTimestamp.toString());

                // ProfileImageUpload 객체를 생성하고 이미지를 데이터베이스에 업로드합니다.
                ProfileImageUpload profileImageUpload = new ProfileImageUpload();
                profileImageUpload.uploadProfileImage(selectedFile, imageDetails);

                // 프로필 패널에 새로운 이미지를 설정합니다.
                Image newProfileImage = new ImageIcon(selectedFile.getAbsolutePath()).getImage();
                profileImageUpload.getProfileImage(userId); // ProfileImageUpload의 setProfileImage 메서드 호출
                profilePanel.updateProfileImage(newProfileImage); // 프로필 이미지 변경
                JOptionPane.showMessageDialog(this, "프로필 이미지가 성공적으로 저장되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
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
        PhotoGalleryWindow galleryWindow = new PhotoGalleryWindow(photoGalleryManager, userId,true);
        galleryWindow.setVisible(true);
    }
    private JPanel PostPanel(JPanel PostPanel) {
        return PostPanel;
    }
    // 사진첩 창을 열기 위한 메서드
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));

<<<<<<< HEAD

        // 예시 버튼 추가
        JButton boardButton1 = new JButton("홈화면");
        JButton boardButton2 = new JButton("프로필");


        JButton boardButton3 = new JButton("게시판");
        JButton boardButton4 = new JButton("방명록");
        JButton boardButton5 = new JButton("사진첩");


        // 각 버튼의 가로 정렬을 가운데로 설정
        boardButton1.setHorizontalAlignment(SwingConstants.CENTER);
        boardButton2.setHorizontalAlignment(SwingConstants.CENTER);
        boardButton3.setHorizontalAlignment(SwingConstants.CENTER);
        boardButton4.setHorizontalAlignment(SwingConstants.CENTER);
        boardButton5.setHorizontalAlignment(SwingConstants.CENTER);





        // 버튼들을 오른쪽 정렬 및 투명 설정
        for (JButton button : Arrays.asList(boardButton1, boardButton2, boardButton3, boardButton4, boardButton5)) {
            button.setAlignmentX(Component.RIGHT_ALIGNMENT);
            button.setOpaque(false);
        }



=======
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
            if (profileEditor != null) {
                profileEditor.handleProfileButtonClick();
            }
        });
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2

        boardButton3.addActionListener(e -> new BoardList());
        // 게시판 버튼 클릭 시 수행할 동작
<<<<<<< HEAD
        boardButton1.addActionListener(e -> new BoardList()); // 게시판 페이지 열기판
        gifPanel.requestFocusInWindow();
        menuBar.add(boardButton1);

        //boardButton1.addActionListener(e -> new BoardList()); // 게시판 페이지 열기
        menuBar.add(boardButton1);
        boardButton2.addActionListener(e -> new BoardList()); // 게시판 페이지 열기
        menuBar.add(boardButton2);
        //boardButton3.addActionListener(e -> new BoardList()); // 게시판 페이지 열기
        menuBar.add(boardButton3);
        //boardButton4.addActionListener(e -> new BoardList()); // 게시판 페이지 열기
        menuBar.add(boardButton4);
        //boardButton5.addActionListener(e -> new BoardList()); // 게시판 페이지 열기
        menuBar.add(boardButton5);

        //일촌신청
        JButton friendsButton=new JButton("일촌신청");
        friendsButton.setOpaque(false);

        friendsButton.addActionListener(e -> {
            FriendSearchDialog searchDialog = new FriendSearchDialog(frame, UserSession.getInstance().getUserId());
            searchDialog.setVisible(true);
=======
        boardButton4.addActionListener(e -> {
            showCommentsUI();
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
        });
        // 방명록 버튼 클릭 시 수행할 동작

<<<<<<< HEAD
        JButton friendListButton = new JButton("일촌 목록");
        friendListButton.setOpaque(false);

        friendListButton.addActionListener(e -> {
            try {
                showFriendListDialog();
                gifPanel.requestFocusInWindow();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        menuBar.add(friendListButton);




        // 추가 버튼들을 여기에 추가
        // 예: JButton otherButton = new JButton("다른 메뉴");
        // otherButton.setAlignmentX(Component.RIGHT_ALIGNMENT); // 추가 버튼을 오른쪽 정렬
        // menuBar.add(otherButton);

        // 각 버튼의 크기 조정
        Dimension buttonSize = new Dimension(200, 50); // 가로 200, 세로 50으로 크기를 설정
        for (JButton button : Arrays.asList(boardButton1, boardButton2, boardButton3, boardButton4, boardButton5)) {
            button.setPreferredSize(buttonSize);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setAlignmentX(Component.RIGHT_ALIGNMENT);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);

            // 텍스트 색상을 하얀색으로 설정
            button.setForeground(Color.WHITE);
        }


        // 패널의 배경을 투명하게 설정
        menuBar.setOpaque(false);

        // 버튼 사이에 여백 추가
        int HorizontalStrutHeight = 11; // 원하는 여백의 높이
        menuBar.add(boardButton1);
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(boardButton2);
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(boardButton3);
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(boardButton4);
        menuBar.add(Box.createHorizontalStrut(HorizontalStrutHeight));
        menuBar.add(boardButton5);

        return menuBar;
    }
    //친구리스트보여주는 메소드
    private void showFriendListDialog() throws Exception {
        String userId=UserSession.getInstance().getUserId();
        JDialog friendListDialog = new JDialog(frame, "친구 목록", true);
        friendListDialog.setLayout(new BorderLayout());

        // 친구 목록을 가져오는 코드 (예시)
        List<String> friends = friendManager.getAcceptedFriendRequests(userId); // 친구 목록을 가져오는 메소드 구현 필요

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String friend : friends) {
            model.addElement(friend);
        }

        JList<String> friendList = new JList<>(model);
        friendListDialog.add(new JScrollPane(friendList), BorderLayout.CENTER);

        // 채팅 및 쪽지 버튼을 추가하기 위한 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // 1대1 채팅 버튼 추가
        JButton chatButton = new JButton("1대1 채팅");
        chatButton.addActionListener(e -> {
            if (!friendList.isSelectionEmpty()) {
                String selectedFriend = friendList.getSelectedValue();
                // 1대1 채팅 로직 구현
                try {
                    startChatWithFriend(selectedFriend);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(chatButton);

        // 쪽지 보내기 버튼 추가
        JButton messageButton = new JButton("쪽지 보내기");
        messageButton.addActionListener(e -> {
            if (!friendList.isSelectionEmpty()) {
                String selectedFriend = friendList.getSelectedValue();
                // 쪽지 보내기 로직 구현
                sendNoteToFriend(selectedFriend);
            }
        });
        buttonPanel.add(messageButton);

        friendListDialog.add(buttonPanel, BorderLayout.SOUTH);

        friendListDialog.setSize(300, 400);
        friendListDialog.setLocationRelativeTo(frame);
        friendListDialog.setVisible(true);
    }
    //1대 1채팅 구현
    private void startChatWithFriend(String friend) throws SQLException {
        String currentUser = UserSession.getInstance().getUserId(); // 현재 사용자의 ID
        ChatWindow chatWindow = new ChatWindow(friend, currentUser);
        chatWindow.setVisible(true);
    }


    private void sendNote(String friend, String note) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, UserSession.getInstance().getUserId()); // 현재 사용자 ID
                statement.setString(2, friend); // 받는 사람의 ID
                statement.setString(3, note); // 쪽지 내용
                statement.executeUpdate();

                JOptionPane.showMessageDialog(frame, "쪽지가 성공적으로 보내졌습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "쪽지 보내기에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }




=======
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
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false); // 메인 컨텐츠 투명하게 설정
        return mainContent;
    }
    public void setUserId(String userId) {
        this.userId = userId;
        photoGalleryManager = new PhotoGalleryManager(this, this.userId);
        this.profileEditor = new ProfileEditor(this.userId);
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
<<<<<<< HEAD


    // 기타 필요한 메소드...

}
=======
}
>>>>>>> 80c81737c145a0e34d71f1efd88e3d647eef0fe2
