package axies.objects.cubes;

import java.awt.Color;

import axies.objects.Point;
import axies.objects.World;

public class Water extends Cube{

    public Water(Point position, Point size){
        super(position, size, false, true, false, new Color(23, 0, 196));
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
