package org.example;

import org.Utility.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;
import java.sql.SQLException;

public class SignUppage {


    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField nameField;
    private JTextField emailField;
    private LoginPage loginPage;

    public SignUppage(LoginPage loginPage) {
        this.loginPage=loginPage;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("회원가입");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(7, 2)); // 로우 개수 증가

        // UI 컴포넌트 초기화 및 프레임에 추가
        addFormField("이름", nameField = new JTextField());
        addFormField("아이디", usernameField = new JTextField());
        addFormField("비밀번호", passwordField = new JPasswordField());
        addFormField("휴대폰 번호", phoneField = new JTextField());
        addFormField("이메일", emailField = new JTextField());


        // 회원가입 버튼 및 이벤트 처리
        JButton signUpButton = createStyledButton("회원가입", 15);
        frame.add(signUpButton);
        signUpButton.addActionListener(e->performSignUp());

        // 취소 버튼
        JButton cancelButton = createStyledButton("취소", 15);// 텍스트 색을 검은색으로 설정
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e ->loginPage.show());
        frame.add(cancelButton);
    }

    private void addFormField(String label, JComponent component) {
        frame.add(new JLabel(label));
        frame.add(component);
    }

    private JButton createStyledButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Nanum Font", Font.BOLD, fontSize));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        button.setForeground(new Color(255, 102, 6));
        return button;
    }

    private void performSignUp() {
        String username = usernameField.getText();
        String userPassword = new String(passwordField.getPassword());
        String phone = phoneField.getText();
        String name = nameField.getText();
        String email = emailField.getText();

        StringBuilder errorMessage = new StringBuilder();

        // 아이디 유효성 검사
        if (!isValidUsername(username)) {
            errorMessage.append("아이디는 영문과 숫자를 포함한 6자 이상이어야 합니다.\n");
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(userPassword)) {
            errorMessage.append("비밀번호는 최소 8자이며, 숫자, 대문자, 소문자, 특수문자를 모두 포함해야 합니다.\n");
        }

        // 전화번호 유효성 검사
        if (!isValidPhoneNumber(phone)) {
            errorMessage.append("휴대폰 번호는 숫자 11자리여야 합니다.\n");
        }

        // 이메일 유효성 검사
        if (!isValidEmail(email)) {
            errorMessage.append("유효하지 않은 이메일 형식입니다.\n");
        }

        // 아이디 중복 검사
        if (isUsernameTaken(username)) {
            errorMessage.append("이미 사용 중인 아이디입니다.\n");
        }

        // 에러 메시지가 있을 경우, 사용자에게 한번에 보여줌
        if (errorMessage.length() > 0) {
            JOptionPane.showMessageDialog(frame, errorMessage.toString(), "유효성 오류", JOptionPane.ERROR_MESSAGE);
            return; // 메서드 종료
        }

        // 데이터베이스에 사용자 정보 저장 시도
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user (username, password, phonenumber, name, email) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setString(1, username);
            pstmt.setString(2, userPassword);
            pstmt.setString(3, phone);
            pstmt.setString(4, name);
            pstmt.setString(5, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(frame, "회원가입에 성공했습니다.");
                frame.dispose(); // 회원가입 창 닫기
            } else {
                JOptionPane.showMessageDialog(frame, "회원가입에 실패했습니다.", "회원가입 오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "데이터베이스 오류: " + ex.getMessage(), "데이터베이스 오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
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

    private boolean isUsernameTaken(String username) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            pstmt.setString(1, username);
            return pstmt.executeQuery().next(); // 아이디가 이미 존재하면 true 반환
        } catch (SQLException ex) {
            ex.printStackTrace();
            return true; // 오류 발생 시 중복으로 처리
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}

