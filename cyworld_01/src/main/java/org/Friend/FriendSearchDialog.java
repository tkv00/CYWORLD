package org.Friend;

import org.Friend.FriendManager;
import org.Utility.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FriendSearchDialog extends JDialog {
    private PlaceholderTextField searchField; // PlaceholderTextField로 변경
    private JButton searchButton;
    private FriendManager friendManager;
    private String currentUserId;
    private JPanel resultsPanel; // 검색 결과를 표시할 패널

    public FriendSearchDialog(JFrame parent, String currentUserId) {
        super(parent, "유저 검색", true);
        this.currentUserId = currentUserId;
        this.friendManager = new FriendManager();

        // Placeholder 텍스트 설정
        String placeholderText = "추가할 일촌의 ID를 검색하세요";
        searchField = new PlaceholderTextField(placeholderText, 20);
        searchButton = new JButton("검색");


        // 검색 결과 패널 초기화
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);

        // 컴포넌트 추가
        setLayout(new BorderLayout());
        add(searchField, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);

        // 검색 버튼 이벤트 핸들러
        searchButton.addActionListener(e -> searchFriends());

        // 창 크기 설정
        setSize(500, 300);


    }

    private void searchFriends() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            try {
                // Clear previous results
                resultsPanel.removeAll(); // 이전 검색 결과 제거

                // Fetch search results from the FriendManager
                java.util.List<String> results = friendManager.searchFriends(currentUserId, searchText);

                // Create a panel for each result with a button
                for (String friend : results) {
                    JPanel userPanel = new JPanel();
                    userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    userPanel.add(new JLabel(friend));

                    JButton addButton = new JButton("일촌 추가");
                    addButton.addActionListener(e -> sendFriendRequest(friend));
                    userPanel.add(addButton);

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

    // '일촌 추가' 버튼 이벤트 핸들러
    public void sendFriendRequest(String selectedUserId) {
        try {
            friendManager.sendFriendRequest(currentUserId, selectedUserId);
            JOptionPane.showMessageDialog(this, selectedUserId + "에게 일촌 신청을 보냈습니다.", "일촌 신청", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "일촌 신청 중 오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
