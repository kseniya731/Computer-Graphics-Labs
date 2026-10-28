import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private String title;

    public ImagePanel(String title) {
        this.title = title;
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString(title, 10, 20);

        if (image == null) return;

        int availW = getWidth() - 20;
        int availH = getHeight() - 40;
        if (availW <= 0 || availH <= 0) return;

        double scale = Math.min(
                (double) availW / image.getWidth(),
                (double) availH / image.getHeight()
        );

        int drawW = (int) (image.getWidth() * scale);
        int drawH = (int) (image.getHeight() * scale);

        int x = (getWidth() - drawW) / 2;
        int y = 30 + (availH - drawH) / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(image, x, y, drawW, drawH, null);
        g2.dispose();
    }
}
