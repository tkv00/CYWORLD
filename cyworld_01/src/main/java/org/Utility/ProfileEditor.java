package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ProfileEditor {
    private String username;
    public ProfileEditor(String username) {
        this.username = username;
    }

    public void handleProfileButtonClick() {
        // 프로필 버튼 클릭 시 수행할 동작을 구현해요.
        JFrame profileFrame = new JFrame("프로필 변경");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(300, 200);
        profileFrame.setLayout(new GridLayout(4, 2));

        JLabel idLabel = new JLabel("새로운 ID:");
        JTextField idField = new JTextField();

        JLabel passwordLabel = new JLabel("새로운 비밀번호:");
        JPasswordField passwordField = new JPasswordField();

        JButton changeButton = new JButton("변경");
        changeButton.addActionListener(e -> {
            String newID = idField.getText();
            String newPassword = String.valueOf(passwordField.getPassword());

            // 데이터베이스에서 현재 유저 정보 가져오기
            UserSession userSession = UserSession.getInstance();
            String username = userSession.getUserId(); // 현재 유저 ID 가져오기

            // UserSession을 활용하여 로그인 정보 가져오기
            String[] userInformation = getUserInformation(username);
            String currentPassword = userInformation[1]; // 유저의 비밀번호 가져오기


            if (newID.isEmpty() && newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(profileFrame, "ID나 비밀번호 중 하나는 입력되어야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Connection conn = DatabaseConfig.getConnection();
                    if (!newID.isEmpty() && isValidUsername(newID)) {
                        // 유저 ID 변경
                        String updateIdQuery = "UPDATE user SET username = ? WHERE username = ?";
                        try (PreparedStatement statement = conn.prepareStatement(updateIdQuery)) {
                            statement.setString(1, newID);
                            statement.setString(2, username); // 사용자 ID 가져와서 사용
                            statement.executeUpdate();
                            username = newID;
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else {
                        JOptionPane.showMessageDialog(profileFrame, "올바른 형식의 ID를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!newPassword.isEmpty() && !newPassword.equals(currentPassword) && isValidPassword(newPassword)) {
                        // 비밀번호 변경
                        String updatePasswordQuery = "UPDATE user SET password = ? WHERE username = ?";
                        try (PreparedStatement statement = conn.prepareStatement(updatePasswordQuery)) {
                            statement.setString(1, newPassword);
                            statement.setString(2, username); // 사용자 ID 가져와서 사용
                            statement.executeUpdate();
                        }
                    }
                    else if (!isValidPassword(newPassword)) {
                        JOptionPane.showMessageDialog(profileFrame, "비밀번호는 최소 8자리이며, 문자와 숫자를 포함해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(profileFrame, "정보가 업데이트되었습니다.", "업데이트 완료", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // 업데이트 중 오류 발생 시 오류 메시지 출력
                    JOptionPane.showMessageDialog(profileFrame, "업데이트 중 오류가 발생했습니다.", "업데이트 오류", JOptionPane.ERROR_MESSAGE);
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
        profileFrame.add(cancelButton);
        profileFrame.setVisible(true);
    }
    private String[] getUserInformation(String username){
        String[] userInformation = new String[2]; // 유저 정보를 저장할 배열 [0]: 유저 아이디, [1]: 비밀번호

        try (Connection conn = DatabaseConfig.getConnection()) {
            // 데이터베이스에서 유저 정보를 가져오는 쿼리
            String selectQuery = "SELECT username, password FROM user WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // 데이터베이스에서 가져온 유저 아이디와 비밀번호를 배열에 저장
                    userInformation[0] = rs.getString("username");
                    userInformation[1] = rs.getString("password");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userInformation;
    }
    private boolean isValidUsername(String username) {
        // 아이디는 영문, 숫자 포함 6자 이상
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        return username.matches(usernameRegex);
    }
    private boolean isValidPassword(String password) {
        // 비밀번호는 최소 8자, 최소 하나의 문자와 하나의 숫자를 포함
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }
}