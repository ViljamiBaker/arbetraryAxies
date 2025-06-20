package axes.objects;

import java.awt.Color;

import axes.objects.cubes.Cube;
import axes.objects.cubes.MoveableCube;
import axes.objects.cubes.TextCube;
import axes.objects.cubes.Union;
import axes.objects.cubes.Water;
import axes.objects.logic.CubeMover;
import axes.objects.logic.GenericLogicOBJ;
import axes.objects.logic.LevelWinArea;
import axes.objects.logic.LogicObject;
import axes.objects.logic.TriggerArea;
import axes.objects.logic.TriggerMover;
import axes.objects.logic.TriggerArea.TriggerAreaType;
import axes.util.Vector2D;

public class World {
    
    public static Vector2D[] axes = {new Vector2D(1,-0.3), new Vector2D(0,1), new Vector2D(-0.9,-0.4), new Vector2D(-0.9,0.7)};//, new Vector2D(0.9,0.7)};

    public static Point ZERO = new Point();

    public static int axisCount = axes.length;

    public static int gravityAxis = 1;

    public static double gravityPower = -1;

    public static boolean[] enabledDims = new boolean[axisCount];

    public static int collisionSteps = 1;

    public static Point[] points = {};//{new Point(1,1,1),new Point(2,2),new Point(1,2,1),new Point(2,2)};

    static {Cube.calcCube();}

    public static Level level = new Level("Level 1",
        new Cube[]{
            // walls
            new Union(
                new Cube(new Point(-1,-1,-1,-1), new Point(20,20,20,20), false,false,true), 
                new Cube(new Point(0,0,0,0), new Point(18,18,18,18), false,false,true)),
            
            // floor
            new Cube(new Point(0,0,0,0), new Point(18,1,18,1), false,true,true),
        },
        new MoveableCube[] {
            new MoveableCube(new Point(15,1,1,0.01), new Point(0.9,0.9,0.9,0.9), 0.98, 0.93, "Player"),
            new MoveableCube(new Point(2.0, 1.0, 1.0, 0.0), new Point(0.9,0.9,0.9,0.9)),
            new MoveableCube(new Point(2.0, 2.0, 1.0, 0.0), new Point(0.9,0.9,0.9,0.9)),
            new MoveableCube(new Point(2.0, 3.0, 1.0, 0.0), new Point(0.9,0.9,0.9,0.9)),
            new MoveableCube(new Point(2.0, 4.0, 1.0, 0.0), new Point(0.9,0.9,0.9,0.9)),
            new MoveableCube(new Point(2.0, 5.0, 1.0, 0.0), new Point(0.9,0.9,0.9,0.9)),
        },
        new Model[]{},
        new LogicObject[]{}
        );
        static{
            level.initSync();
        }
    

    public static Vector2D convertPointVector2D(Point initial){
        Vector2D out = new Vector2D();

        for (int i = 0; i < World.axisCount; i++) {
            if(!enabledDims[i])continue;
            out = out.add(World.axes[i].multiply(initial.getAxis(i)));
        }

        return out;
    }
}
