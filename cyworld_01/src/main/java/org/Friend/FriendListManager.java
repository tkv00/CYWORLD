package org.Friend;

import org.Utility.DatabaseConfig;
import org.Utility.PhotoGalleryWindow;
import org.Utility.UserSession;
import org.example.Message;
import org.example.MiniHomepage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FriendListManager {
    private JFrame parentFrame;

    static final Logger LOGGER = Logger.getLogger(FriendListManager.class.getName());

    private List<Message> sentMessages; // 보낸// 쪽지 목록
    private List<Message> receivedMessages; // 받은 쪽지 목록
    private JDialog friendListDialog; // 친구 목록 대화상자
    private FriendManager friendManager;
    private MiniHomepage parentMiniHomepage; // MiniHomepage 인스턴스
    private JPanel friendsPanel; // 친구 목록을 표시할 패널
    private JButton notificationButton; // 알림 버튼


    public FriendListManager(MiniHomepage parentMiniHomepage, FriendManager friendManager, JPanel friendsPanel, JButton notificationButton) {
        this.parentMiniHomepage = parentMiniHomepage;
        this.friendManager = friendManager;
        this.friendsPanel =  friendsPanel;
        this.notificationButton = notificationButton;
    }
    // 쪽지 목록을 보여주는 다이얼로그
    public void showMessagesDialog() {
        LOGGER.log(Level.INFO, "Showing messages dialog.");
        try {
            String userId = UserSession.getInstance().getUserId();
            sentMessages = friendManager.getSentMessages(userId);
            receivedMessages = friendManager.getReceivedMessages(userId);

            // 상세 패널을 포함하는 대화상자 생성
            JDialog messagesDialog = new JDialog(parentFrame, "쪽지", true);
            messagesDialog.setLayout(new BorderLayout());

            // 보낸 쪽지 및 받은 쪽지 패널 생성
            JPanel sentPanel = createMessagesListPanel("보낸 쪽지", true);
            JPanel receivedPanel = createMessagesListPanel("받은 쪽지", false);

            // JSplitPane을 사용하여 두 패널을 수직으로 분할
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sentPanel, receivedPanel);
            splitPane.setDividerLocation(200); // 분할 위치 설정

            messagesDialog.add(splitPane, BorderLayout.CENTER);
            messagesDialog.setSize(600, 400);
            messagesDialog.setLocationRelativeTo(null); // 창을 화면 중앙에 배치
            messagesDialog.setVisible(true);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to show messages dialog.", e);
            JOptionPane.showMessageDialog(parentFrame, "쪽지를 불러오는데 실패했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendNoteToFriend(String friend) {
        // Title input as a smaller field
        JTextField titleField = new JTextField(); // The width will be set by the layout manager
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleField.getPreferredSize().height));

        // Content input as a larger text area
        JTextArea contentArea = new JTextArea(10, 20); // Height is set to 10, width will match the title field
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        // Panel for title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(new JLabel("제목:"));
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
        titlePanel.add(titleField);

        // Panel for content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(new JLabel("내용:"));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        contentPanel.add(contentScrollPane);

        // Ensure the text area is the same width as the title field
        contentArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, contentArea.getPreferredSize().height));

        // Main input panel that holds both title and content panels
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(titlePanel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer between title and content panels
        inputPanel.add(contentPanel);

        // Show the dialog and process the result
        int result = JOptionPane.showConfirmDialog(friendListDialog, inputPanel, friend + "에게 보낼 쪽지",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Process the result
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (!title.isEmpty() && !content.isEmpty()) {
                // Implement message sending logic here
                JOptionPane.showMessageDialog(friendListDialog, "쪽지가 성공적으로 보내졌습니다.");
            } else {
                JOptionPane.showMessageDialog(friendListDialog, "제목과 내용을 모두 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void openFriendSearchDialog() {
        FriendSearchDialog friendSearchDialog = new FriendSearchDialog(parentMiniHomepage, UserSession.getInstance().getUserId());
        friendSearchDialog.setFriendAddListener(new FriendAddListener() {
            public void onFriendAdded(String friendName) {
                addFriendButton(friendName);
            }
        });
        friendSearchDialog.setVisible(true);
    }

    private void addFriendButton(String friendName) {
        JButton friendButton = new JButton("일촌: " + friendName);
        friendButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentMiniHomepage, "일촌 버튼 클릭: " + friendName);
        });
        friendsPanel.add(friendButton);
        friendsPanel.revalidate();
        friendsPanel.repaint();
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

    // 선택한 쪽지의 세부 정보를 보여주고 답장할 수 있는 다이얼로그
    private void showDetailedMessageDialog(Message message, boolean isSent) {
        // 쪽지의 세부 내용을 보여줄 JTextArea를 생성합니다.
        JTextArea messageDetailsArea = new JTextArea();
        // 쪽지의 세부 정보를 포함하여 텍스트 영역에 설정합니다. 보낸 시간 정보도 추가합니다.
        messageDetailsArea.setText(
                "From: " + message.getSender() +
                        "\nTo: " + message.getReceiver() +
                        "\n제목: " + message.getTitle() +
                        "\n시간: " + message.getSentTime().replace("-", ":") +
                        "\n\n" + message.getContent()
        );
        messageDetailsArea.setEditable(false);


        // 세부 정보를 표시할 다이얼로그를 생성하고 구성합니다.
        JDialog messageDetailsDialog = new JDialog(parentFrame, "쪽지 세부 정보", true);
        messageDetailsDialog.setLayout(new BorderLayout());
        messageDetailsDialog.add(new JScrollPane(messageDetailsArea), BorderLayout.CENTER);

        // 답장 버튼을 생성하고, 클릭 시 답장 다이얼로그를 표시하도록 설정합니다.
        JButton replyButton = new JButton("답장");
        if (isSent) {
            replyButton.setEnabled(false);
        } else {
            replyButton.addActionListener(e -> showReplyDialog(message.getSender(), messageDetailsDialog));
        }
        messageDetailsDialog.add(replyButton, BorderLayout.SOUTH);
        messageDetailsDialog.setSize(400, 300);
        messageDetailsDialog.setLocationRelativeTo(parentFrame);
        messageDetailsDialog.setAlwaysOnTop(true);
        messageDetailsDialog.setVisible(true);
    }

    private void showReplyDialog(String recipient, JDialog messageDetailsDialog) {
        // 답장 창을 생성합니다.
        JDialog replyDialog = new JDialog(messageDetailsDialog, "답장 보내기", true);
        replyDialog.setLayout(new BorderLayout());


        // 제목 입력을 위한 필드를 생성합니다.
        JTextField titleField = new JTextField("제목: ", 20);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (titleField.getText().equals("제목: ")) {
                    titleField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (titleField.getText().isEmpty()) {
                    titleField.setText("제목: ");
                }
            }
        });

        // 내용 입력을 위한 텍스트 에리어를 생성합니다.
        JTextArea contentArea = new JTextArea("내용: ", 10, 30);
        contentArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (contentArea.getText().equals("내용: ")) {
                    contentArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (contentArea.getText().isEmpty()) {
                    contentArea.setText("내용: ");
                }
            }
        });
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);



        // 입력 필드를 포함할 패널을 생성합니다.
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(titleField, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        // 보내기 버튼을 추가합니다.
        JButton sendButton = new JButton("보내기");
        sendButton.addActionListener(e -> {
            String replyContent = contentArea.getText();
            String replyTitle = "Re: " + titleField.getText().replace("제목: ", ""); // Remove placeholder text if present.
            sendReplyToFriend(recipient, replyTitle, replyContent, replyDialog);
            replyDialog.dispose(); // 답장을 보낸 후 창을 닫습니다.
        });

        // 다이얼로그에 패널과 버튼을 추가합니다.
        replyDialog.add(inputPanel, BorderLayout.CENTER);
        replyDialog.add(sendButton, BorderLayout.SOUTH);

        // 다이얼로그를 화면에 표시합니다.
        replyDialog.pack();
        replyDialog.setLocationRelativeTo(parentFrame);
        replyDialog.setAlwaysOnTop(true);
        replyDialog.setVisible(true);
    }
    public void showFriendListDialog() {
        try {
            String userId = UserSession.getInstance().getUserId();
            friendListDialog = new JDialog(parentFrame, "일촌 목록", true);
            friendListDialog.setLayout(new BorderLayout());

            List<String> friends = friendManager.getAcceptedFriendRequests(userId);
            DefaultListModel<String> model = new DefaultListModel<>();

            for (String friend : friends) {
                model.addElement(friend);
            }

            JList<String> friendList = new JList<>(model);
            friendListDialog.add(new JScrollPane(friendList), BorderLayout.CENTER);

            // 하단 버튼 패널
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton visitButton = new JButton("싸이월드 방문");
            JButton messageButton = new JButton("쪽지 보내기");

            // 버튼 초기 상태는 비활성화
            visitButton.setEnabled(false);
            messageButton.setEnabled(false);

            buttonPanel.add(visitButton);
            buttonPanel.add(messageButton);
            friendListDialog.add(buttonPanel, BorderLayout.SOUTH);

            // 리스트 선택 이벤트 리스너
            friendList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String selectedFriend = friendList.getSelectedValue();
                    if (selectedFriend != null && !selectedFriend.isEmpty()) {
                        // 버튼 활성화 및 액션 리스너 설정
                        visitButton.setEnabled(true);
                        messageButton.setEnabled(true);

                        visitButton.addActionListener(ev -> visitFriendCyworld(selectedFriend));
                        messageButton.addActionListener(ev -> sendNoteToFriend(selectedFriend));
                    } else {
                        // 선택이 해제된 경우 버튼 비활성화
                        visitButton.setEnabled(false);
                        messageButton.setEnabled(false);
                    }
                }
            });

            friendListDialog.setSize(250, 300);
            friendListDialog.setLocationRelativeTo(parentFrame);
            friendListDialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "친구 목록을 불러오는데 실패했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    //일촌의 싸이월드 방문 로직
    private void visitFriendCyworld(String friend) {
        // 새로운 MiniHomepage 인스턴스 생성
        MiniHomepage friendCyworld = new MiniHomepage();
        // 일촌의 사용자 ID 설정
        friendCyworld.setUserId(friend);

        // 일촌의 싸이월드에 대한 설정 (버튼 비활성화 등)
        disableFriendCyworldFeatures(friendCyworld);

        // 사진첩 버튼에 대한 액션 리스너 추가
        friendCyworld.getPhotoGalleryButton().addActionListener(e -> openFriendPhotoGallery(friendCyworld, friend));
        // 일촌의 싸이월드 창을 닫을 때 나의 싸이월드 창이 닫히지 않도록 다른 닫기 동작을 설정합니다.
        friendCyworld.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 필요한 경우 변경
        // 새 창에서 일촌의 싸이월드 표시
        friendCyworld.showMainPage();
        friendCyworld.setVisible(true);
    }
    // 일촌의 싸이월드에 대한 설정을 비활성화하는 별도의 메서드
    private void disableFriendCyworldFeatures(MiniHomepage friendCyworld) {
        friendCyworld.getProfileButton().setEnabled(false);
        friendCyworld.getBoardButton().setEnabled(false);
        friendCyworld.getPhotoGalleryButton().setEnabled(true);
        //사진첩중 사진추가 버튼 잠그기

        friendCyworld.getChangeImageButton().setEnabled(false);
        friendCyworld.getMessageButton().setEnabled(false);
        friendCyworld.getNotificationButton().setEnabled(false);
        friendCyworld.getFriend().setEnabled(false);
        friendCyworld.getFriendList().setEnabled(false);
        friendCyworld.getNotificationButton().setEnabled(false);
    }
    private void openFriendPhotoGallery(MiniHomepage friendCyworld, String friendId) {
        PhotoGalleryWindow existingGallery = friendCyworld.getPhotoGalleryWindow();
        // 창이 이미 열려있는지 확인합니다.
        if (existingGallery != null) {
            if (existingGallery.isVisible()) {
                existingGallery.toFront(); // 기존 창을 앞으로 가져옴
                existingGallery.repaint(); // 필요한 경우 창을 새로 그림
            } else {
                // 창이 존재하지만 보이지 않는 경우, 창을 다시 보이게 합니다.
                existingGallery.setVisible(true);
            }
        } else {
            // 새 창을 만듭니다.
            try {
                PhotoGalleryWindow galleryWindow = new PhotoGalleryWindow(friendCyworld.getPhotoGalleryManager(), friendId, false);
                galleryWindow.disableAddPhotoButton(); // '사진 추가' 버튼 비활성화
                galleryWindow.setVisible(true);
                friendCyworld.setPhotoGalleryWindow(galleryWindow); // 새 창 참조를 저장합니다.
            } catch (Exception e) {
                // 오류 발생 시 사용자에게 알립니다.
                JOptionPane.showMessageDialog(friendCyworld, "사진첩을 열 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void sendReplyToFriend(String recipient, String title, String content,JDialog parentDialog) {
        // 데이터베이스에 답장 메시지를 삽입하는 로직 또는 API 호출을 여기에 구현합니다.
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO messages (sender_id, receiver_id, title, message, send_time) VALUES (?, ?, ?, ?, NOW())";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, UserSession.getInstance().getUserId());
                statement.setString(2, recipient);
                statement.setString(3, title);
                statement.setString(4, content);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(parentDialog, "답장이 성공적으로 보내졌습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentDialog, "답장 보내기에 실패했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 쪽지 리스트 패널을 생성하는 메서드
    private JPanel createMessagesListPanel(String title, boolean isSent) {
        LOGGER.log(Level.INFO, "Creating messages list panel: " + title);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> messageList = new JList<>(model);
        List<Message> messages = isSent ? sentMessages : receivedMessages;
        for (Message msg : messages) {
            model.addElement(msg.getTitle() + " (" + (isSent ? "To: " + msg.getReceiver() : "From: " + msg.getSender()) + ")");
        }
        messageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !messageList.isSelectionEmpty()) {
                Message selectedMessage = messages.get(messageList.getSelectedIndex());
                showDetailedMessageDialog(selectedMessage, isSent);
            }
        });
        panel.add(new JScrollPane(messageList), BorderLayout.CENTER);
        return panel;
    }
}

class PanelListRenderer implements ListCellRenderer<JPanel> {
    @Override
    public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value, int index, boolean isSelected, boolean cellHasFocus) {
        return value;
    }
}
