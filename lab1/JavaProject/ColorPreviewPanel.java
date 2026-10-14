import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ColorPreviewPanel extends JPanel {
    private Color currentColor;

    public ColorPreviewPanel() {
        setPreferredSize(new Dimension(200, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        currentColor = Color.BLACK;
    }

    public void setColor(Color c) {
        currentColor = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(currentColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
