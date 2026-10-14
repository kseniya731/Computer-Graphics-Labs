public class ColorConverter {
    private static final float EPS = 1e-6f;

    private static boolean approxEquals(float a, float b) {
        return Math.abs(a - b) < EPS;
    }

    public static float[] rgbToCmyk(int r, int g, int b) {
        float c = 1 - r/255f;
        float m = 1 - g/255f;
        float y = 1 - b/255f;
        float k = Math.min(c, Math.min(m, y));
        if(k < 1f - EPS) {
            c = (c - k) / (1 - k);
            m = (m - k) / (1 - k);
            y = (y - k) / (1 - k);
        } else {
            c = 0; m = 0; y = 0;
            k = 1f;
        }
        return new float[]{c, m, y, k};
    }

    public static int[] cmykToRgb(float c, float m, float y, float k) {
        int r = Math.round(255 * (1 - c) * (1 - k));
        int g = Math.round(255 * (1 - m) * (1 - k));
        int b = Math.round(255 * (1 - y) * (1 - k));
        return new int[]{r, g, b};
    }

    public static float[] rgbToHsv(int r, int g, int b) {
        float rf = r/255f; float gf = g/255f; float bf = b/255f;
        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float delta = max - min;
        float v = max;
        float s = approxEquals(max, 0f) ? 0f : delta / max;
        float h;
        if(approxEquals(delta, 0f)) h = 0f;
        else if(max == rf) h = (60f * ((gf - bf) / delta) + 360f) % 360f;
        else if(max == gf) h = (60f * ((bf - rf) / delta) + 120f) % 360f;
        else h = (60f * ((rf - gf) / delta) + 240f) % 360f;
        return new float[]{h, s, v};
    }

    public static int[] hsvToRgb(float h, float s, float v) {
        h = (h % 360 + 360) % 360;
        float c = v * s;
        float x = c * (1 - Math.abs((h/60f) % 2f - 1));
        float m = v - c;
        float r; float g; float b;
        if(h < 60) { r = c; g = x; b = 0; }
        else if(h < 120) { r = x; g = c; b = 0; }
        else if(h < 180) { r = 0; g = c; b = x; }
        else if(h < 240) { r = 0; g = x; b = c; }
        else if(h < 300) { r = x; g = 0; b = c; }
        else { r = c; g = 0; b = x; }
        return new int[]{
            Math.round((r + m) * 255),
            Math.round((g + m) * 255),
            Math.round((b + m) * 255)
        };
    }
}