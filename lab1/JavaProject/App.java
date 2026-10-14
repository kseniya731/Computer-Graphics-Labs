import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;

public class App extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App("Color models (RGB-CMYK-HSV)");
            app.setVisible(true);
        });
    }

    private int r = 0, g = 0, b = 0; // начальное состояние
    private boolean updating = false;

    private ColorModelPanel rgbPanel;
    private ColorModelPanel cmykPanel;
    private ColorModelPanel hsvPanel;
    private ColorPreviewPanel previewPanel;
    private JButton pickColorButton;

    public App(String title) {
        super(title);
        initializeUI();
        wireListeners();
        refreshAll(""); // начальная синхронизация
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        rgbPanel = new ColorModelPanel("RGB",
            new String[]{"R", "G", "B"},
            new int[]{0, 0, 0},
            new int[]{255, 255, 255});

        cmykPanel = new ColorModelPanel("CMYK",
            new String[]{"C", "M", "Y", "K"},
            new int[]{0, 0, 0, 0},
            new int[]{100, 100, 100, 100});

        hsvPanel = new ColorModelPanel("HSV",
            new String[]{"H", "S", "V"},
            new int[]{0, 0, 0},
            new int[]{360, 100, 100});

        previewPanel = new ColorPreviewPanel();
        pickColorButton = new JButton("Choose color from palette");
    
        setLayout(new BorderLayout(10, 10));

        // верх: кнопка палитры
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(pickColorButton);
        add(topPanel, BorderLayout.NORTH);

        // центр: три панели моделей в ряд
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        centerPanel.add(rgbPanel);
        centerPanel.add(cmykPanel);
        centerPanel.add(hsvPanel);
        add(centerPanel, BorderLayout.CENTER);

        // низ: превью
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(new JLabel("Color preview:"));
        bottomPanel.add(previewPanel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void wireListeners() {
        rgbPanel.setOnChange(this::onRgbChanged);
        cmykPanel.setOnChange(this::onCmykChanged);
        hsvPanel.setOnChange(this::onHsvChanged);
        pickColorButton.addActionListener(e -> onPickColor());
    }

    private void onRgbChanged() {
        if (updating) return;
        int[] vals = rgbPanel.getValues();
        r = vals[0]; g = vals[1]; b = vals[2];
        refreshAll("RGB");
    }

    private void onCmykChanged() {
        if (updating) return;
        int[] vals = cmykPanel.getValues();
        int[] rgb = ColorConverter.cmykToRgb(
            vals[0] / 100f, vals[1] / 100f, vals[2] / 100f, vals[3] / 100f);
        r = rgb[0]; g = rgb[1]; b = rgb[2];
        refreshAll("CMYK");
    }

    private void onHsvChanged() {
        if (updating) return;
        int[] vals = hsvPanel.getValues();
        int[] rgb = ColorConverter.hsvToRgb(
            vals[0], vals[1] / 100f, vals[2] / 100f);
        r = rgb[0]; g = rgb[1]; b = rgb[2];
        refreshAll("HSV");
    }

    private void onPickColor() {
        Color chosen = JColorChooser.showDialog(
            this, "Choose color", new Color(r, g, b));
        if (chosen != null) {
            r = chosen.getRed();
            g = chosen.getGreen();
            b = chosen.getBlue();
            refreshAll("");
        }
    }

    private void refreshAll(String source) {
        updating = true;
        try {
            if (!source.equals("RGB")) {
                rgbPanel.setValues(new int[]{r, g, b});
            }

            if (!source.equals("CMYK")) {
                float[] cmyk = ColorConverter.rgbToCmyk(r, g, b); // c/m/y/k 0..1
                cmykPanel.setValues(new int[]{
                    Math.round(cmyk[0] * 100),
                    Math.round(cmyk[1] * 100),
                    Math.round(cmyk[2] * 100),
                    Math.round(cmyk[3] * 100)
                });
            }

            if (!source.equals("HSV")) {
                float[] hsv = ColorConverter.rgbToHsv(r, g, b); // h 0..360, s/v 0..1
                hsvPanel.setValues(new int[]{
                    Math.round(hsv[0]),
                    Math.round(hsv[1] * 100),
                    Math.round(hsv[2] * 100)
                });
            }

            previewPanel.setColor(new Color(r, g, b));
        } finally {
            updating = false;
        }
    }
}
