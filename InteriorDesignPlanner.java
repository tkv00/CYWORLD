import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class InteriorDesignPlanner extends JFrame {

    private JPanel canvas;

    public InteriorDesignPlanner() {
        initUI();
    }

    private void initUI() {
        canvas = new JPanel() {
            // 가구와 벽 등을 그리기 위해 paintComponent를 오버라이드합니다.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawObjects(g);
            }
        };

        canvas.setPreferredSize(new Dimension(800, 600));
        canvas.setLayout(null); // 드래그 앤 드롭을 위해 null 레이아웃을 사용합니다.

        // 드래그 가능한 라벨 (가구)를 추가합니다.
        JLabel label = new JLabel(new ImageIcon("chair.png"));
        label.setBounds(20, 20, 100, 100); // 초기 위치와 크기 설정
        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // 드래그 시작 시 처리
            }
        });
        label.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                // 드래그하는 동안 위치 업데이트
                moveAt(e, label);
            }
        });

        canvas.add(label);

        add(canvas);

        pack();

        setTitle("2D Interior Design Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // 가구와 기타 객체를 그리는 메소드입니다.
    private void drawObjects(Graphics g) {
        // 예를 들어, 방의 벽을 그리는 코드를 여기에 추가할 수 있습니다.
    }

    // 드래그하는 동안 라벨의 위치를 업데이트하는 메소드입니다.
    private void moveAt(MouseEvent e, JLabel label) {
        int x = e.getX() + label.getX() - label.getWidth() / 2;
        int y = e.getY() + label.getY() - label.getHeight() / 2;
        label.setLocation(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InteriorDesignPlanner idp = new InteriorDesignPlanner();
            idp.setVisible(true);
        });
    }
}
