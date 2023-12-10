package org.example;


import org.Friend.FriendManager;
import org.Friend.FriendRequestDialog;
import org.Friend.FriendSearchDialog;
import org.Utility.*;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;


public class MiniHomepage {
    private JButton notificationButton;
    private FriendManager friendManager;
    private JFrame frame;


    private Timer playTimeUpdateTimer; // 재생 시간 업데이트 타이머
    String imagePath = "/image/main.jpg";
    URL imageUrl = getClass().getResource(imagePath);
    private MusicPlayer musicPlayer; // MusicPlayer 객체 추가
    // 추가: 음악 파일 리스트
    private List<String> musicFiles = Arrays.asList(
            "/music/music1.wav",
            "/music/music2.wav",
            "/music/music3.wav",
            "/music/music4.wav"
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
        gifIcon = new ImageIcon(getClass().getResource("/image/MainRoom.gif")); // GIF 파일 경로 수정 필요

        // 캐릭터 이미지 로드
        characterImages = new ImageIcon[16]; // 총 16개의 이미지
        for (int i = 0; i < 16; i++) {
            characterImages[i] = new ImageIcon(getClass().getResource("/image/character_" + (i + 1) + ".png"));
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
        new MiniHomepage().showLogin();
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
        backgroundPanel.setBounds(0, 0, 888, 598);
        backgroundPanel.setOpaque(false); // 배경 이미지 패널 투명도 설정

        // JLayeredPane 생성 및 설정
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(888, 598));

        // 배경 패널 추가
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        //프로필패널메소드
        ImageIcon profileImageIcon = new ImageIcon(getClass().getResource("/image/DefaultImage.jpg"));
        Image profileImage = profileImageIcon.getImage();
        ProfilePanel profilePanel = new ProfilePanel(profileImage);
        profilePanel.setBounds(75, 135, profileImageIcon.getIconWidth(), profileImageIcon.getIconHeight());
        layeredPane.add(profilePanel, Integer.valueOf(500));

        JButton newButton = new JButton("사진변경");
        newButton.setBounds(60, 200 + profileImageIcon.getIconHeight(), 85, 20); // 위치 설정 (가로: 100, 세로: 30)
        layeredPane.add(newButton, Integer.valueOf(501)); // 새로운 버튼을 적절한 레이어에 추가
        // '사진변경'에 파일 선택 기능 추가
        newButton.addActionListener(e -> {
            profilePanel.uploadAndResizeImage();
        });


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

        // 알림 버튼 추가
        URL notificationIconUrl = getClass().getResource("/image/bell.jpg");
        ImageIcon notificationIcon = new ImageIcon(notificationIconUrl);
        JButton notificationButton = new JButton(notificationIcon);
        notificationButton.setBounds(850, 5, 30, 30);
        layeredPane.add(notificationButton, Integer.valueOf(JLayeredPane.POPUP_LAYER));
        notificationButton.addActionListener(e -> {
            showFriendRequestsDialog();
            updateFriendRequestCount(); // 친구 요청 다이얼로그를 표시한 후 요청 수를 업데이트
            gifPanel.requestFocusInWindow();
        });
        layeredPane.add(notificationButton, Integer.valueOf(JLayeredPane.POPUP_LAYER));

        //쪽지 버튼 추가
        URL messageUrl=getClass().getResource("/image/message.jpeg");
        ImageIcon messageIcon=new ImageIcon(messageUrl);
        JButton messageButton=new JButton(messageIcon);
        messageButton.setBounds(800,5,30,30);
        layeredPane.add(messageButton,Integer.valueOf(JLayeredPane.POPUP_LAYER));
        messageButton.addActionListener(e->{
            try {
                showMessageListDialog();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            gifPanel.requestFocusInWindow();
        });



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

        // 쪽지 목록을 가져오는 로직
        List<Message> messages = getMessagesForUser(UserSession.getInstance().getUserId());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (Message msg : messages) {
            model.addElement(msg.getSender() + ": " + msg.getContent()); // 예시 형식
        }

        JList<String> messageList = new JList<>(model);
        messageListDialog.add(new JScrollPane(messageList), BorderLayout.CENTER);
        messageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !messageList.isSelectionEmpty()) {
                // 선택한 메시지의 전체 내용을 보여주는 다이얼로그 생성 및 표시
                Message selectedMsg = messages.get(messageList.getSelectedIndex());
                showFullMessageDialog(selectedMsg);
            }
        });


        JButton replyButton = new JButton("답장 보내기");
        replyButton.addActionListener(e -> {
            if (!messageList.isSelectionEmpty()) {
                // 선택한 메시지의 발신자를 가져와 답장을 보냄
                Message selectedMsg = messages.get(messageList.getSelectedIndex());
                sendNoteToFriend(selectedMsg.getSender());
            }
        });

        JButton newMessageButton = new JButton("새 쪽지 보내기");
        newMessageButton.addActionListener(e -> sendNewMessage());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(replyButton);
        buttonPanel.add(newMessageButton);
        messageListDialog.add(buttonPanel, BorderLayout.SOUTH);

        messageListDialog.setSize(400, 300);
        messageListDialog.setLocationRelativeTo(frame);
        messageListDialog.setVisible(true);
    }
    private void sendNoteToFriend(String friend) {
        JDialog noteDialog = new JDialog(frame, "쪽지 보내기: " + friend, true);
        noteDialog.setLayout(new BorderLayout());

        JTextArea noteTextArea = new JTextArea();
        noteDialog.add(new JScrollPane(noteTextArea), BorderLayout.CENTER);

        JButton sendButton = new JButton("보내기");
        sendButton.addActionListener(e -> {
            String note = noteTextArea.getText();
            // 쪽지 보내는 로직 구현
            sendNote(friend, note);
            noteDialog.dispose();
        });
        noteDialog.add(sendButton, BorderLayout.SOUTH);

        noteDialog.setSize(300, 200);
        noteDialog.setLocationRelativeTo(frame);
        noteDialog.setVisible(true);
    }

    private void showFullMessageDialog(Message message) {
        JDialog fullMessageDialog = new JDialog(frame, "쪽지 내용", true);
        fullMessageDialog.setLayout(new BorderLayout());

        JTextArea messageTextArea = new JTextArea();
        messageTextArea.setText("From: " + message.getSender() + "\n\n" + message.getContent());
        messageTextArea.setEditable(false);
        fullMessageDialog.add(new JScrollPane(messageTextArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> fullMessageDialog.dispose());
        fullMessageDialog.add(closeButton, BorderLayout.SOUTH);

        fullMessageDialog.setSize(400, 300);
        fullMessageDialog.setLocationRelativeTo(frame);
        fullMessageDialog.setVisible(true);
    }


    private List<Message> getMessagesForUser(String userId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT sender_id, message FROM messages WHERE receiver_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String sender = resultSet.getString("sender_id");
                        String content = resultSet.getString("message");
                        messages.add(new Message(sender, userId, content));
                    }
                }
            }
        }
        return messages;
    }

    private void sendNewMessage() {
        // 새 쪽지를 보내는 다이얼로그를 표시
        String recipient = JOptionPane.showInputDialog(frame, "쪽지를 받을 사용자 ID:");
        if (recipient != null && !recipient.isEmpty()) {
            sendNoteToFriend(recipient);
        }
    }


    private void updateFriendRequestCount() {
        try {
            // 현재 사용자 ID를 가져옵니다.
            String currentUserId = UserSession.getInstance().getUserId();

            // FriendManager를 사용하여 현재 사용자에 대한 대기중인 친구 요청 목록을 가져옵니다.
            List<String> pendingRequests = friendManager.getPendingFriendRequests(currentUserId);

            // 대기중인 요청의 수를 계산합니다.
            int requestCount = pendingRequests.size();

            // 버튼에 알림 수를 텍스트로 표시합니다.
            if (requestCount > 0&&this.notificationButton!=null) {
                notificationButton.setText(String.valueOf(requestCount));
            } else {
                notificationButton.setText(""); // 요청이 없으면 텍스트를 비웁니다.
            }

            // 버튼을 다시 그려 변경사항을 적용합니다.
            notificationButton.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 처리
        }
    }

    private void showFriendRequestsDialog() {
        try {
            if (friendManager == null) {
                throw new IllegalStateException("FriendManager is not initialized");
            }
            List<String> friendRequests = friendManager.getPendingFriendRequests(UserSession.getInstance().getUserId());
            FriendRequestDialog requestsDialog = new FriendRequestDialog(frame, UserSession.getInstance().getUserId(), friendRequests);
            requestsDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 처리 (예: 오류 메시지 표시)
        }
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


        boardButton3.addActionListener(e -> new BoardList()); // 게시판 페이지 열기판
        gifPanel.requestFocusInWindow();
        menuBar.add(boardButton3);

        //일촌신청
        JButton friendsButton=new JButton("일촌신청");
        //boardButton.setOpaque(false);

        friendsButton.addActionListener(e -> {
            FriendSearchDialog searchDialog = new FriendSearchDialog(frame, UserSession.getInstance().getUserId());
            searchDialog.setVisible(true);
        });
        menuBar.add(friendsButton);

        JButton friendListButton = new JButton("일촌 목록");
        //boardButton.setOpaque(false);

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
}
