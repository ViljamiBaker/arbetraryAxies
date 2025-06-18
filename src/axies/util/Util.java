package axies.util;

import axies.objects.Point;
import axies.objects.World;

public class Util {
    public static int triangleNum(int i){
        int tri = 0;
        for (int j = 0; j < i; j++) {
            tri += j;
        }
        return tri;
    }

    public static Point lerp(Point p1, Point p2, double t){
        Point newPoint = new Point();
        for (int i = 0; i < World.axisCount; i++) {
            newPoint.setAxis(i, p1.getAxis(i)*(1.0-t) + p2.getAxis(i)*t);
        }
        return newPoint;
    }

    public static boolean within(double p11, double p12, double p21, double p22){
        return (Math.min(p11, p12)<=Math.max(p21, p22)&&
                Math.min(p21, p22)<=Math.max(p11, p12));
    }

    public static boolean within(double p11, double p12, double p2){
        return (Math.min(p11,p12)<=p2&&Math.max(p11,p12)>=p2);
    }

    public static boolean xor(boolean a, boolean b){
        return (a||b)&&!(a&&b);
    }

    public static boolean arrayContains(Object[] o, Object c){
        for (int i = 0; i < o.length; i++) {
            if (o[i].equals(c)) {
                return true;
            }
        }
        return false;
    }
}
