package org.example;

import org.Utility.DatabaseConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardList {
    private JFrame boardFrame;
    private UneditableTableModel tableModel;  // 수정 불가능한 모델로 변경
    private JTable boardTable;  // JTable 추가
    private JScrollPane scrollPane;
    private WriteBoardManager writeBoardManager;  // WriteBoardManager 추가

    public BoardList() {
        boardFrame = new JFrame("게시판 목록");
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.setSize(800, 600);
        boardFrame.setLayout(new BorderLayout());
        JButton createPostButton = new JButton("게시글 작성");
        writeBoardManager = new WriteBoardManager();  // WriteBoardManager 초기화

        writeBoardManager.setPostClickListener(postId -> showPostDetailsInNewWindow(postId));

        createPostButton.addActionListener(e -> new WriteBoard());

        JPanel topPanel = new JPanel(); // 패널 생성
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 패널에 레이아웃 설정
        topPanel.add(createPostButton); // 패널에 버튼 추가

        boardFrame.add(topPanel, BorderLayout.NORTH); // 프레임에 패널 추가

        // 수정 불가능한 모델로 JTable 초기화
        tableModel = new UneditableTableModel();
        tableModel.addColumn("No.");
        tableModel.addColumn("Title");
        tableModel.addColumn("NAME");
        tableModel.addColumn("DATE");

        boardTable = new JTable(tableModel);
        boardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // JScrollPane에 JTable을 추가
        scrollPane = new JScrollPane(boardTable);
        boardFrame.add(scrollPane, BorderLayout.CENTER);

        fetchBoardData();

        boardFrame.setVisible(true);

        // 텍스트 가운데 정렬을 위한 DefaultTableCellRenderer 설정
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < boardTable.getColumnCount(); i++) {
            boardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void fetchBoardData() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT WriteBoardNum, userId, time, title FROM WriteBoard";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // 기존 데이터 삭제
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                int writeBoardNum = resultSet.getInt("WriteBoardNum");
                String title = resultSet.getString("title");
                String userId = resultSet.getString("userId");
                String time = resultSet.getString("time").substring(0, 10);

                Vector<Object> row = new Vector<>();
                row.add(writeBoardNum);
                row.add(title);
                row.add(userId);
                row.add(time);

                // 새로운 행 추가
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPostDetailsInNewWindow(int postId) {
        // 여기에 showPostDetailsInNewWindow 메서드의 구현을 추가
    }

    private JPanel createSectionPanel(String label, String value, int postId) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel labelComponent = new JLabel(label);
        JLabel valueComponent = new JLabel(value);

        panel.add(labelComponent);
        panel.add(valueComponent);

        // 제목 패널에 MouseListener 추가
        if (label.equals("제목: ")) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPostDetailsInNewWindow(postId);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BoardList());
    }
}

class WriteBoardManager {

    public interface PostClickListener {
        void onPostClick(int postId);
    }

    private PostClickListener postClickListener;

    public void setPostClickListener(PostClickListener postClickListener) {
        this.postClickListener = postClickListener;
    }

    // WriteBoardManager에서 수행할 다양한 기능을 추가할 수 있습니다.

    // 예: 게시글 작성 기능
    public void writePost(String userId, String title, String content) {
        // 여기에 게시글 작성에 관한 로직을 추가하세요.
        // 예를 들어, 데이터베이스에 새로운 게시글을 추가하거나 필요한 동작을 수행할 수 있습니다.
    }

    // 예: 게시글 삭제 기능
    public void deletePost(int postId) {
        // 여기에 게시글 삭제에 관한 로직을 추가하세요.
        // 예를 들어, 데이터베이스에서 해당 게시글을 삭제하거나 필요한 동작을 수행할 수 있습니다.
    }
}


// 수정 불가능한 모델로 확장한 클래스
class UneditableTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
        // 모든 열에 대해 수정 불가능하게 만듦
        return false;
    }
}