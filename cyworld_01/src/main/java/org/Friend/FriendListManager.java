package org.Friend;

import org.Utility.DatabaseConfig;
import org.Utility.UserSession;
import org.example.MiniHomepage;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class FriendListManager {
    private JFrame parentFrame;
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

    public void showFriendListDialog() {
        try {
            String userId = UserSession.getInstance().getUserId();
            JDialog friendListDialog = new JDialog(parentFrame, "친구 목록", true);
            friendListDialog.setLayout(new BorderLayout());

            List<String> friends = friendManager.getAcceptedFriendRequests(userId);

            DefaultListModel<String> model = new DefaultListModel<>();
            for (String friend : friends) {
                model.addElement(friend);
            }
            JList<String> friendList = new JList<>(model);
            friendListDialog.add(new JScrollPane(friendList), BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton messageButton = new JButton("쪽지 보내기");
            messageButton.addActionListener(e -> {
                if (!friendList.isSelectionEmpty()) {
                    String selectedFriend = friendList.getSelectedValue();
                    sendNoteToFriend(selectedFriend);
                }
            });
            buttonPanel.add(messageButton);
            friendListDialog.add(buttonPanel, BorderLayout.SOUTH);
            friendListDialog.setSize(300, 400);
            friendListDialog.setLocationRelativeTo(parentFrame);
            friendListDialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, "친구 목록을 불러오는데 실패했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void sendNoteToFriend(String friend) {
        String note = JOptionPane.showInputDialog(parentFrame, friend + "에게 보낼 쪽지 내용:");
        if (note != null && !note.trim().isEmpty()) {
            try (Connection connection = DatabaseConfig.getConnection()) {
                String sql = "INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, UserSession.getInstance().getUserId());
                    statement.setString(2, friend);
                    statement.setString(3, note);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(parentFrame, "쪽지가 성공적으로 보내졌습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentFrame, "쪽지 보내기에 실패했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "쪽지 내용을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
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
}
