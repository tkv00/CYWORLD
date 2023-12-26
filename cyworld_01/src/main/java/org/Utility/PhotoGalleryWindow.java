package org.Utility;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class PhotoGalleryWindow extends JFrame {
    private final String userId;
    private boolean isReadOnly;

    private JButton addPhotoButton; // 사진 추가 버튼
    private final PhotoGalleryManager photoGalleryManager;
    private JPanel tagPanel;
    private JPanel photoPanel;

    public PhotoGalleryWindow(PhotoGalleryManager manager, String userId,boolean isReadOnly) {
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
    // 사진 추가 버튼 활성화/비활성화 메서드
    public void setAddPhotoButtonEnabled(boolean enabled) {
        if (addPhotoButton != null) {
            addPhotoButton.setEnabled(enabled);
        }
    }
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

        // 초기 사진 표시
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
        add(topPanel, BorderLayout.NORTH);
    }
    // 태그 패널 초기화
    private void initializeTagPanel() {
        tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.Y_AXIS));
        JScrollPane tagScrollPane = new JScrollPane(tagPanel);
        tagScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(tagScrollPane, BorderLayout.WEST);

        // 데이터베이스에서 태그 목록을 검색하고 태그 버튼을 생성
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


    // 데이터베이스에서 태그 목록을 검색하고 태그 버튼을 생성하는 메서드
    private void initializeTags() {
        Set<String> tags = photoGalleryManager.retrieveTags();
        for (String tag : tags) {
            JButton tagButton = new JButton(tag);
            tagButton.addActionListener(e -> displayPhotos(tag));
            tagPanel.add(tagButton);
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
            JLabel titleLabel = new JLabel(photoDetail.getTitle(), SwingConstants.CENTER);
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

