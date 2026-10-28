import java.awt.image.BufferedImage;

public class Sharpen {

    private static final int[][] KERNEL = {
            { 0, -1,  0 },
            {-1,  5, -1 },
            { 0, -1,  0 }
    };

    public static BufferedImage apply(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int sumR = 0, sumG = 0, sumB = 0;

                // свртка 3x3
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int px = x + kx;
                        int py = y + ky;

                        // если вышли за пределы — берем ближайший пиксель
                        px = Math.min(w - 1, Math.max(0, px));
                        py = Math.min(h - 1, Math.max(0, py));

                        int rgb = src.getRGB(px, py);
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8)  & 0xFF;
                        int b =  rgb        & 0xFF;

                        int k = KERNEL[ky + 1][kx + 1];
                        sumR += r * k;
                        sumG += g * k;
                        sumB += b * k;
                    }
                }

                // ограничиваем значения диапазоном [0, 255]
                sumR = clamp(sumR);
                sumG = clamp(sumG);
                sumB = clamp(sumB);

                out.setRGB(x, y, (sumR << 16) | (sumG << 8) | sumB);
            }
        }
        return out;
    }

    private static int clamp(int v) {
        return Math.min(255, Math.max(0, v));
    }
}
