package axies.objects;

import axies.util.Vector2D;

public class World {
    
    public static Vector2D[] axies = {new Vector2D(1,-0.3), new Vector2D(0,1), new Vector2D(-0.9,-0.4), new Vector2D(-0.9,0.7)};//, new Vector2D(0.9,0.7)};

    public static Point ZERO = new Point();

    public static int axisCount = axies.length;

    public static int gravityAxis = 1;

    public static double gravityPower = -1;

    public static boolean[] enabledDims = new boolean[axisCount];

    public static int collisionSteps = 1;

    public static Point[] points = {};//{new Point(1,1,1),new Point(2,2),new Point(1,2,1),new Point(2,2)};

    static {Cube.calcCube();}

    public static Level level = new Level(
        new Cube[]{
            new Cube(new Point(0,0,0), new Point(10,1,10,10,10),false, true, true),

            new Cube(new Point(0,1,7), new Point(5,1,3,2),false, true, true),
            new Cube(new Point(0,2,7), new Point(10,1,3,2),false, true, true),
            new Cube(new Point(6,1,7), new Point(4,1,3,2),false, true, true),

            new Cube(new Point(-1,0,-1,-1), new Point(1,7,11,11),false,false, true),
            new Cube(new Point(10,0,-1,-1), new Point(1,7,12,12),false,false, true),

            new Cube(new Point(-1,0,-1,-1), new Point(11,7,1,11),false,false, true),
            new Cube(new Point(-1,0,10,-1), new Point(12,7,1,12),false,false, true),
            
            new Cube(new Point(-1,0,-1,-1), new Point(11,7,11,1),false,false, true),
            new Cube(new Point(-1,0,-1,10), new Point(12,7,11,1),false,false, true),

            new Cube(new Point(-1,0,10,-1), new Point(1,7,1,12),false, false, true),
            
            new TextCube(new Point(4.4,1.3,4.4,0.4), new Point(0.2,0.2,0.2,0.2), 0.5, "Haha you're trapped"),

            new TextCube(new Point(4.4,1.3,4.4,5), new Point(0.2,0.2,0.2,0.2), 1, "Why are you out here"),
        },
        new MoveableCube[] {
            new MoveableCube(new Point(8,1,4), new Point(0.9,0.9,0.9,1.0,1.0)),
            new MoveableCube(new Point(8,2,4), new Point(0.9,0.9,0.9,1.0,1.0)),
            new MoveableCube(new Point(8,3,4), new Point(0.9,0.9,0.9,1.0,1.0)),
            new MoveableCube(new Point(8,4,4), new Point(0.9,0.9,0.9,1.0,1.0)),
            new MoveableCube(new Point(8,5,4), new Point(0.9,0.9,0.9,1.0,1.0)),
        },
        new Model[]{
            new Model(
                new Cube[]{
                    new Cube(new Point(3.9,2,3.9), new Point(0.1,3,1.2,2),false, true, true),
                    new Cube(new Point(3.9,1,4.0), new Point(0.1,1,1.0,0.5,2),false, true, true),
                    new Cube(new Point(3.9,1,3.9), new Point(1.2,4,0.1,2),false, true, true),
                    new Cube(new Point(5.0,1,3.9), new Point(0.1,4,1.2,2),false, true, true),
                    new Cube(new Point(3.9,1,5.0), new Point(1.2,4,0.1,2),false, true, true),
                
                    new Cube(new Point(2,5,2), new Point(5,1,2,2),false, true, true),
                    new Cube(new Point(2,5,4), new Point(2,1,1,2),false, true, true),
                    new Cube(new Point(5,5,4), new Point(2,1,1,2),false, true, true),
                    new Cube(new Point(2,5,5), new Point(5,1,2,2),false, true, true),
                
                    new Cube(new Point(3,6,3), new Point(3,1,1,2),false, true, true),
                    new Cube(new Point(3,6,4), new Point(1,1,1,2),false, true, true),
                    new Cube(new Point(5,6,4), new Point(1,1,1,2),false, true, true),
                    new Cube(new Point(3,6,5), new Point(3,1,1,2),false, true, true),
                }, 
                new Cube[]{
                    new Cube(new Point(3.9,1,3.9), new Point(1.2,4,1.2,2),false, true, false),
                    new Cube(new Point(3.9,1,4.0), new Point(0.1,1,1.0,0.5,2),false, true, false),
                    new Cube(new Point(4,1,4), new Point(1,6,1,2),false, true, false),
                    new Cube(new Point(2,5,2), new Point(5,1,5,2),false, true, false),
                    new Cube(new Point(3,6,3), new Point(3,1,3,2),false, true, false),
                },
                false)
        });

    

    

    

    public static Vector2D convertPointVector2D(Point initial){
        Vector2D out = new Vector2D();

        for (int i = 0; i < World.axisCount; i++) {
            if(!enabledDims[i])continue;
            out = out.add(World.axies[i].multiply(initial.getAxis(i)));
        }

        return out;
    }
}
