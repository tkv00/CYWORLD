package org.Friend;

import org.Friend.FriendManager;
import org.Utility.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
// FriendAddListener.java
// FriendAddListener.java

public class FriendSearchDialog extends JDialog {
    private FriendAddListener friendAddListener;
    private PlaceholderTextField searchField;
    private JButton searchButton;
    private FriendManager friendManager;
    private String currentUserId;
    private JPanel resultsPanel;

    public FriendSearchDialog(JFrame parent, String currentUserId) {
        super(parent, "유저 검색", true);
        this.currentUserId = currentUserId;
        this.friendManager = new FriendManager();

        String placeholderText = "추가할 일촌의 ID를 검색하세요";
        searchField = new PlaceholderTextField(placeholderText, 20);
        searchButton = new JButton("검색");

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);

        setLayout(new BorderLayout());
        add(searchField, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> searchFriends());
        setSize(500, 300);
    }

    public void setFriendAddListener(FriendAddListener listener) {
        this.friendAddListener = listener;
    }


    private void searchFriends() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            try {
                resultsPanel.removeAll();

                java.util.List<String> results = friendManager.searchFriends(currentUserId, searchText);

                for (String friend : results) {
                    JPanel userPanel = new JPanel();
                    userPanel.setLayout(new BorderLayout());

                    JLabel friendLabel = new JLabel(friend);
                    userPanel.add(friendLabel, BorderLayout.CENTER);

                    JButton addButton = new JButton("일촌 추가");
                    addButton.addActionListener(e -> sendFriendRequest(friend));
                    userPanel.add(addButton, BorderLayout.WEST);

                    resultsPanel.add(userPanel);
                }
                resultsPanel.revalidate();
                resultsPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sendFriendRequest(String selectedUserId) {
        try {
            friendManager.sendFriendRequest(currentUserId, selectedUserId);
            JOptionPane.showMessageDialog(this, selectedUserId + "에게 일촌 신청을 보냈습니다.", "일촌 신청", JOptionPane.INFORMATION_MESSAGE);
            if (friendAddListener != null) {
                friendAddListener.onFriendAdded(selectedUserId);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "일촌 신청 중 오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
