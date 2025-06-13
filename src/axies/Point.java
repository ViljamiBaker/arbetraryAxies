package axies;

import java.util.Arrays;

public class Point {
    private double[] position;

    public Point(double... position){
        this.position = new double[position.length];
        for (int i = 0; i < position.length; i++) {
            this.position[i] = position[i];
        }
    }

    public Point(){
        this.position = new double[World.axisCount];
    }

    public Point(Point p){
        this.position = new double[World.axisCount];
        for (int i = 0; i < position.length; i++) {
            this.position[i] = p.getAxis(i);
        }
    }

    public double getAxis(int axis){
        if(axis<0)
            return 0;
        if(axis>=position.length){
            setMax(axis+1);
            return 0;
        }
        return position[axis];
    }

    public Point setAxis(int axis, double value){
        if(axis<0)
            return this;
        if(axis>=position.length)
            setMax(axis+1);
        position[axis] = value;

        return this;
    }

    public Point setMax(int count){
        if(count<position.length){
            return this;
        }
        double[] newPos = new double[count];

        for (int i = 0; i < position.length; i++) {
            newPos[i] = position[i];
        }
        
        position = newPos;

        return this;
    }

    public Point add(Point other){
        for (int i = 0; i < World.axisCount; i++) {
            setAxis(i, getAxis(i)+other.getAxis(i));
        }
        return this;
    }

    public Point add(int axis, double value){
        setAxis(axis, getAxis(axis)+value);
        return this;
    }

    public Point mul(double other){
        for (int i = 0; i < World.axisCount; i++) {
            setAxis(i, getAxis(i)*other);
        }
        return this;
    }

    public void rotateAbout(int axis, double theta){
        //TODO: generalize?
        switch (axis) {
            case 0://x
                setAxis(1, getAxis(1)*Math.cos(theta)-getAxis(2)*Math.sin(theta));
                setAxis(2, getAxis(1)*Math.sin(theta)+getAxis(2)*Math.cos(theta));
                break;
            case 1://y
                setAxis(0, getAxis(0)*Math.cos(theta)+getAxis(2)*Math.sin(theta));
                setAxis(2, getAxis(2)*Math.cos(theta)-getAxis(0)*Math.sin(theta));
                break;
            case 2://z
                setAxis(0, getAxis(0)*Math.cos(theta)-getAxis(1)*Math.sin(theta));
                setAxis(1, getAxis(0)*Math.sin(theta)+getAxis(1)*Math.cos(theta));
                break;
            default:
                break;
        }
    }

    @Override
    public String toString(){
        return Arrays.toString(position);
    }
}
