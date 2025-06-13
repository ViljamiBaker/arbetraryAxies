package axies.objects;

public class MoveableCube extends Cube{

    public static void resolveCollision(MoveableCube cube1, MoveableCube cube2){
        if(!cube1.isCollidingWith(cube2)) return;
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
            c1.isOnGround = true;
            c2.isOnGround = true;
        }

        double diffInVel = c1.getVelocityAxis(lowestDistIndex)*massc1 - c2.getVelocityAxis(lowestDistIndex)*massc2;

        c1.addVelocityAxis(lowestDistIndex, -diffInVel/massc1);
        c2.addVelocityAxis(lowestDistIndex, diffInVel/massc2);
    }

    public static void resolveCollision(MoveableCube mc, Cube nmc){
        if(!mc.isCollidingWith(nmc)) return;
        double lowestNewPos = -1;
        int lowestDistIndex = -1;
        double lowestDist = Double.MAX_VALUE;
        for (int i = 0; i < World.axisCount; i++) {
            if(!mc.isWithinAxis(i,nmc)) continue;
            if(mc.getPositionAxis(i)<nmc.getPositionAxis(i)){
                double newPos = nmc.getPositionAxis(i)-mc.getSizeAxis(i);
                double distance = Math.abs(newPos-mc.getPositionAxis(i));
                if(distance<lowestDist){
                    lowestDistIndex = i;
                    lowestDist = distance;
                    lowestNewPos = newPos - 0.00001;
                }
            }else{
                double newPos = nmc.getPositionAxis(i)+nmc.getSizeAxis(i);
                double distance = Math.abs(newPos-mc.getPositionAxis(i));
                if(distance<lowestDist){
                    lowestDistIndex = i;
                    lowestDist = distance;
                    lowestNewPos = newPos + 0.00001;
                }
            }
        }

        if(lowestDistIndex == -1) return;

        if(lowestDistIndex==World.gravityAxis){
            mc.isOnGround = true;
        }

        mc.setPositionAxis(lowestDistIndex, lowestNewPos);
    }
    
    public static void resolveCollision(MoveableCube mc, Model m){
        for (Cube c : m.getCubes()) {
            MoveableCube.resolveCollision(mc, c);
        }
    }

    private Point velocity = new Point();

    private double drag;

    private double groundDrag;

    public MoveableCube(Point position, Point size, double drag, double groundDrag){
        super(position, size, true);
        this.drag = drag;
        this.groundDrag = groundDrag;
    }

    public MoveableCube(Point position, Point size){
        this(position, size, 0.98, 0.93);
    }

    boolean isOnGround;

    public void update(double dt){
        velocity.add(World.gravityAxis, -World.gravityPower);

        if(isOnGround){
            velocity.mul(groundDrag);
        }else{
            velocity.mul(drag);
        }

        isOnGround = false;

        for (int i = 0; i < World.axisCount; i++) {
            this.addPoisitionAxis(i,velocity.getAxis(i)*dt);
            if(World.isWithinCubes(this)){
                this.addPoisitionAxis(i,-velocity.getAxis(i)*dt-0.00001*Math.signum(velocity.getAxis(i)));
                velocity.setAxis(i, 0);
                if(i==World.gravityAxis){
                    isOnGround = true;
                }
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
}
