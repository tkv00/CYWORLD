package org.Friend;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class FriendRequestDialog extends JDialog {
    private JList<String> friendRequestList;
    private FriendManager friendManager;
    private String currentUserId;

    public FriendRequestDialog(JFrame parent, String currentUserId,List<String> friendRequest) {
        super(parent, "일촌 신청 확인", true);
        this.friendManager = new FriendManager();
        this.currentUserId = currentUserId;

        friendRequestList = new JList<>(new Vector<>(friendRequest)); // 친구 신청 목록 설정
        add(new JScrollPane(friendRequestList), BorderLayout.SOUTH);


        JButton acceptButton = new JButton("수락");
        acceptButton.addActionListener(e -> acceptFriendRequest());
        JButton declineButton = new JButton("거절");
        declineButton.addActionListener(e -> declineFriendRequest());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(300, 400);
        setLocationRelativeTo(parent);
    }

    private void loadFriendRequests() {
        try {
            List<String> requests = friendManager.getFriendRequests(currentUserId);
            friendRequestList.setListData(requests.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 처리
        }
    }

    private void acceptFriendRequest() {
        String selectedUser = friendRequestList.getSelectedValue();
        if (selectedUser != null) {
            try {
                friendManager.acceptFriendRequest(currentUserId, selectedUser);
                loadFriendRequests();
                JOptionPane.showMessageDialog(this, "일촌 요청이 수락되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "일촌 요청 수락 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void declineFriendRequest() {
        String selectedUser = friendRequestList.getSelectedValue();
        if (selectedUser != null) {
            try {
                friendManager.declineFriendRequest(currentUserId, selectedUser);
                loadFriendRequests();
                JOptionPane.showMessageDialog(this, "일촌 요청이 거부되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                // 오류 처리
                JOptionPane.showMessageDialog(this, "일촌 요청 거부 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
