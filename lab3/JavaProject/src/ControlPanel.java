import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    public interface DrawListener {
        void onDraw(String algorithm, int x0, int y0, int x1, int y1,
                    int cx, int cy, int r);
    }

    private final JComboBox<String> algorithmBox;
    private final JTextField x0Field = new JTextField("0", 4);
    private final JTextField y0Field = new JTextField("0", 4);
    private final JTextField x1Field = new JTextField("8", 4);
    private final JTextField y1Field = new JTextField("5", 4);
    private final JTextField cxField = new JTextField("0", 4);
    private final JTextField cyField = new JTextField("0", 4);
    private final JTextField rField  = new JTextField("5", 4);
    private final JSpinner scaleSpinner = new JSpinner(new SpinnerNumberModel(25, 5, 60, 1));
    private final JLabel timeLabel = new JLabel("Время: —");

    private final JPanel inputs;
    private DrawListener listener;

    public ControlPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        algorithmBox = new JComboBox<>(new String[]{
                "Пошаговый",
                "ЦДА",
                "Брезенхем (отрезок)",
                "Брезенхем (окружность)",
                "Кастл-Питвей"
        });

        JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linePanel.add(new JLabel("x0:")); linePanel.add(x0Field);
        linePanel.add(new JLabel("y0:")); linePanel.add(y0Field);
        linePanel.add(new JLabel("x1:")); linePanel.add(x1Field);
        linePanel.add(new JLabel("y1:")); linePanel.add(y1Field);

        JPanel circlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        circlePanel.add(new JLabel("cx:")); circlePanel.add(cxField);
        circlePanel.add(new JLabel("cy:")); circlePanel.add(cyField);
        circlePanel.add(new JLabel("R:"));  circlePanel.add(rField);

        inputs = new JPanel(new CardLayout());
        inputs.add(linePanel, "line");
        inputs.add(circlePanel, "circle");

        algorithmBox.addActionListener(e -> {
            CardLayout cl = (CardLayout) inputs.getLayout();
            String sel = (String) algorithmBox.getSelectedItem();
            cl.show(inputs, "Брезенхем (окружность)".equals(sel) ? "circle" : "line");
        });

        JButton drawBtn = new JButton("Нарисовать");
        drawBtn.addActionListener(e -> fireDraw());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.add(new JLabel("Масштаб (px):"));
        right.add(scaleSpinner);
        right.add(drawBtn);

        JPanel top = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        left.add(new JLabel("Алгоритм:"));
        left.add(algorithmBox);
        left.add(inputs);
        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(timeLabel, BorderLayout.SOUTH);

        scaleSpinner.addChangeListener(e ->
                firePropertyChange("cellSize", 0, ((Integer) scaleSpinner.getValue()).intValue()));
    }

    public void setDrawListener(DrawListener listener) {
        this.listener = listener;
    }

    public void setTime(long nanos) {
        timeLabel.setText("Время: " + nanos + " нс");
    }

    private void fireDraw() {
        if (listener == null) return;
        try {
            String alg = (String) algorithmBox.getSelectedItem();
            int x0 = Integer.parseInt(x0Field.getText().trim());
            int y0 = Integer.parseInt(y0Field.getText().trim());
            int x1 = Integer.parseInt(x1Field.getText().trim());
            int y1 = Integer.parseInt(y1Field.getText().trim());
            int cx = Integer.parseInt(cxField.getText().trim());
            int cy = Integer.parseInt(cyField.getText().trim());
            int r  = Integer.parseInt(rField.getText().trim());
            listener.onDraw(alg, x0, y0, x1, y1, cx, cy, r);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Введите целые числа во все поля.",
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }
}
