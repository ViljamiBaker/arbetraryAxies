package axes.objects.cubes;

import java.awt.Color;

import axes.objects.Point;

public class TextCube extends Cube{
    double radius; 
    String text;

    public TextCube(Point position, Point size, double radius, String text, String tag){
        super(position, size, true, true, false, new Color(46, 255, 227), tag);
        this.text = text;
        this.radius = radius;
    }

    public TextCube(Point position, Point size, double radius, String text){
        this(position, size, radius, text, "");
    }

    public TextCube(Point position, double radius, String text){
        this(position, new Point(0.4,0.4,0.4,0.4), radius, text);
    }

    public String getText(){
        return text;
    }

    public double getRadius(){
        return radius;
    }
}
