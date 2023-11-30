package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;
import java.sql.SQLException;

public class SignUppage {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField nameField;
    private JTextField emailField;

    public SignUppage() {
        frame = new JFrame("회원가입");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(7, 2)); // 로우 개수 증가

        // 아이디 입력 필드
        frame.add(new JLabel("아이디:"));
        usernameField = new JTextField();
        frame.add(usernameField);

        // 비밀번호 입력 필드
        frame.add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        // 휴대폰 번호 입력 필드
        frame.add(new JLabel("휴대폰 번호:"));
        phoneField = new JTextField();
        frame.add(phoneField);

        // 이메일 입력 필드
        frame.add(new JLabel("이메일:"));
        emailField = new JTextField();
        frame.add(emailField);


        // 이름 입력 필드
        frame.add(new JLabel("이름:"));
        nameField = new JTextField();
        frame.add(nameField);

        // 회원가입 버튼
        JButton signUpButton = new JButton("회원가입");
        frame.add(signUpButton);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSignUp();
            }
        });

        // 취소 버튼
        JButton cancelButton = new JButton("취소");
        frame.add(cancelButton);
        cancelButton.addActionListener(e -> frame.dispose());
    }

    private void performSignUp() {
        String username = usernameField.getText();
        String userPassword = new String(passwordField.getPassword());
        String phone = phoneField.getText();
        String name = nameField.getText();
        // 이메일 필드가 추가되어야 함
        String email = emailField.getText();

        // 유효성 검사 수행
        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(frame, "아이디는 영문과 숫자를 포함한 6자 이상이어야 합니다.", "유효성 오류", JOptionPane.ERROR_MESSAGE);

        }
        if (!isValidPassword(userPassword)) {
            JOptionPane.showMessageDialog(frame, "비밀번호는 최소 8자이며, 숫자, 대문자, 소문자, 특수문자를 모두 포함해야 합니다.", "유효성 오류", JOptionPane.ERROR_MESSAGE);

        }
        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(frame, "휴대폰 번호는 숫자 11자리여야 합니다.", "유효성 오류", JOptionPane.ERROR_MESSAGE);

        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(frame, "유효하지 않은 이메일 형식입니다.", "유효성 오류", JOptionPane.ERROR_MESSAGE);

        }
        String dbUrl = "jdbc:sqlite:identifier.sqlite";



        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password, phone, name,email) VALUES (?, ?, ?, ?,?)")) {

            pstmt.setString(1, username);
            pstmt.setString(2, userPassword); // 추후 비밀번호 암호화 고려
            pstmt.setString(3, phone);
            pstmt.setString(4, name);
            pstmt.setString(5,email);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(frame, "회원가입에 성공했습니다.");
            } else {
                JOptionPane.showMessageDialog(frame, "회원가입에 실패했습니다.", "회원가입 오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "데이터베이스 오류: " + ex.getMessage(), "데이터베이스 오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();//
        }
    }

    public void show() {
        frame.setVisible(true);
    }
    //이메일 유효성검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    //비번 유효성검사
    private boolean isValidPassword(String password) {
        // 비밀번호는 최소 8자, 최소 하나의 문자와 하나의 숫자를 포함
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }
    //전번 유효성검사
    private boolean isValidPhoneNumber(String phone) {
        // 휴대폰 번호는 숫자로만 구성된 11자리
        String phoneRegex = "^\\d{11}$";
        return phone.matches(phoneRegex);
    }
    //아이디 유효성검사
    private boolean isValidUsername(String username) {
        // 아이디는 영문, 숫자 포함 6자 이상
        String usernameRegex = "^[A-Za-z0-9]{6,}$";
        return username.matches(usernameRegex);
    }

}
