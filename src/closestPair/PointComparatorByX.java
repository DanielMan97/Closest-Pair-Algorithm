package closestPair;

import java.util.*;

/* Comparator by X implementation for PointSet.sort(c). */
public class PointComparatorByX implements Comparator<Point> {

    public int compare(Point p1, Point p2) {
        return p1.compareToByX(p2);
    }

}
