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


    public JSONArray searchPlace(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:3000/kakao-map-api?query=" + keyword;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();



        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();
            System.out.println(responseBody);

            JSONObject json = new JSONObject(responseBody);
            return json.getJSONArray("documents");
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
