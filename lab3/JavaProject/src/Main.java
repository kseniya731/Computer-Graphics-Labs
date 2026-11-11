import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {

    private final GridPanel gridPanel = new GridPanel();
    private final ControlPanel controlPanel = new ControlPanel();

    public Main() {
        super("Базовые растровые алгоритмы");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(controlPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        controlPanel.setDrawListener(this::drawAlgorithm);
        controlPanel.addPropertyChangeListener("cellSize", evt ->
                gridPanel.setCellSize((Integer) evt.getNewValue()));
    }

    private void drawAlgorithm(String alg, int x0, int y0, int x1, int y1,
                               int cx, int cy, int r) {
        long start = System.nanoTime();
        List<Point> pts;

        switch (alg) {
            case "Пошаговый":             pts = Algorithms.stepByStep(x0, y0, x1, y1); break;
            case "ЦДА":                   pts = Algorithms.dda(x0, y0, x1, y1); break;
            case "Брезенхем (отрезок)":   pts = Algorithms.bresenhamLine(x0, y0, x1, y1); break;
            case "Брезенхем (окружность)":pts = Algorithms.bresenhamCircle(cx, cy, r); break;
            case "Кастл-Питвей":          pts = CastlePitway.line(x0, y0, x1, y1); break;
            default:                      pts = Algorithms.bresenhamLine(x0, y0, x1, y1);
        }

        long end = System.nanoTime();
        controlPanel.setTime(end - start);
        gridPanel.setPoints(pts);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new Main().setVisible(true);
        });
    }
}
