package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;


public class MiniHomepage {



    private Timer playTimeUpdateTimer; // 재생 시간 업데이트 타이머
    String imagePath = "/main.jpg";
    URL imageUrl = getClass().getResource(imagePath);
    private MusicPlayer musicPlayer; // MusicPlayer 객체 추가
    // 추가: 음악 파일 리스트
    private List<String> musicFiles = Arrays.asList(
            "/music1.wav",
            "/music2.wav",
            "/music3.wav",
            "/music4.wav"
    );

    private String[][] songInfo = {
            {"Y", "Free Style"},
            {"응급실", "izi"},
            {"밤하늘의 별을..","양정승"},
            {"I love U Oh Thank U","MC몽"}

            // ... 다른 노래 정보 ...
    };
    private JLabel songInfoLabel;


    private LoginPage loginPage;
    private SignUppage signUpPage;
    private JLabel userIdLabel; // 사용자 ID를 표시할 레이블
    private ImageIcon gifIcon; // 방 움짤

    private JLabel avatarLabel;//아바타
    private JPanel gifPanel;
    //캐릭터관련 변수들
    private ImageIcon[] characterImages; // 캐릭터 이미지 배열
    private int currentCharacterFrame = 0; // 현재 캐릭터 프레임
    private String characterDirection = "front"; // 캐릭터 방향
    private Point characterPosition; // 캐릭터 위치


    private JLabel playTimeLabel; // 재생 시간을 표시할 레이블
    private JSlider musicSlider; // 뮤직 바


    public MiniHomepage() {
        signUpPage = new SignUppage();
        //loginPage = new LoginPage(signUpPage, this);
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
            }
        };
        gifPanel.setPreferredSize(new Dimension(gifIcon.getIconWidth(), gifIcon.getIconHeight()));
        gifPanel.add(avatarLabel);
        avatarLabel.setLocation(50, 50); // 초기 아바타 위치 설정
    }


    public static void main(String[] args) {
        new MiniHomepage().showMainPage();
    }

    private void showLogin() {
        loginPage.show();
    }

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
    // 수평 패널을 생성하는 메서드
    public void showMainPage() {
        JFrame frame = new JFrame("싸이월드");
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
        backgroundPanel.setBounds(0, 0, 888, 598);
        backgroundPanel.setOpaque(false); // 배경 이미지 패널 투명도 설정

        // JLayeredPane 생성 및 설정
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(888, 598));

        // 배경 패널 추가
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // gifPanel 및 아바타 초기화 및 추가
        initializeGifAndAvatar();
        gifPanel.setBounds(350, 250, gifIcon.getIconWidth(), gifIcon.getIconHeight());
        layeredPane.add(gifPanel, Integer.valueOf(300)); // gifPanel에 높은 z-인덱스 부여

        // 상단 바 패널을 생성합니다.
        JPanel topBar = createTopBar();
        topBar.setOpaque(false); // 상단 바 패널 투명도 설정
        // 상단 바를 레이어드 패널에 추가
        layeredPane.add(topBar, Integer.valueOf(JLayeredPane.PALETTE_LAYER));
        topBar.setBounds(0, 0, 888, 30); // 위치와 크기를 지정합니다.

        // 메뉴 바 패널을 생성합니다.
        JPanel menuBar = createMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBounds(700, 75, 200, 190);
        layeredPane.add(menuBar, JLayeredPane.MODAL_LAYER); // 메뉴 바를 적절한 레이어에 추가

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

        // 메인 컨텐츠를 gifPanel 내부에 추가
        JPanel mainContent = createMainContent();
        gifPanel.add(mainContent);
        mainContent.setBounds(0, 0, gifPanel.getWidth(), gifPanel.getHeight());
        mainContent.setOpaque(false); // 메인 컨텐츠를 투명하게 설정

        // 프레임에 레이어드 패인 추가 및 표시
        frame.setLayeredPane(layeredPane); // 프레임에 레이어드 판을 설정
        frame.setVisible(true);

        gifPanel.requestFocusInWindow();

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

    private JPanel createRecentPostPanel(JPanel recentPostPanel) {
        return recentPostPanel;
    }


    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS)); // 세로 방향 레이아웃 설정


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




        // 게시판 버튼 클릭 시 수행할 동작
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







    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());

        // 상단 타이틀
        //JLabel title = new JLabel("", SwingConstants.CENTER);
        //title.setFont(new Font("Stylish", Font.PLAIN, 25));
        //mainContent.add(title, BorderLayout.NORTH);
        mainContent.setOpaque(false); // 메인 컨텐츠 투명하게 설정

        // 키보드 이벤트 처리
        gifPanel.setFocusable(true);
        gifPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                moveAvatar(e);
                gifPanel.repaint();
            }
        });


        return mainContent;
    }
    private void moveAvatar(KeyEvent e) {
        int dx = 0, dy = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                dx = -7; characterDirection = "left"; break;
            case KeyEvent.VK_RIGHT:
                dx = 7; characterDirection = "right"; break;
            case KeyEvent.VK_UP:
                dy = -7; characterDirection = "back"; break;
            case KeyEvent.VK_DOWN:
                dy = 7; characterDirection = "front"; break;
        }
        // 캐릭터의 잠재적인 새 위치 계산
        int newX = characterPosition.x + dx;
        int newY = characterPosition.y + dy;

        characterPosition.x = Math.max(0, Math.min(characterPosition.x + dx, gifPanel.getWidth() - avatarLabel.getWidth()));
        characterPosition.y = Math.max(0, Math.min(characterPosition.y + dy, gifPanel.getHeight() - avatarLabel.getHeight()));

        // 캐릭터 애니메이션 프레임 업데이트
        currentCharacterFrame = (currentCharacterFrame + 1) % 4; // 각 방향별 4개의 프레임
        gifPanel.repaint();
    }


    // ... 기타 필요한 메소드...

    public void setUserId(String username) {
        userIdLabel.setText("Welcome, " + username);
    }
    //현재방향에 따라 이미지 프레임 인덱스 계산
    private int getCharacterFrameIndex() {
        int baseIndex;
        switch (characterDirection) {
            case "front": baseIndex = 0; break;
            case "left": baseIndex = 4; break;
            case "right": baseIndex = 8; break;
            case "back": baseIndex = 12; break;
            default: baseIndex = 0; break;
        }
        return baseIndex + currentCharacterFrame % 4; // 각 방향별로 4개의 이미지
    }


    // 기타 필요한 메소드...

}
