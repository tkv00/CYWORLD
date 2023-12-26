package org.Utility;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class KaKaoMap {
    static JXMapViewer mapViewer = new JXMapViewer();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Map Example");

        // Tile factory 설정
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // 지도 중심 설정 및 줌 레벨
        GeoPosition seoul = new GeoPosition(37.5665, 126.9780);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(seoul);

        // 검색 버튼 추가
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = JOptionPane.showInputDialog("Enter a location:");
                if (location != null && !location.isEmpty()) {
                    try {
                        JSONArray places = new KakaoMapManager().searchPlace(location);
                        if (places.length() > 0) {
                            JSONObject firstPlace = places.getJSONObject(0);
                            double lat = firstPlace.getJSONObject("y").getDouble("value");
                            double lng = firstPlace.getJSONObject("x").getDouble("value");
                            GeoPosition position = new GeoPosition(lat, lng);
                            mapViewer.setAddressLocation(position);

                        } else {
                            JOptionPane.showMessageDialog(mapViewer, "No places found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(mapViewer, "Error searching for location", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.add(searchButton, BorderLayout.NORTH);
        frame.add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

