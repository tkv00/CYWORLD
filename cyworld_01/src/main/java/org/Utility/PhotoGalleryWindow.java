package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class PhotoGalleryWindow extends JFrame {
    private final String userId;
    private boolean isReadOnly;

    private JButton addPhotoButton; // 사진 추가 버튼
    private final PhotoGalleryManager photoGalleryManager;
    private JPanel tagPanel;
    private JPanel photoPanel;

<<<<<<< HEAD
    // Keep track of clicked tags
    private Set<String> clickedTags = new HashSet<>();


    public PhotoGalleryWindow(PhotoGalleryManager manager, String userId) {
=======
    public PhotoGalleryWindow(PhotoGalleryManager manager, String userId,boolean isReadOnly) {
>>>>>>> e8a3bb52a4c87c6d321ad2c9a1e5924f54bcce1b
        this.photoGalleryManager = manager;
        this.userId = userId;
        this.isReadOnly = isReadOnly;
        System.out.println("PhotoGalleryWindow initialized with userId: " + userId);
        if (userId == null || userId.trim().isEmpty()) {
            System.err.println("User ID is null or empty in openPhotoGalleryWindow");
            return;  // Handle this situation appropriately
        }
        initializeUI();
    }
<<<<<<< HEAD

    // 사진첩 패널을 생성하는 메서드
    private JPanel createPhotoGalleryPanel() {
        JPanel photoGalleryPanel = new JPanel();
        JLabel photoGalleryLabel = new JLabel("사진첩");

        // Set font size to 15 and make it bold
        Font labelFont = photoGalleryLabel.getFont();
        photoGalleryLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 15));

        photoGalleryPanel.add(photoGalleryLabel);
        return photoGalleryPanel;
    }

=======
    // 사진 추가 버튼 활성화/비활성화 메서드
    public void setAddPhotoButtonEnabled(boolean enabled) {
        if (addPhotoButton != null) {
            addPhotoButton.setEnabled(enabled);
        }
    }
>>>>>>> e8a3bb52a4c87c6d321ad2c9a1e5924f54bcce1b
    // 사용자 인터페이스 초기화
    private void initializeUI() {
        setTitle("사진첩");
        setSize(800, 600);
        setLayout(new BorderLayout());
        // 상단 패널 및 사진 추가 버튼 초기화
        initializeTopPanel();

        // 태그 패널 초기화
        initializeTagPanel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 사진 패널 초기화
        initializePhotoPanel();

        // 초기 사진 표시 (Recent 태그로)
        displayPhotos("Recent"); // "Recent"는 최근 사진을 나타내는 가상의 태그입니다.

        setVisible(!isReadOnly);
    }

    // 상단 패널 및 사진 추가 버튼 초기화
    private void initializeTopPanel() {
        addPhotoButton = new JButton("사진 추가");
        addPhotoButton.setEnabled(!isReadOnly);
        addPhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    photoGalleryManager.uploadPhoto(selectedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "사진 추가 중 오류 발생",
                            "오류",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(addPhotoButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.SOUTH);
    }
    // 태그 패널 초기화
    // 태그 패널 초기화
    private void initializeTagPanel() {
        tagPanel = new JPanel();
        tagPanel.setLayout(new FlowLayout(FlowLayout.LEADING)); // FlowLayout으로 변경
        JScrollPane tagScrollPane = new JScrollPane(tagPanel);
        tagScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(tagScrollPane, BorderLayout.NORTH); // 상단에 배치

        // 데이터베이스에서 태그 목록을 검색하고 태그 라벨을 생성
        initializeTags();
    }

    // 사진 패널 초기화
    private void initializePhotoPanel() {
        // 2열의 그리드 레이아웃을 사용하고, 가로 10, 세로 10의 간격을 가짐
        photoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        photoPanel.setBackground(Color.WHITE);

        JScrollPane photoScrollPane = new JScrollPane(photoPanel);
        photoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // 세로 스크롤 바 필요시 표시
        photoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤 바는 표시하지 않음
        add(photoScrollPane, BorderLayout.CENTER);
    }


    // Initialize tags with a clickable behavior
    // Initialize tags with a clickable behavior
    // Initialize tags with a clickable behavior
    private void initializeTags() {
        Set<String> tags = photoGalleryManager.retrieveTags();
        for (String tag : tags) {
            JLabel tagLabel = new JLabel(tag);

            // Set font size to 14
            Font tagFont = tagLabel.getFont();
            tagLabel.setFont(new Font(tagFont.getName(), Font.PLAIN, 14));

            tagLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Simulate button click action
                    displayPhotos(tag);

                    // Clear the previous clicked tags
                    for (Component component : tagPanel.getComponents()) {
                        JLabel label = (JLabel) component;
                        updateTagColor(label, false);
                    }

                    // Toggle the clicked state of the tag
                    if (clickedTags.contains(tag)) {
                        clickedTags.remove(tag);
                    } else {
                        clickedTags.add(tag);
                    }

                    // Update the text color based on the clicked state
                    updateTagColor(tagLabel, clickedTags.contains(tag));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Change the text color to orange when the mouse enters the label
                    if (!clickedTags.contains(tag)) {
                        tagLabel.setForeground(new Color(255, 102, 6));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Change the text color back to the default when the mouse exits the label
                    if (!clickedTags.contains(tag)) {
                        tagLabel.setForeground(UIManager.getColor("Label.foreground"));
                    }
                }
            });

            tagPanel.add(tagLabel);
        }
    }


    // Update the text color based on the clicked state
    private void updateTagColor(JLabel tagLabel, boolean clicked) {
        if (clicked) {
            tagLabel.setForeground(new Color(255, 102, 6));
        } else {
            tagLabel.setForeground(UIManager.getColor("Label.foreground"));
        }
    }

    // 선택된 태그에 해당하는 사진을 표시하는 메서드
    private void displayPhotos(String tag) {
        List<PhotoGalleryImage> photos = photoGalleryManager.retrievePhotosByTag(tag);
        photoPanel.removeAll();

        if (photos.size() == 2) {
            // 사진이 2개일 때는 1행 2열의 GridLayout 사용
            photoPanel.setLayout(new GridLayout(1, 2, 10, 10));
        } else if (photos.size() > 2) {
            // 사진이 2개보다 많을 때는 원래대로 0행 2열로 설정하여 자동으로 행이 늘어나게 설정
            photoPanel.setLayout(new GridLayout(0, 2, 10, 10));
        } else {
            // 사진이 1개일 때는 FlowLayout을 사용
            photoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        }


        for (PhotoGalleryImage photoDetail : photos) {
            // 각 사진에 대한 패널 생성
            JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
            imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 여백 추가

            // 이미지 라벨 생성 및 추가
            JLabel photoLabel = new JLabel();
            photoLabel.setIcon(new ImageIcon(photoDetail.getImage().getImage().getScaledInstance(280, 230, Image.SCALE_SMOOTH)));
            imagePanel.add(photoLabel, BorderLayout.CENTER);
            // 사진 아래에 표시되는 시간 포맷을 변경합니다.
            String originalTime = photoDetail.getUploadTime(); // 원래 시간 문자열
            String formattedTime = formatDateTime(originalTime); // 변경된 시간 포맷

            // 제목 및 시간 라벨 추가
            // 제목 라벨 및 시간 라벨 추가
            JLabel titleLabel = new JLabel(photoDetail.getTitle(), SwingConstants.CENTER);
            titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 16)); // Set font size to 16 and make it bold
            JLabel timeLabel = new JLabel(formattedTime, SwingConstants.CENTER);
            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.add(titleLabel);
            textPanel.add(timeLabel);
            imagePanel.add(textPanel, BorderLayout.SOUTH);

            photoPanel.add(imagePanel);
        }

        // 사진이 없을 때 패널 크기를 줄이기 위한 조건문
        if (photos.isEmpty()) {
            photoPanel.setPreferredSize(new Dimension(800, 10)); // 최소 높이 설정
        } else {
            photoPanel.setPreferredSize(null); // 패널 크기 자동 조절
        }

        photoPanel.revalidate();
        photoPanel.repaint();
    }

    // 시간 포맷을 변경하는 헬퍼 메서드
    private String formatDateTime(String dateTimeStr) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 원래의 시간 포맷
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 변경하고 싶은 시간 포맷
            Date date = originalFormat.parse(dateTimeStr);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateTimeStr; // 파싱에 실패할 경우 원래 문자열을 반환합니다.
        }
    }
    // '사진 추가' 버튼을 비활성화하는 메서드
    public void disableAddPhotoButton() {
        if (addPhotoButton != null) {
            addPhotoButton.setEnabled(false);
        }
    }
}

