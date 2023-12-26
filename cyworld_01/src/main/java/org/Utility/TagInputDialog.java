package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TagInputDialog extends JDialog {
    private JTextField inputField;
    private String tags = "";

    public TagInputDialog(Component parentComponent) {
        // Set up the dialog
        setTitle("Enter Tags");
        setModal(true);
        setLayout(new BorderLayout());

        // Label
        JLabel messageLabel = new JLabel("태그를 입력하세요 (#태그, 엔터로 구분):");
        add(messageLabel, BorderLayout.NORTH);

        // Input field
        inputField = new JTextField(20);
        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = inputField.getText().trim();
                    if (text.startsWith("#") && text.length() > 1) {
                        tags += text + " ";
                        inputField.setText("");
                    }
                }
            }
        });
        add(inputField, BorderLayout.CENTER);

        // OK Button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        add(okButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parentComponent);
    }

    public String getTags() {
        return tags.trim();
    }
}
