package axies.objects.cubes;

import java.awt.Color;

import axies.objects.Point;
import axies.objects.World;

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
    private boolean drawLines;
    private boolean collidable;

    private String tag;

    private Color c;

    public Cube(Point position, Point size, boolean drawPoints, boolean drawLines, boolean collidable, Color c, String tag){
        this.position = position;
        this.size = size;
        this.drawPoints = drawPoints;
        this.drawLines = drawLines;
        this.collidable = collidable;
        this.c = c;
        this.tag = tag;
    }

    public Cube(Point position, Point size, boolean drawPoints, boolean drawLines, boolean collidable, Color c){
        this(position, size, drawPoints, drawLines, collidable, Color.BLACK,"");
    }

    public Cube(Point position, Point size, boolean drawPoints, boolean drawLines, boolean collidable){
        this(position, size, drawPoints, drawLines, collidable, Color.BLACK);
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

    public boolean isWithinAxis(int axis, double p1, double p2){
        return (Math.min(p1, p2)<=position.getAxis(axis)+size.getAxis(axis)&&
                position.getAxis(axis)<=Math.max(p1, p2));
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

    public boolean drawLines(){
        return drawLines;
    }

    public boolean collidable(){
        return collidable;
    }

    public boolean isCollidingWith(Cube other, boolean collide){
        if(!(this.collidable()&&other.collidable())&&collide) return false;

        for (int i = 0; i < World.axisCount; i++) {
            if(!(other.position.getAxis(i)<=position.getAxis(i)+size.getAxis(i)&&
                position.getAxis(i)<=other.position.getAxis(i)+other.size.getAxis(i))){
                return false;
            }
        }
        return true;
    }

    public String getTag() {
        return tag;
    }

    public Color getColor() {
        return c;
    }

    @Override
    public String toString(){
        return "Pos: " + position.toString() + " Size:" + size.toString() + " Tag:" + tag + " Color: " + c.toString();
    }
}
