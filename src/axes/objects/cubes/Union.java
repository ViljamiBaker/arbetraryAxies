package axes.objects.cubes;

import axes.objects.Point;
import axes.objects.World;

public class Union extends Cube{
    Cube negation;

    Cube[] alternateCubes;

    public Union(Cube part, Cube negation){
        super(part);
        this.negation = negation;

        alternateCubes = new Cube[World.axisCount*2];

        for (int i = 0; i < World.axisCount; i++) {
            Point lowerPos = new Point();
            for (int j = 0; j < World.axisCount; j++) {
                lowerPos.setAxis(j, getPositionAxis(j));
            }
            Point lowerSize = new Point();
            for (int j = 0; j < World.axisCount; j++) {
                if(i==j){
                    lowerSize.setAxis(j, negation.getPositionAxis(j)-getPositionAxis(j));
                }else{
                    lowerSize.setAxis(j, getSizeAxis(j));
                }
            }

            Cube lower = new Cube(lowerPos, lowerSize, false, false, true);

            for (int j = 0; j < World.axisCount; j++) {
                if(lower.getSizeAxis(j)<=0){
                    lower.setPositionAxis(0,-1000000);
                }
            }

            Point upperPos = new Point();
            for (int j = 0; j < World.axisCount; j++) {
                if(i==j){
                    upperPos.setAxis(j, negation.getPositionAxis(i)+negation.getSizeAxis(i));
                }else{
                    upperPos.setAxis(j, getPositionAxis(j));
                }
            }
            Point upperSize = new Point();
            for (int j = 0; j < World.axisCount; j++) {
                if(i==j){
                    upperSize.setAxis(j, getSizeAxis(j) - (negation.getPositionAxis(j)-getPositionAxis(j)) - negation.getSizeAxis(j));
                }else{
                    upperSize.setAxis(j, getSizeAxis(j));
                }
            }

            Cube upper = new Cube(upperPos, upperSize, false, false, true);

            for (int j = 0; j < World.axisCount; j++) {
                if(upper.getSizeAxis(j)<=0){
                    upper.setPositionAxis(0,-1000000);
                }
            }

            //lower.addPoisitionAxis(1, i*3+4);
            //upper.addPoisitionAxis(1, i*3+4);

            alternateCubes[i*2] = lower;
            alternateCubes[i*2+1] = upper;
        }
    }
    @Override
    public void resolveCollision(MoveableCube mc){
        for (Cube c : alternateCubes) {
            c.resolveCollision(mc);
        }
    }

    public Cube getNegation() {
        return negation;
    }

    public Cube[] getAlternateCubes() {
        return alternateCubes;
    }

    @Override
    public boolean isCollidingWith(Cube other, boolean collide){
        if(!(this.collidable()&&other.collidable())&&collide) return false;

        for (int i = 0; i < World.axisCount; i++) {
            if(!isWithinAxis(i, other)||negation.isWithinAxis(i, other)){
                return false;
            }
        }
        return true;
    }
}
