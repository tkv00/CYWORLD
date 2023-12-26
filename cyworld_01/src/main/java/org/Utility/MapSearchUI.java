package org.Utility;

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
import java.util.List;
import java.util.ArrayList;

public class MapSearchUI {
    private JFrame frame;
    private JXMapViewer mapViewer;
    private JTextField searchField;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;

    public MapSearchUI() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Map Search Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchField.getText());
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // 지도 설정
        mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(new GeoPosition(37.5665, 126.9780));

        // 검색 결과 목록
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        JScrollPane listScroller = new JScrollPane(resultList);
        listScroller.setPreferredSize(new Dimension(200, 0));

        // 컴포넌트 추가
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(mapViewer, BorderLayout.CENTER);
        frame.add(listScroller, BorderLayout.EAST);

        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void performSearch(String query) {
        // 검색을 수행하고 결과를 지도와 목록에 업데이트하는 로직
        try {
            // 가짜 검색 결과 생성 (실제로는 KakaoMapManager의 searchPlace 메서드를 사용)
            List<String> places = new ArrayList<>();
            places.add("Place 1");
            places.add("Place 2");
            places.add("Place 3");

            // 검색 결과 목록을 업데이트
            listModel.clear();
            for (String place : places) {
                listModel.addElement(place);
            }

            // 지도의 위치를 첫 번째 검색 결과로 업데이트 (여기서는 임의의 위치를 설정)
            GeoPosition newPosition = new GeoPosition(37.5665, 126.9780);
            mapViewer.setAddressLocation(newPosition);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error performing search", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MapSearchUI();
            }
        });
    }
}
