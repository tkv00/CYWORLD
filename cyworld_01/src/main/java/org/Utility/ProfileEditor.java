package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.showMessageDialog;

public class ProfileEditor {
    private String username;
    private Component profileFrame;

    public ProfileEditor(String username) {
        if (username != null) {
            this.username = username;
        }
    }
    public void handleProfileButtonClick() {
        // 프로필 버튼 클릭 시 수행할 동작을 구현해요.
        JFrame profileFrame = new JFrame("프로필 변경");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 300);
        profileFrame.setLayout(new GridLayout(10, 5));

        JLabel idLabel = new JLabel("새로운 ID:");
        JTextField idField = new JTextField();

        JLabel passwordLabel = new JLabel("새로운 비밀번호:");
        JPasswordField passwordField = new JPasswordField();

        JLabel reviewLabel = new JLabel("한 줄평:");
        JTextField reviewField = new JTextField();

        JButton addReviewButton = new JButton("추가/변경");
        addReviewButton.addActionListener(e -> {
            String reviewText = reviewField.getText();
            if (!reviewText.isEmpty()) {
                saveReviewToDatabase(reviewText);
            } else {
                showMessageDialog(profileFrame, "한 줄평을 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton changeButton = new JButton("변경");
        changeButton.addActionListener(e -> {
            String newID = idField.getText();
            String newPassword = String.valueOf(passwordField.getPassword());

            // 데이터베이스에서 현재 유저 정보 가져오기
            UserSession userSession = UserSession.getInstance();
            String currentUser = userSession.getUserId();

            // UserSession을 활용하여 로그인 정보 가져오기
            String[] userInformation = getUserInformation(username);
            String currentPassword = userInformation[1]; // 유저의 비밀번호 가져오기

            if (newID.isEmpty() && newPassword.isEmpty()) {
                showMessageDialog(profileFrame, "ID나 비밀번호 중 하나는 입력되어야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Connection conn = DatabaseConfig.getConnection();
                    if (!newID.isEmpty() && isValidUsername(newID)) {
                        // 유저 ID 변경
                        String updateIdQuery = "UPDATE user SET username = ? WHERE username = ?";
                        try (PreparedStatement statement = conn.prepareStatement(updateIdQuery)) {
                            statement.setString(1, newID);
                            statement.setString(2, currentUser); // 사용자 ID 가져와서 사용
                            statement.executeUpdate();
                            currentUser = newID;
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else {
                        showMessageDialog(profileFrame, "올바른 형식의 ID를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!newPassword.isEmpty() && !newPassword.equals(currentPassword) && isValidPassword(newPassword)) {
                        // 비밀번호 변경
                        String updatePasswordQuery = "UPDATE user SET password = ? WHERE username = ?";
                        try (PreparedStatement statement = conn.prepareStatement(updatePasswordQuery)) {
                            statement.setString(1, newPassword);
                            statement.setString(2, currentUser); // 사용자 ID 가져와서 사용
                            statement.executeUpdate();
                        }
                    }
                    else if (!isValidPassword(newPassword)) {
                        showMessageDialog(profileFrame, "비밀번호는 최소 8자리이며, 문자와 숫자를 포함해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    showMessageDialog(profileFrame, "정보가 업데이트되었습니다.", "업데이트 완료", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // 업데이트 중 오류 발생 시 오류 메시지 출력
                    showMessageDialog(profileFrame, "업데이트 중 오류가 발생했습니다.", "업데이트 오류", JOptionPane.ERROR_MESSAGE);
                }
                profileFrame.dispose(); // 프레임 닫기
            }
        });
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> profileFrame.dispose());
        profileFrame.add(idLabel);
        profileFrame.add(idField);
        profileFrame.add(passwordLabel);
        profileFrame.add(passwordField);
        profileFrame.add(changeButton);
        profileFrame.add(reviewLabel);
        profileFrame.add(reviewField);
        profileFrame.add(addReviewButton);
        profileFrame.add(cancelButton);
        profileFrame.setVisible(true);
    }
    private String[] getUserInformation(String username){
        String[] userInformation = new String[10]; // 유저 정보를 저장할 배열
        try (Connection conn = DatabaseConfig.getConnection()) {
            // 데이터베이스에서 유저 정보를 가져오는 쿼리
            String selectQuery = "SELECT usernameFROM user WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    userInformation[0] = rs.getString("username");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userInformation;
    }
    private String getCurrentUser(String username) {
        String currentUser = "";
        try (Connection conn = DatabaseConfig.getConnection()) {
            String selectQuery = "SELECT username FROM user WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentUser = rs.getString("username");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return currentUser;
    }
    private void saveReviewToDatabase(String reviewText) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String selectQuery = "SELECT username FROM user WHERE username = ?";
            // 해당 사용자가 있는지 확인
            try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery)) {
                selectStatement.setString(1, username);
                ResultSet rs = selectStatement.executeQuery();
                if (rs.next()) {
                    // 해당 사용자의 정보가 이미 있는 경우, UPDATE 수행
                    String updateReviewQuery = "UPDATE user SET reviewtext = ? WHERE username = ?";
                    try (PreparedStatement updateStatement = conn.prepareStatement(updateReviewQuery)) {
                        updateStatement.setString(1, reviewText);
                        updateStatement.setString(2, username);
                        updateStatement.executeUpdate();
                        showMessageDialog(profileFrame, "한 줄평이 업데이트되었습니다.", "업데이트 완료", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // 해당 사용자의 정보가 없는 경우, INSERT 수행
                    String insertReviewQuery = "INSERT INTO user (username, reviewtext) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = conn.prepareStatement(insertReviewQuery)) {
                        insertStatement.setString(1, username);
                        insertStatement.setString(2, reviewText);
                        insertStatement.executeUpdate();
                        showMessageDialog(profileFrame, "새로운 한 줄평이 추가되었습니다.", "추가 완료", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessageDialog(profileFrame, "한 줄평 업데이트 중 오류가 발생했습니다.", "업데이트 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidUsername(String username) {
        // 아이디는 영문, 숫자 포함 6자 이상
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        return username.matches(usernameRegex);
    }
    private boolean isValidPassword(String password) {
        // 비밀번호는 최소 8자, 최소 하나의 문자와 하나의 숫자를 포함
        String passwordRegex = "^(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).find();
    }
    public String getUserReviewText(String username) {
        String reviewText = null;
        try (Connection conn = DatabaseConfig.getConnection()) {
            // 데이터베이스에서 해당 유저의 한 줄평 정보를 가져오는 쿼리
            String selectQuery = "SELECT reviewtext FROM user WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    reviewText = rs.getString("reviewtext");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reviewText;
    }
    public String getProfileComment() {
        return getUserReviewText(username);
    }
}