package axies;

public class World {

    static Point[] points = {};//{new Point(1,1,1),new Point(2,2,2),new Point(1,2,1),new Point(2,2,2)};

    static Cube[] cubes = {
        new Cube(new Point(0,0,0), new Point(10,1,10,10,10),false),

        new Cube(new Point(3.9,2,3.9), new Point(0.1,3,1.2,2,2),false),
        new Cube(new Point(3.9,1,3.9), new Point(0.1,1,1.2,0.5,2),true),
        new Cube(new Point(3.9,1,3.9), new Point(1.2,4,0.1,2,2),false),
        new Cube(new Point(5.0,1,3.9), new Point(0.1,4,1.2,2,2),false),
        new Cube(new Point(3.9,1,5.0), new Point(1.2,4,0.1,2,2),false),
        //new Cube(new Point(3.9,1,3.9), new Point(1.1,4,0.1,2,2)),
        //new Cube(new Point(3.9,1,3.9), new Point(1.1,4,0.1,2,2)),

        new Cube(new Point(2,5,2), new Point(5,1,5,2,2),false),
        new Cube(new Point(3,6,3), new Point(3,1,3,2,2),false),

        new Cube(new Point(0,1,7), new Point(5,1,3,2,2),false),
        new Cube(new Point(0,2,7), new Point(10,1,3,2,2),false),
        new Cube(new Point(6,1,7), new Point(4,1,3,2,2),false),
    };

    static MoveableCube[] moveableCubes = {
        new MoveableCube(new Point(8,1,4), new Point(0.9,0.9,0.9,1.0,1.0)),
        new MoveableCube(new Point(8,2,4), new Point(0.9,0.9,0.9,1.0,1.0)),
        new MoveableCube(new Point(8,3,4), new Point(0.9,0.9,0.9,1.0,1.0)),
        new MoveableCube(new Point(8,4,4), new Point(0.9,0.9,0.9,1.0,1.0)),
        new MoveableCube(new Point(8,5,4), new Point(0.9,0.9,0.9,1.0,1.0)),
    };
    
    static Vector2D[] axies = {new Vector2D(1,-0.3), new Vector2D(0,1), new Vector2D(-0.9,-0.4), new Vector2D(-0.9,0.7)};//, new Vector2D(0.9,0.7)};

    static Point ZERO = new Point();

    static int axisCount = axies.length;

    static int gravityAxis = 1;

    static double gravityPower = 1;

    static boolean[] enabledDims = new boolean[axisCount];

    static int collisionSteps = 1;

    static public Vector2D convertPointVector2D(Point initial){
        Vector2D out = new Vector2D();

        for (int i = 0; i < World.axisCount; i++) {
            if(!enabledDims[i])continue;
            out = out.add(World.axies[i].multiply(initial.getAxis(i)));
        }

        return out;
    }

    static public boolean isWithinCubes(Cube test){
        for (Cube c : cubes) {
            if(c.isCollidingWith(test)){
                return true;
            }
        }
        return false;
    }

    static public boolean isWithinMoveableCubes(Cube test){
        for (MoveableCube c : moveableCubes) {
            if(c.isCollidingWith(test)){
                return true;
            }
        }
        return false;
    }


    static void updateMovableCubes(MoveableCube player, double dt){
        for (int i = 0; i < collisionSteps; i++) {
            for (MoveableCube mc: moveableCubes) {
                mc.update(dt);
            }
            for (int i2 = 0; i2 < moveableCubes.length; i2++) {
                MoveableCube.resolveCollision(player, moveableCubes[i2]);
                for (int j = i2+1; j < moveableCubes.length; j++) {
                    MoveableCube.resolveCollision(moveableCubes[j], moveableCubes[i2]);
                }
            }
            for (MoveableCube mc: moveableCubes) {
                for (Cube c : World.cubes) {
                    MoveableCube.resolveCollision(mc, c);
                }
            }
        }
    }
}
