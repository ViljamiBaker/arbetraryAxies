package axes.objects.logic;

import axes.objects.Point;
import axes.objects.World;
import axes.objects.cubes.Cube;
import axes.util.Util;

public class CubeMover extends LogicObject{
    private Point p1; 
    private Point p2; 

    private double time;
    private double currentLerp = 0;

    public CubeMover(String tag, String targetTag, Point p1, Point p2, double time){
        super(tag, targetTag);
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
    }

    @Override
    public void update(double dt){
        if(enabled){
            double speed = 1.0/time*dt;
            if(currentLerp+speed>1){
                currentLerp = 1;
                doneMoving();
            }else{
                currentLerp += speed;
            }
            targets[0].setPosition(Util.lerp(p1, p2, currentLerp));
        }else{
            double speed = -1.0/time*dt;
            if(currentLerp+speed<0){
                currentLerp = 0;
                doneMoving();
            }else{
                currentLerp += speed;
            }
            targets[0].setPosition(Util.lerp(p1, p2, currentLerp));
        }
    }

    protected void doneMoving(){}

    public Point getP1() {
        return p1;
    }
    
    public Point getP2() {
        return p2;
    }

    public double getTime() {
        return time;
    }

    @Override
    public void activate(){
        enabled = true;
    }

    @Override
    public void deactivate(){
        enabled = false;
    }

    @Override
    public void initSync(){
        targets = new Cube[1];
        targets[0] = World.level.getCubeWithTag(targetTag);
    }
}
