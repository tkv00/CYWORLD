package org.Utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class PhotoGalleryWindow extends JFrame {
    private final String userId;
    private boolean isReadOnly;
    private static final int MIN_ZOOM = 1;
    private static final int MAX_ZOOM = 20;

    private JXMapViewer mapViewer; // 지도 뷰어 객체
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
    private void initializeUI() {
        setTitle("사진첩");
        setSize(800, 600);
        setLayout(new BorderLayout());
        initializeTopPanel();
        initializeTagPanel();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializePhotoPanel();
        displayPhotos("Recent");
        setVisible(isReadOnly);
    }
    private void initializeTopPanel() {
        addPhotoButton = new JButton("사진 추가");
        //addPhotoButton.setEnabled(!isReadOnly);
        addPhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String title = JOptionPane.showInputDialog(this, "제목을 입력하세요:");
                TagInputDialog tagDialog = new TagInputDialog(this);
                tagDialog.setVisible(true);
                String tags = tagDialog.getTags();
                if (title != null && !tags.isEmpty()) {
                    openPlaceSearchDialog(selectedFile, title, tags);
                } else {
                    JOptionPane.showMessageDialog(this, "제목과 최소 하나의 태그를 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(addPhotoButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
    }
    private JXMapViewer setupMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));

        PanMouseInputListener panMouseInputListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panMouseInputListener);
        mapViewer.addMouseMotionListener(panMouseInputListener);
        mapViewer.addMouseWheelListener(panMouseInputListener);

        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(new GeoPosition(37.5665, 126.9780));

        JButton zoomInButton = new JButton("+");
        JButton zoomOutButton = new JButton("-");
        zoomInButton.addActionListener(e -> {
            int newZoom = Math.min(mapViewer.getZoom() - 1, MAX_ZOOM);
            mapViewer.setZoom(newZoom);
        });
        zoomOutButton.addActionListener(e -> {
            int newZoom = Math.max(mapViewer.getZoom() +1, MIN_ZOOM);
            mapViewer.setZoom(newZoom);
        });

        JPanel zoomPanel = new JPanel(new GridLayout(2, 1));
        zoomPanel.add(zoomInButton);
        zoomPanel.add(zoomOutButton);
        mapViewer.setLayout(new BorderLayout());
        mapViewer.add(zoomPanel, BorderLayout.EAST);

        return mapViewer;
    }
    // 사진 추가 버튼 활성화/비활성화 메서드
    public void setAddPhotoButtonEnabled(boolean enabled) {
        if (addPhotoButton != null) {
            addPhotoButton.setEnabled(enabled);
        }
    }

    // 상단 패널 및 사진 추가 버튼 초기화
    private void initializeTagPanel() {
        tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.Y_AXIS));
        JScrollPane tagScrollPane = new JScrollPane(tagPanel);
        tagScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(tagScrollPane, BorderLayout.WEST);

        // 데이터베이스에서 태그 목록을 검색하고 태그 버튼을 생성
        initializeTags();
    }

    private void openPlaceSearchDialog(File photoFile, String title, String tags) {
        JDialog searchDialog = new JDialog(this, "사진 찍은 장소 검색", true);
        searchDialog.setSize(1000, 600);
        searchDialog.setLayout(new BorderLayout());

        // 검색 패널 설정
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // 지도 설정
        JXMapViewer searchMapViewer = setupMapViewer();
       DefaultListModel<JPanel> searchResultModel = new DefaultListModel<>();
        JList<JPanel> searchResults = new JList<>(searchResultModel);
        searchResults.setCellRenderer(new SearchResultRenderer());
        JScrollPane resultListScroller = new JScrollPane(searchResults);
        resultListScroller.setPreferredSize(new Dimension(300, 600));

        JPanel bottomPanel = new JPanel(new BorderLayout()); // 하단 패널 추가
        searchDialog.add(bottomPanel, BorderLayout.SOUTH);


        // 검색 버튼 액션 리스너 설정
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                bottomPanel.removeAll();
                new Thread(() -> {
                    try {
                        JSONArray places = new KakaoMapManager().searchPlace(keyword);
                        SwingUtilities.invokeLater(() -> {
                            searchResultModel.clear();
                            for (int i = 0; i < places.length(); i++) {
                                JSONObject place = places.getJSONObject(i);
                                String placeName = place.getString("location");
                                JPanel panel = new JPanel();
                                panel.add(new JLabel(placeName));
                                searchResultModel.addElement(panel);
                            }
                            if (places.length() == 0) {
                                JOptionPane.showMessageDialog(searchDialog, "검색 결과가 없습니다.", "검색 결과", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(searchDialog, "검색 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE)
                        );
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(searchDialog, "검색어를 입력해주세요.", "검색 오류", JOptionPane.WARNING_MESSAGE);
            }
        });
        searchResults.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !searchResultModel.isEmpty()) {
                JPanel selectedPanel = searchResults.getSelectedValue();
                if (selectedPanel != null) {
                    JLabel placeLabel = (JLabel) selectedPanel.getComponent(0);
                    String selectedLocation = placeLabel.getText();
                    GeoPosition position = getGeoPositionFromKakaoMap(selectedLocation);
                    if (position != null) {
                        searchMapViewer.setAddressLocation(position);
                        searchMapViewer.setZoom(10);

                        Set<Waypoint> waypoints = new HashSet<>();
                        waypoints.add(new DefaultWaypoint(position));
                        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
                        waypointPainter.setWaypoints(waypoints);
                        searchMapViewer.setOverlayPainter(waypointPainter);

                        JButton selectButton = new JButton("선택");
                        selectButton.addActionListener(ev -> {
                            selectLocation(selectedLocation, photoFile, title, tags, searchMapViewer, searchDialog);
                        });
                        bottomPanel.removeAll(); // 이전에 추가된 선택 버튼을 제거합니다.
                        bottomPanel.add(selectButton, BorderLayout.EAST); // 패널에 선택 버튼 추가
                        selectButton.setPreferredSize(new Dimension(100, 40)); // 버튼의 선호하는 크기 설정
                        bottomPanel.revalidate();
                        bottomPanel.repaint();
                    }
                }
            }
        });

        // 다이얼로그에 컴포넌트 추가
        searchDialog.add(searchPanel, BorderLayout.NORTH);
        searchDialog.add(resultListScroller, BorderLayout.EAST);
        searchDialog.add(searchMapViewer, BorderLayout.CENTER);  // 수정된 부분: searchMapViewer를 사용
        searchDialog.setVisible(true);
    }


    // 선택 버튼을 눌렀을 때 실행되는 메서드
    private void selectLocation(String placeName, File photoFile, String title, String category, JXMapViewer mapViewer, JDialog dialog) {
        GeoPosition position = getGeoPositionFromKakaoMap(placeName);
        if (position != null) {
            mapViewer.setAddressLocation(position);
            try {
                byte[] imageData = Files.readAllBytes(photoFile.toPath());
                photoGalleryManager.uploadPhotoWithLocation(title, category, placeName, imageData);
                JOptionPane.showMessageDialog(dialog, "사진 및 위치 정보가 성공적으로 업로드되었습니다.");
                dialog.dispose();
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "사진 및 위치 정보 추가 중 오류 발생: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "선택한 위치의 정보를 찾을 수 없습니다.", "위치 검색 오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    private GeoPosition getGeoPositionFromKakaoMap(String placeName) {
        try {
            String urlString = "http://localhost:3000/kakao-map-api?query=" + URLEncoder.encode(placeName, "UTF-8");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(url.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                scanner.close();

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("documents")) {
                    JSONArray documents = jsonObject.getJSONArray("documents");

                    if (documents.length() > 0) {
                        JSONObject document = documents.getJSONObject(0);
                        if (document.has("y") && document.has("x")) {
                            double latitude = document.getDouble("y");
                            double longitude = document.getDouble("x");
                            return new GeoPosition(latitude, longitude);
                        }
                    } else {
                        System.out.println("No location found for the given place name.");
                    }
                }
            } else {
                System.out.println("Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            // 사진이 2개일 때는 상단에 배치하기 위해 1행 2열의 GridLayout 사용
            photoPanel.setLayout(new GridLayout(1, 2, 10, 10));
        } else if (photos.size() > 2) {
            // 사진이 2개보다 많을 때는 원래대로 0행 2열로 설정하여 자동으로 행이 늘어나게 설정
            photoPanel.setLayout(new GridLayout(0, 2, 10, 10));
        } else {
            // 사진이 1개 또는 없을 때는 상단에 정렬하기 위해 FlowLayout을 사용
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
            JLabel locationLabel = new JLabel(photoDetail.getLocation(), SwingConstants.CENTER);
            JPanel textPanel = new JPanel(new GridLayout(3, 1));
            textPanel.add(titleLabel);
            textPanel.add(timeLabel);
            textPanel.add(locationLabel);
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
    // 검색 결과를 표시하기 위한 렌더러
    class SearchResultRenderer implements ListCellRenderer<JPanel> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value, int index, boolean isSelected, boolean cellHasFocus) {
            return value;
        }
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
