import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLabApp extends JFrame {
    private BufferedImage originalImage;
    private ImagePanel originalPanel;
    private ImagePanel resultPanel;
    private JComboBox<String> methodBox;
    private JLabel statusLabel;

    // для названия файла
    private String currentBaseName = "image"; // текущий файл
    private String lastOperationSuffix = "result"; // последняя операция

    public ImageLabApp() {
        setTitle("Обработка изображений");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton loadBtn    = new JButton("Загрузить изображение");
        JButton processBtn = new JButton("Обработать");
        JButton saveBtn    = new JButton("Сохранить результат");
        methodBox = new JComboBox<>(new String[]{
                "Метод Оцу",
                "Метод среднего",
                "Повышение резкости"
        });
        topPanel.add(loadBtn);
        topPanel.add(methodBox);
        topPanel.add(processBtn);
        topPanel.add(saveBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        originalPanel = new ImagePanel("Оригинал");
        resultPanel   = new ImagePanel("Результат");
        imagePanel.add(originalPanel);
        imagePanel.add(resultPanel);
        add(imagePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Ожидание: загрузите изображение");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.add(statusLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> loadImage());
        processBtn.addActionListener(e -> processImage());
        saveBtn.addActionListener(e -> saveResult());

        setLocationRelativeTo(null);
    }

    private void loadImage() {
        JFileChooser chooser = new JFileChooser("images");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                originalImage = ImageIO.read(file);
                originalPanel.setImage(originalImage);
                resultPanel.setImage(null);
                statusLabel.setText("Изображение загружено. Выберите метод и нажмите «Обработать»");

                // извлекаем имя файла без расширения
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    currentBaseName = fileName.substring(0, dotIndex);
                } else {
                    currentBaseName = fileName;
                }
                lastOperationSuffix = "result";

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }

    private void processImage() {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "Сначала загрузите изображение!");
            return;
        }

        String method = (String) methodBox.getSelectedItem();
        BufferedImage result;

        if ("Метод Оцу".equals(method)) {
            int threshold = Thresholding.otsuThreshold(originalImage);
            result = Thresholding.applyThreshold(originalImage, threshold);
            statusLabel.setText("Метод Оцу, порог = " + threshold);
            lastOperationSuffix = "otsu_" + threshold;

        } else if ("Метод среднего".equals(method)) {
            int threshold = Thresholding.meanThreshold(originalImage);
            result = Thresholding.applyThreshold(originalImage, threshold);
            statusLabel.setText("Метод среднего, порог = " + threshold);
            lastOperationSuffix = "mean_" + threshold;

        } else { // повышение резкости
            result = Sharpen.apply(originalImage);
            statusLabel.setText("Повышение резкости (ядро 3×3)");
            lastOperationSuffix = "sharpen_3x3";
        }

        resultPanel.setImage(result);
    }

    private void saveResult() {
        BufferedImage result = resultPanel.getImage();
        if (result == null) {
            JOptionPane.showMessageDialog(this, "Сначала обработайте изображение!");
            return;
        }

        // предлагаем имя файла: оригинал_метод_параметр.png
        String suggestedName = currentBaseName + "_" + lastOperationSuffix + ".png";

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(suggestedName)); // подставляем имя в диалог

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(result, "png", chooser.getSelectedFile());
                statusLabel.setText("Сохранено: " + chooser.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageLabApp().setVisible(true));
    }
}
