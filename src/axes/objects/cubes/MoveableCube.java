package axes.objects.cubes;

import java.awt.Color;

import axes.objects.Model;
import axes.objects.Point;
import axes.objects.World;

public class MoveableCube extends Cube{

    public static void resolveCollision(MoveableCube cube1, MoveableCube cube2){
        if(!cube1.isCollidingWith(cube2, true)) return;
        double lowestDist = Double.MAX_VALUE;
        int lowestDistIndex = -1;
        boolean lowestFlipped = false;
        MoveableCube c1;
        MoveableCube c2;
        double massc1 = 1;
        double massc2 = 1;
        for (int i = 0; i < World.axisCount; i++) {
            if(!cube1.isWithinAxis(i,cube2)) continue;
            massc1 *= cube1.getSizeAxis(i);
            massc2 *= cube2.getSizeAxis(i);
            boolean flipped;
            if (cube1.getPositionAxis(i)<cube2.getPositionAxis(i)) {
                c1 = cube1;
                c2 = cube2;
                flipped = false;
            }else{
                c1 = cube2;
                c2 = cube1;
                flipped = true;
            }
            double midpoint = (c1.getPositionAxis(i)+c1.getSizeAxis(i)+c2.getPositionAxis(i)) / 2.0;
            double distance = (midpoint - c2.getPositionAxis(i));
            if(distance<lowestDist){
                lowestDist = distance;
                lowestDistIndex = i;
                lowestFlipped = flipped;
            }
        }

        if(lowestDistIndex == -1) return;

        if(!lowestFlipped){
            c1 = cube1;
            c2 = cube2;
        }else{
            c1 = cube2;
            c2 = cube1;
            double temp = massc1;
            massc1 = massc2;
            massc2 = temp;
        }
        c1.addPoisitionAxis(lowestDistIndex, -lowestDist);
        c2.addPoisitionAxis(lowestDistIndex, lowestDist);
        
        if(lowestDistIndex==World.gravityAxis){
            c2.isOnGround = true;
        }

        double diffInVel = c1.getVelocityAxis(lowestDistIndex)*massc1 - c2.getVelocityAxis(lowestDistIndex)*massc2;

        c1.addVelocityAxis(lowestDistIndex, -diffInVel/massc1);
        c2.addVelocityAxis(lowestDistIndex, diffInVel/massc2);
    }
    
    public static void resolveCollision(MoveableCube mc, Model m){
        for (Cube c : m.getCubes()) {
            c.resolveCollision(mc);
        }
    }

    private Point velocity = new Point();

    private double drag;

    private double groundDrag;

    public MoveableCube(Point position, Point size, double drag, double groundDrag, String tag){
        super(position, size, true,true,true, new Color(173, 101, 0), tag);
        this.drag = drag;
        this.groundDrag = groundDrag;
    }

    public MoveableCube(Point position, Point size){
        this(position, size, 0.98, 0.93, "Moving");
    }

    public MoveableCube(Point position, Point size, String tag){
        this(position, size, 0.98, 0.93, tag);
    }

    boolean isOnGround;

    public void update(double dt){

        if(isOnGround){
            velocity.mul(groundDrag);
        }else{
            velocity.add(World.gravityAxis, World.gravityPower);
            velocity.mul(drag);
        }

        this.addPoisitionAxis(World.gravityAxis, 0.1*Math.signum(World.gravityPower));
        isOnGround = World.level.isWithinCubes(this);
        this.addPoisitionAxis(World.gravityAxis, -0.1*Math.signum(World.gravityPower));

        for (int i = 0; i < World.axisCount; i++) {
            this.addPoisitionAxis(i,velocity.getAxis(i)*dt);
            if(World.level.isWithinCubes(this)){
                this.addPoisitionAxis(i,-velocity.getAxis(i)*dt-0.000001*Math.signum(velocity.getAxis(i)));
                velocity.setAxis(i, 0);
            }
        }
    }

    public void setVelocityAxis(int axis, double val){
        velocity.setAxis(axis, val);
    }

    public void setVelocity(Point velocity) {
        for (int i = 0; i < World.axisCount; i++) {
            setVelocityAxis(i, velocity.getAxis(i));
        }
    }

    public void addVelocity(Point add){
        velocity.add(add);
    }

    public void addVelocityAxis(int axis, double val){
        velocity.add(axis,val);
    }

    public void mulVelocity(double val){
        velocity.mul(val);
    }

    public Point getVelocity() {
        return velocity;
    }

    public double getVelocityAxis(int axis) {
        return velocity.getAxis(axis);
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public double getGroundDrag() {
        return groundDrag;
    }

    public double getDrag() {
        return drag;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof MoveableCube)) return false;
        MoveableCube m = (MoveableCube)o;
        return this.getPosition().equals(m.getPosition())&&this.getSize().equals(m.getSize());
    }
}
