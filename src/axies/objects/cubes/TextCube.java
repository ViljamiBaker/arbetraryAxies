package axies.objects.cubes;

import java.awt.Color;

import axies.objects.Point;

public class TextCube extends Cube{
    double radius; 
    String text;
    public TextCube(Point position, Point size, double radius, String text){
        super(position, size, true, true, false, new Color(46, 255, 227));
        this.text = text;
        this.radius = radius;
    }

    public String text(){
        return text;
    }

    public double radius(){
        return radius;
    }
}
