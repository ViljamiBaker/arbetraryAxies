package axes.objects.cubes;

import java.awt.Color;

import axes.objects.Point;
import axes.objects.World;

public class Water extends Cube{

    public Water(Point position, Point size){
        this(position,size,"");
    }

    public Water(Point position, Point size, String tag){
        super(position, size, false, true, false, new Color(23, 0, 196),tag);
    }

    @Override
    public void resolveCollision(MoveableCube mc){
        if(!mc.isCollidingWith(this,false)) return;
        mc.addVelocityAxis(World.gravityAxis, World.gravityPower*-1.1);
        if(mc.getPositionAxis(World.gravityAxis)>getPositionAxis(World.gravityAxis)+getSizeAxis(World.gravityAxis)-0.3){
            mc.isOnGround = true;
        }
    }
}
