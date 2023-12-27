package org.Friend;

import org.Utility.DatabaseConfig;
import org.example.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.Friend.FriendListManager.LOGGER;

public class FriendManager {

    // 친구 신청 보내기
    public void sendFriendRequest(String userId, String friendId) throws Exception {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 'pending')";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                statement.setString(2, friendId);
                statement.executeUpdate();
            }
        }
    }

    // 친구 신청 수락
    public void acceptFriendRequest(String userId, String friendId) throws Exception {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "UPDATE friends SET status = 'accepted' WHERE user_id = ? AND friend_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, friendId);
                statement.setString(2, userId);
                statement.executeUpdate();
            }
        }
    }

    // 친구 신청 거부
    public void declineFriendRequest(String userId, String friendId) throws Exception {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "UPDATE friends SET status = 'declined' WHERE user_id = ? AND friend_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, friendId); // 주의: 순서가 반대입니다.
                statement.setString(2, userId);
                statement.executeUpdate();
            }
        }
    }

    // 친구 목록 조회
    // Method to get pending friend requests
    public List<String> getFriendRequests(String userId) throws Exception {
        List<String> requests = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT user_id FROM friends WHERE friend_id = ? AND status = 'pending'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        requests.add(resultSet.getString("user_id"));
                    }
                }
            }
        }
        return requests;
    }
    //이미 일촌추가한 목록 가져옴
    public List<String> getSentFriendRequests(String userId) throws Exception {
        List<String> sentRequests = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT friend_id FROM friends WHERE user_id = ? AND status = 'pending'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        sentRequests.add(resultSet.getString("friend_id"));
                    }
                }
            }
        }
        return sentRequests;
    }

    // Search for friends based on search text
    public List<String> searchFriends(String userId, String searchText) throws Exception {
        List<String> searchResults = new ArrayList<>();
        List<String> sentRequests = getSentFriendRequests(userId); // 이미 보낸 요청 목록 가져오기

        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT username FROM user WHERE username LIKE ? AND username != ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%" + searchText + "%");
                statement.setString(2, userId); // 현재 사용자 제외

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String friendId = resultSet.getString("username");
                        if (!sentRequests.contains(friendId)) { // 이미 보낸 요청에 없는 경우만 추가
                            searchResults.add(friendId);
                        }
                    }
                }
            }
        }
        return searchResults;
    }
    // 친구 신청 목록 조회
    public List<String> getPendingFriendRequests(String userId) throws Exception {
        List<String> pendingRequests = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) {
            // 'pending' 상태인 친구 신청을 조회
            String sql = "SELECT user_id FROM friends WHERE friend_id = ? AND status = 'pending'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        pendingRequests.add(resultSet.getString("user_id"));
                    }
                }
            }
        }
        return pendingRequests;
    }
    // 서로 친구인 유저 목록을 가져오는 메소드
    public List<String> getAcceptedFriendRequests(String userId) throws Exception {
        List<String> acceptedRequests = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT user_id FROM friends WHERE friend_id = ? AND status = 'accepted'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        acceptedRequests.add(resultSet.getString("user_id"));
                    }
                }
            }
        }
        return acceptedRequests;
    }


    public List<Message> getSentMessages(String userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT sender_id, receiver_id, title, message, send_time FROM messages WHERE sender_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String sender = rs.getString("sender_id");
                    String receiver = rs.getString("receiver_id");
                    String title = rs.getString("title");
                    String content = rs.getString("message");
                    String sentTime = rs.getString("send_time");

                    messages.add(new Message(sender, receiver, title, content, sentTime));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching sent messages", e);
            // 오류 발생 시 빈 리스트를 반환
        }

        return messages; // 항상 리스트 반환, null이 아님
    }

    // 받은 쪽지 목록을 조회하는 메서드
    public List<Message> getReceivedMessages(String userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT sender_id, receiver_id, title, message, send_time FROM messages WHERE receiver_id = ?";

        try (Connection conn = DatabaseConfig.getConnection(); // 데이터베이스 연결
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId); // 현재 사용자 ID 설정

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // 쿼리 결과로부터 쪽지 정보 추출
                    String sender = rs.getString("sender_id");
                    String receiver = rs.getString("receiver_id");
                    String title = rs.getString("title");
                    String content = rs.getString("message");
                    String sentTime = String.valueOf(rs.getTimestamp("send_time")); // 보낸 시간

                    // 추출한 정보로 Message 객체 생성 및 리스트에 추가
                    messages.add(new Message(sender, receiver, title, content, sentTime));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching received messages", e);
            // 오류 발생 시 빈 리스트를 반환합니다. 이렇게 함으로써 메서드 호출자는 null을 처리할 필요가 없습니다.
        }

        return messages; // 조회된 쪽지 목록 반환, 항상 null이 아닌 리스트 반환
    }
}
