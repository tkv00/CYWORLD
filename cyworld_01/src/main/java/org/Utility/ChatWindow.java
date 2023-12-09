package org.Utility;
import org.example.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatWindow extends JFrame {
    private JPanel messageContainer;
    private JTextField messageField;
    private String friend;
    private String currentUser;
    private JButton sendButton;
    private JScrollPane scrollPane;

    public ChatWindow(String friend, String currentUser) {
        this.friend = friend;
        this.currentUser = currentUser;

        setTitle("Chat with " + friend);
        setSize(400, 500);
        setLayout(new BorderLayout());

        // messageContainer 초기화
        messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(messageContainer);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // 맨 아래에 컴포넌트를 추가하기 위해 필요한 경우에만 스크롤을 조절합니다.
        scrollPane.getViewport().addChangeListener(e -> {
            JViewport viewport = (JViewport) e.getSource();
            int viewHeight = viewport.getHeight();
            int extentHeight = viewport.getExtentSize().height;
            int viewPositionY = viewport.getViewPosition().y;
            if (viewPositionY + extentHeight >= viewHeight - extentHeight) { // 맨 아래에 있다고 판단
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });

        // 입력 필드 및 전송 버튼 초기화
        messageField = new JTextField();
        sendButton = new JButton("전송");
        sendButton.addActionListener(e -> sendMessage());
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // 채팅 창이 열릴 때 이전 메시지 로드
        loadMessages();
    }

    // 메시지 전송 메소드
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            String timestamp =new SimpleDateFormat("HH:mm:ss").format(new Date());
            saveMessageToDatabase(currentUser, friend, message);
            addMessage(message, true, timestamp);
            messageField.setText("");
        }
    }


    // 메시지를 데이터베이스에 저장하는 메소드
    private void saveMessageToDatabase(String sender, String receiver, String message) {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO chat_messages (sender_id, receiver_id, message, timestamp, is_read) VALUES (?, ?, ?, NOW(), FALSE)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, sender);
                statement.setString(2, receiver);
                statement.setString(3, message);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 이전 메시지 로드 메소드
    private void loadMessages() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT sender_id, message FROM chat_messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, currentUser);
                statement.setString(2, friend);
                statement.setString(3, friend);
                statement.setString(4, currentUser);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String sender = resultSet.getString("sender_id");
                        String message = resultSet.getString("message");
                        String timestamp = resultSet.getString("timestamp");
                        boolean isCurrentUser = sender.equals(currentUser);
                        addMessage(message, isCurrentUser,timestamp);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메시지 컨테이너에 메시지 추가

    public void addMessage(String message, boolean isCurrentUser, String timestamp) {
        BubblePanel bubble = new BubblePanel(message, timestamp, isCurrentUser);
        bubble.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageContainer.add(bubble);
        messageContainer.revalidate();
        messageContainer.repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatWindow chatWindow = new ChatWindow("FriendName", "CurrentUser");
            chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            chatWindow.setVisible(true);
        });
    }
}
