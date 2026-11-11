import java.util.ArrayList;
import java.util.List;

public class CastlePitway {

    public static List<Point> line(int x0, int y0, int x1, int y1) {
        List<Point> pts = new ArrayList<>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        boolean swap = dy > dx;
        int a = swap ? dy : dx;
        int b = swap ? dx : dy;

        String code = (b == 0) ? repeat("S", a) : buildCode(a, b);

        int cx = x0, cy = y0;
        pts.add(new Point(cx, cy));
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == 'D') {
                cx += sx; cy += sy;
            } else {
                if (swap) cy += sy;
                else      cx += sx;
            }
            pts.add(new Point(cx, cy));
        }
        return pts;
    }

    static String buildCode(int a, int b) {
        if (b == 0) return repeat("S", a);
        String m1 = "S", m2 = "D";
        int x = a - b, y = b;
        while (x != y) {
            if (x > y) { m2 = m1 + reverse(m2); x -= y; }
            else       { m1 = m2 + reverse(m1); y -= x; }
        }
        String unit = m2 + reverse(m1);
        return repeat(unit, x);
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    private static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}