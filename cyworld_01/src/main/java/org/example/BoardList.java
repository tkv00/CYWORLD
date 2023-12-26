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
import javax.swing.table.TableRowSorter;
import java.util.Comparator;
import java.lang.Integer;

public class BoardList {
    private JFrame boardFrame;
    private UneditableTableModel tableModel;  // 수정 불가능한 모델로 변경
    private JTable boardTable;  // JTable 추가
    private JScrollPane scrollPane;
    private WriteBoardManager writeBoardManager;  // WriteBoardManager 추가



        public BoardList() {
        boardFrame = new JFrame("싸이월드 - 게시판 목록");
        boardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardFrame.setSize(800, 600);
        boardFrame.setLayout(new BorderLayout());
        JButton createPostButton = new JButton("글쓰기");
        createPostButton.setBackground(new Color(0, 122, 255)); // 파란색 배경
        createPostButton.setForeground(Color.WHITE); // 흰색 텍스트
        createPostButton.setFocusPainted(false); // 클릭 시 테두리 제거

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
        boardTable = new JTable(tableModel) {
                // Drag-and-Drop을 비활성
                protected TransferHandler createTransferHandler() {
                return null;
            }
        };

            boardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // JTable 스타일 설정
        boardTable.setBackground(new Color(255,255,255)); // 연한 회색 배경
        boardTable.setSelectionBackground(new Color(240,240,240)); // 파란색 선택 배경
        boardTable.setSelectionForeground(Color.black); // 검은색 선택 텍스트

        // TableRowSorter를 사용하여 JTable을 오름차순으로 정렬
        TableRowSorter<UneditableTableModel> sorter = new TableRowSorter<>(tableModel);
        boardTable.setRowSorter(sorter);

        // 기본적으로 0번 열(No.)을 기준으로 오름차순 정렬
        sorter.setComparator(0, Comparator.comparingInt(row -> Integer.parseInt(row.toString())));
        sorter.toggleSortOrder(0);

        // JScrollPane에 JTable을 추가
        scrollPane = new JScrollPane(boardTable);

        // 열 헤더를 비활성화하여 열을 움직이지 못하게 함
        scrollPane.setColumnHeaderView(null);

        boardFrame.add(scrollPane, BorderLayout.CENTER);

        fetchBoardData();

        boardFrame.setVisible(true);

        // 파란색 텍스트를 위한 DefaultTableCellRenderer 설정
        DefaultTableCellRenderer blueTextRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Title 열의 텍스트 색상을 파란색(RGB: 0, 0, 205)으로 설정
                setForeground(new Color(0, 122, 255));

                return component;
            }
        };

        // 검은색 텍스트를 위한 DefaultTableCellRenderer 설정
        DefaultTableCellRenderer blackTextRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // No.와 Date 열의 텍스트 색상을 검은색으로 설정
                setForeground(Color.BLACK);

                return component;
            }
        };

        boardTable.getColumnModel().getColumn(1).setCellRenderer(blueTextRenderer);
        // NAME 열에 파란색 텍스트 적용
        boardTable.getColumnModel().getColumn(2).setCellRenderer(blueTextRenderer);
        // No. 열에 검은색 텍스트 적용
        boardTable.getColumnModel().getColumn(0).setCellRenderer(blackTextRenderer);
        // DATE 열에 검은색 텍스트 적용
        boardTable.getColumnModel().getColumn(3).setCellRenderer(blackTextRenderer);

        // JTable에서 제목 열을 클릭할 때의 동작을 처리하는 MouseListener 추가

            // JTable에서 제목 열을 클릭할 때의 동작을 처리하는 MouseListener 추가
            boardTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = boardTable.getSelectedRow();
                    int selectedColumn = boardTable.getSelectedColumn();

                    if (selectedColumn == 1 && selectedRow != -1) {
                        // 클릭한 Title 셀의 텍스트 색상을 변경
                        blueTextRenderer.setForeground(new Color(0, 0, 0));

                        // 테이블 갱신을 위해 repaint() 호출
                        boardTable.repaint();

                        int postId = (int) boardTable.getValueAt(selectedRow, 0);
                        showPostDetailsInNewWindow(postId);
                }
            }
        });
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
            JFrame detailsFrame = new JFrame("Cyworld - 게시글 세부 정보");
            detailsFrame.setSize(800, 600);

            try (Connection connection = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM WriteBoard WHERE WriteBoardNum= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String postTitle = resultSet.getString("title");
                String userId = resultSet.getString("userId");
                String postDate = resultSet.getString("time");
                String postContent = resultSet.getString("content");

                // 패널 생성
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());

                // 상단 패널 (제목, 작성자, 작성 날짜)
                JPanel headerPanel = new JPanel();
                headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

                // 변경된 부분: 제목을 볼드체로 설정
                JPanel titlePanel = createStyledSectionPanel("제목", postTitle, false, false, false);

                headerPanel.add(titlePanel);

                // 중복 텍스트 패널 생성 여부를 확인하기 위한 변수
                boolean authorDatePanelCreated = false;

                // 작성자 및 작성 날짜 패널
                JPanel authorDatePanel = new JPanel();
                authorDatePanel.setLayout(new BoxLayout(authorDatePanel, BoxLayout.X_AXIS));

                // 중복 생성 방지
                if (!authorDatePanelCreated) {
                    // 작성자 패널 (변경된 부분: 작성자 텍스트를 파란색으로, 제목과 같은 높이에 배치)
                    JPanel authorPanel = createStyledSectionPanel("작성자", userId, false, true, false);

                    // 작성 날짜 패널 (변경된 부분: 날짜 텍스트를 오른쪽에 배치)
                    JPanel datePanel = createStyledSectionPanel("작성 날짜", postDate, false, false, true);

                    authorDatePanel.add(authorPanel);
                    authorDatePanel.add(Box.createHorizontalGlue()); // 작성자와 날짜 간 여백
                    authorDatePanel.add(datePanel);

                    headerPanel.add(authorDatePanel);
                    authorDatePanelCreated = true;
                }

                mainPanel.add(headerPanel, BorderLayout.NORTH);

                // 중앙 패널 (작성 내용)
                JPanel contentPanel = createStyledSectionPanel("작성 내용", postContent, false, false, false);
                contentPanel.setPreferredSize(new Dimension(600, 400)); // 크기 조절
                mainPanel.add(contentPanel, BorderLayout.CENTER);

                detailsFrame.add(mainPanel);
                detailsFrame.setVisible(true);
            }
            } catch (SQLException e) {
                e.printStackTrace();
                JPanel errorPanel = createStyledSectionPanel("에러", "게시글 세부 정보를 불러오는 데 실패했습니다.", false, false, false);
                detailsFrame.add(errorPanel);
                detailsFrame.setVisible(true);
            }
        }





    private JPanel createStyledSectionPanel(String label, String value, boolean isBold, boolean isBlueText, boolean alignRight) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), label),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel labelComponent = new JLabel(label);

        // 제목을 볼드체로 설정 (변경된 부분)
        if (isBold) {
            labelComponent.setFont(new Font("Arial", Font.BOLD, 16));
        }

        JTextArea valueComponent = new JTextArea(value);
        valueComponent.setEditable(false);
        valueComponent.setLineWrap(true);
        valueComponent.setWrapStyleWord(true);

        // 파란색 텍스트 설정 (변경된 부분)
        if (isBlueText) {
            valueComponent.setForeground(new Color(0, 122, 255));
        }

        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);

        // 날짜를 오른쪽에 배치 (변경된 부분)
        if (alignRight) {
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.add(new JLabel(value)); // 날짜를 레이블로 추가
            panel.add(rightPanel, BorderLayout.EAST);
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

    // 각 열에 대한 올바른 셀 렌더러를 사용하기 위해 이 메서드를 오버라이드
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
            // 각 열에 대한 올바른 클래스를 반환
            return getValueAt(0, columnIndex).getClass();
        } else {
            // 기본적으로 Object 클래스 반환
            return Object.class;
        }
    }
}
