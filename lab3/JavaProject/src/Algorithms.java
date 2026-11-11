import java.util.ArrayList;
import java.util.List;

public class Algorithms {

    // Пошаговый
    public static List<Point> stepByStep(int x0, int y0, int x1, int y1) {
        List<Point> pts = new ArrayList<>();
        int dx = x1 - x0, dy = y1 - y0;
        if (dx == 0 && dy == 0) { pts.add(new Point(x0, y0)); return pts; }

        if (Math.abs(dx) >= Math.abs(dy)) {
            int step = Integer.signum(dx);
            for (int x = x0; x != x1 + step; x += step) {
                int y = (int) Math.round(y0 + (double) (x - x0) * dy / dx);
                pts.add(new Point(x, y));
            }
        } else {
            int step = Integer.signum(dy);
            for (int y = y0; y != y1 + step; y += step) {
                int x = (int) Math.round(x0 + (double) (y - y0) * dx / dy);
                pts.add(new Point(x, y));
            }
        }
        return pts;
    }

    // ЦДА
    public static List<Point> dda(int x0, int y0, int x1, int y1) {
        List<Point> pts = new ArrayList<>();
        int dx = x1 - x0, dy = y1 - y0;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        if (steps == 0) { pts.add(new Point(x0, y0)); return pts; }

        double xInc = (double) dx / steps;
        double yInc = (double) dy / steps;
        double x = x0, y = y0;
        for (int i = 0; i <= steps; i++) {
            pts.add(new Point((int) Math.round(x), (int) Math.round(y)));
            x += xInc;
            y += yInc;
        }
        return pts;
    }

    // Брезенхем (отрезок)
    public static List<Point> bresenhamLine(int x0, int y0, int x1, int y1) {
        List<Point> pts = new ArrayList<>();
        int dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        while (true) {
            pts.add(new Point(x0, y0));
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 < dx)  { err += dx; y0 += sy; }
        }
        return pts;
    }

    // Брезенхем (окружность)
    public static List<Point> bresenhamCircle(int cx, int cy, int r) {
        List<Point> pts = new ArrayList<>();
        int x = 0, y = r;
        int d = 3 - 2 * r;
        while (x <= y) {
            add8(pts, cx, cy, x, y);
            if (d < 0) {
                d = d + 4 * x + 6;
            } else {
                d = d + 4 * (x - y) + 10;
                y--;
            }
            x++;
        }
        return pts;
    }

    private static void add8(List<Point> pts, int cx, int cy, int x, int y) {
        pts.add(new Point(cx + x, cy + y));
        pts.add(new Point(cx - x, cy + y));
        pts.add(new Point(cx + x, cy - y));
        pts.add(new Point(cx - x, cy - y));
        pts.add(new Point(cx + y, cy + x));
        pts.add(new Point(cx - y, cy + x));
        pts.add(new Point(cx + y, cy - x));
        pts.add(new Point(cx - y, cy - x));
    }
}
