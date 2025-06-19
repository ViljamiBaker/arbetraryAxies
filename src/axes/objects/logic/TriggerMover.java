package axes.objects.logic;

import axes.objects.Point;
import axes.objects.World;
import axes.objects.cubes.Cube;

public class TriggerMover extends CubeMover{

    public TriggerMover(String tag, String targetTag, Point p1, Point p2, double time){
        super(tag, targetTag, p1, p2, time);
    }

    @Override
    public void initSync(){
        targets = new Cube[1];
        targets[0] = World.level.getLOBJWithTag(targetTag);
    }

    @Override
    protected void doneMoving() {
        ((LogicObject)targets[0]).enabled = enabled;
    }
}
