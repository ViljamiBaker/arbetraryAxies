package axies.objects;

public class Cube {
    public static int[][] vertexes;
    public static int[][] edges;

    public static void calcCube(){
        int[][] binary = new int[(int)Math.pow(2,World.axisCount)][World.axisCount];
        for (int i = 0; i < binary[0].length; i++) {
            for (int n = 0; n < binary.length; n++) {
                binary[n][i] = (int)(n/(Math.pow(2,World.axisCount-1-i)))%2;
            }
        }
        vertexes = binary;

        int[][] newEdges = new int[World.axisCount*((int)Math.pow(2,World.axisCount-1))][2];
        int index = 0;
        for (int i = 0; i < binary.length; i++) {
            for (int i2 = i+1; i2 < binary.length; i2++) {
                int diff = 0;
                for (int i3 = 0; i3 < binary[0].length; i3++) {
                    diff+=(Math.abs(binary[i][i3]-binary[i2][i3]));
                }
                if(diff==1){
                    newEdges[index][0] = i;
                    newEdges[index][1] = i2;
                    index++;
                }
            }
        }
        edges = newEdges;
    };

    private Point position;
    private Point size;
    private boolean drawPoints;

    public Cube(Point position, Point size, boolean drawPoints){
        this.position = position;
        this.size = size;
        this.drawPoints = drawPoints;
    }

    public double getPositionAxis(int axis){
        return position.getAxis(axis);
    }

    public Point getPosition() {
        return position;
    }

    public void setPositionAxis(int axis, double val){
        position.setAxis(axis, val);
    }

    public void setPosition(Point position) {
        for (int i = 0; i < World.axisCount; i++) {
            setPositionAxis(i, position.getAxis(i));
        }
    }

    public void addPoisition(Point add){
        position.add(add);
    }

    public void addPoisitionAxis(int axis, double value){
        position.add(axis, value);
    }

    public double getSizeAxis(int axis){
        return size.getAxis(axis);
    }

    public void setSizeAxis(int axis, double val){
        size.setAxis(axis, val);
    }

    public void setSize(Point size) {
        for (int i = 0; i < World.axisCount; i++) {
            setSizeAxis(i, size.getAxis(i));
        }
    }

    public Point getSize() {
        return size;
    }

    public boolean isWithinAxis(int axis, double point){
        return position.getAxis(axis)<=point&&position.getAxis(axis)+size.getAxis(axis)>=point;
    }

    public boolean isWithinAxis(int axis, Cube other){
        return other.position.getAxis(axis)<=position.getAxis(axis)+size.getAxis(axis)&&
        position.getAxis(axis)<=other.position.getAxis(axis)+other.size.getAxis(axis);
    }

    public Point getMidpoint(){
        Point midPoint = new Point();
        for (int i = 0; i < World.axisCount; i++) {
            midPoint.setAxis(i, getPositionAxis(i)+getSizeAxis(i)/2.0);
        }
        return midPoint;
    }

    public double getMidpointAxis(int axis){
        return getPositionAxis(axis)+getSizeAxis(axis)/2.0;
    }

    public boolean drawPoints(){
        return drawPoints;
    }

    public boolean isCollidingWith(Cube other){
        for (int i = 0; i < World.axisCount; i++) {
            if(!(other.position.getAxis(i)<=position.getAxis(i)+size.getAxis(i)&&
                position.getAxis(i)<=other.position.getAxis(i)+other.size.getAxis(i))){
                return false;
            }
        }
        return true;
    }
}
