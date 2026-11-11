import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridPanel extends JPanel {

    private List<Point> points = null;
    private int cellSize = 25;

    public GridPanel() {
        setBackground(Color.WHITE);
    }

    public void setPoints(List<Point> points) {
        this.points = points;
        repaint();
    }

    public void setCellSize(int cellSize) {
        this.cellSize = Math.max(5, cellSize);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();

        int originX = w / 2;
        int originY = h / 2;

        drawGrid(g2, w, h, originX, originY);
        drawAxes(g2, w, h, originX, originY);

        if (points != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setColor(new Color(200, 30, 30));
            for (Point p : points) {
                int px = originX + p.x * cellSize;
                int py = originY - p.y * cellSize - cellSize;
                g2.fillRect(px, py, cellSize, cellSize);
            }
        }
    }

    private void drawGrid(Graphics2D g2, int w, int h, int ox, int oy) {
        g2.setColor(new Color(225, 225, 225));

        for (int x = ox; x < w; x += cellSize) g2.drawLine(x, 0, x, h);
        for (int x = ox; x > 0; x -= cellSize) g2.drawLine(x, 0, x, h);

        for (int y = oy; y < h; y += cellSize) g2.drawLine(0, y, w, y);
        for (int y = oy; y > 0; y -= cellSize) g2.drawLine(0, y, w, y);
    }

    private void drawAxes(Graphics2D g2, int w, int h, int ox, int oy) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(0, oy, w, oy);   // ось X через весь экран
        g2.drawLine(ox, 0, ox, h);   // ось Y через весь экран

        g2.drawLine(w - 5, oy, w - 15, oy - 5);
        g2.drawLine(w - 5, oy, w - 15, oy + 5);
        g2.drawLine(ox, 5, ox - 5, 15);
        g2.drawLine(ox, 5, ox + 5, 15);

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("X", w - 20, oy - 8);
        g2.drawString("Y", ox + 8, 18);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setColor(Color.DARK_GRAY);

        // по X (положительные и отрицательные)
        for (int i = 5; ox + i * cellSize < w - 5; i += 5) {
            int x = ox + i * cellSize;
            g2.drawLine(x, oy - 3, x, oy + 3);
            g2.drawString(String.valueOf(i), x - 5, oy + 16);
        }
        for (int i = -5; ox + i * cellSize > 5; i -= 5) {
            int x = ox + i * cellSize;
            g2.drawLine(x, oy - 3, x, oy + 3);
            g2.drawString(String.valueOf(i), x - 8, oy + 16);
        }

        // по Y (положительные и отрицательные)
        for (int i = 5; oy - i * cellSize > 5; i += 5) {
            int y = oy - i * cellSize;
            g2.drawLine(ox - 3, y, ox + 3, y);
            g2.drawString(String.valueOf(i), ox + 8, y + 4);
        }
        for (int i = -5; oy - i * cellSize < h - 5; i -= 5) {
            int y = oy - i * cellSize;
            g2.drawLine(ox - 3, y, ox + 3, y);
            g2.drawString(String.valueOf(i), ox + 8, y + 4);
        }

        g2.setColor(Color.BLACK);
        g2.drawString("0", ox + 5, oy + 15);
    }
}