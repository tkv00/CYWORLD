package org.Utility;

import org.example.Panel.ProfilePanel;
import javax.swing.*;
import java.awt.*;

public class ProfileButton {
    private ProfilePanel profilePanel;

    public ProfileButton(ProfilePanel profilePanel) {
        this.profilePanel = profilePanel;
    }

    public void handleProfileButtonClick() {
        // 프로필 버튼 클릭 시 수행할 동작을 구현해요.
        JFrame profileFrame = new JFrame("프로필 변경");
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(300, 200);
        profileFrame.setLayout(new GridLayout(3, 2));

        JLabel idLabel = new JLabel("새로운 ID:");
        JTextField idField = new JTextField();

        JLabel passwordLabel = new JLabel("새로운 비밀번호:");
        JPasswordField passwordField = new JPasswordField();

        JButton changeButton = new JButton("변경");
        changeButton.addActionListener(e -> {
            String newID = idField.getText();
            String newPassword = String.valueOf(passwordField.getPassword());

            // 여기서 입력 받은 새로운 ID와 비밀번호로 변경하는 로직을 구현할 수 있어요.

            profileFrame.dispose();
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
}
