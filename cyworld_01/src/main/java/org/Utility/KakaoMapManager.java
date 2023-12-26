package org.Utility;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class KakaoMapManager {
    private JFrame frame;
    private String apiKey = "f32e49f5dedd2c37722a3d4f1ada6317"; // REST API 키
    private JXMapViewer mapViewer = new JXMapViewer(); // 지도를 표시할 JXMapViewer 객체
    private DefaultListModel<String> listModel = new DefaultListModel<>(); // 검색 결과를 표시할 ListModel
    private JTextField searchField; // 검색어를 입력할 텍스트 필드
    private JList<String> resultList; // 검색 결과를 표시할 리스트
    public KakaoMapManager() {
        initializeUI();
    }

    public JSONArray searchPlace(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + encodedKeyword;

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "KakaoAK " + this.apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            return json.getJSONArray("documents");
        }
    }
    private void initializeUI() {
        frame = new JFrame("Kakao Map Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // 검색 패널 설정
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 검색 결과 리스트 설정
        resultList = new JList<>(listModel);
        JScrollPane listScroller = new JScrollPane(resultList);
        listScroller.setPreferredSize(new Dimension(250, 80));

        // 지도 설정
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(new GeoPosition(37.5665, 126.9780)); // 서울의 좌표로 초기 위치 설정

        // 검색 버튼 액션 연결
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchField.getText());
            }
        });

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroller, mapViewer), BorderLayout.CENTER);
        frame.setVisible(true);
    }
    // 위치 정보 저장

    private void performSearch(String query) {
        try {
            // KakaoMapManager를 사용하여 실제 장소를 검색
            JSONArray places = new KakaoMapManager().searchPlace(query);

            // 검색 결과 목록을 업데이트
            listModel.clear();
            List<GeoPosition> geoPositions = new ArrayList<>(); // GeoPosition 목록을 저장할 리스트
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                String placeName = place.getString("place_name");
                double lat = Double.parseDouble(place.getString("y")); // 위도
                double lng = Double.parseDouble(place.getString("x")); // 경도
                geoPositions.add(new GeoPosition(lat, lng)); // GeoPosition 리스트에 추가
                listModel.addElement(placeName + " (" + lat + ", " + lng + ")");
            }

            if (!geoPositions.isEmpty()) {
                // 지도의 위치를 첫 번째 검색 결과로 업데이트
                mapViewer.setAddressLocation(geoPositions.get(0));
            } else {
                JOptionPane.showMessageDialog(frame, "No places found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error performing search", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KakaoMapManager();
            }
        });
    }

}
