package org.Friend;

import org.Utility.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                statement.setString(1, friendId); // 주의: 순서가 반대입니다.
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




}
