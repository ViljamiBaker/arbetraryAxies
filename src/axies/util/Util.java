package axies.util;

public class Util {
    public static int triangleNum(int i){
        int tri = 0;
        for (int j = 0; j < i; j++) {
            tri += j;
        }
        return tri;
    }
}
