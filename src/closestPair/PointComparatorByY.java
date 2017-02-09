package closestPair;

import java.util.*;

/* Comparator by Y implementation for PointSet.sort(c). */
public class PointComparatorByY implements Comparator<Point> {

    public int compare(Point p1, Point p2) {
        return p1.compareToByY(p2);
    }

}
