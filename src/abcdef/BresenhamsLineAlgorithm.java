package abcdef;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 13/12/13
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class BresenhamsLineAlgorithm {


    public static List<Point> getPoints(int x0, int y0, int x1, int y1) {
        int ox = x0;
        int oy = y0;
        List<Point> points = new ArrayList<Point>();
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            //x0 <-> y0
            int t = x0;
            x0 = y0;
            y0 = t;
            //x1 <-> y1
            t = x1;
            x1 = y1;
            y1 = t;
        }
        if (x0 > x1) {
            //x0 <-> x1
            int t = x0;
            x0 = x1;
            x1 = t;
            //y0 <-> y1
            t = y0;
            y0 = y1;
            y1 = t;
        }

        int deltax = Math.abs(x1 - x0);
        int deltay = Math.abs(y1 - y0);
        int error = 0;
        int ystep;
        int y = y0;
        if (y0 < y1) ystep = 1; else ystep = -1;
        for (int x = x0; x <= x1; x++) {
            if (steep) points.add(new Point(y, x));
            else points.add(new Point(x, y));
            error += deltay;
            if (2 * error >= deltax) {
                y += ystep;
                error -= deltax;
            }
        }

        //Need to reorder points
        if (ox != points.get(0).x || oy != points.get(0).y)
            points = reverse(points);


        return points;
    }

    private static List<Point> reverse(List<Point> points) {
        List<Point> n = new ArrayList<Point>();
        for (int i = points.size() - 1; i >= 0; i--)
            n.add(points.get(i));
        return n;
    }
}
