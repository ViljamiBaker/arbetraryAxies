package axies.objects.logic;

import axies.objects.Point;
import axies.objects.World;
import axies.objects.cubes.Cube;
import axies.util.Util;

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
            }else{
                currentLerp += speed;
            }
            targets[0].setPosition(Util.lerp(p1, p2, currentLerp));
        }else{
            double speed = -1.0/time*dt;
            if(currentLerp+speed<0){
                currentLerp = 0;
            }else{
                currentLerp += speed;
            }
            targets[0].setPosition(Util.lerp(p1, p2, currentLerp));
        }
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
