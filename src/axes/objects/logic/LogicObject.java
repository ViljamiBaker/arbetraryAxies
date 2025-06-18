package axes.objects.logic;

import java.awt.Color;

import axes.objects.Point;
import axes.objects.World;
import axes.objects.cubes.Cube;
// Base class, dont init
public class LogicObject extends Cube{

    protected Cube[] targets;

    protected String targetTag;

    protected boolean enabled = false;

    // creates a VISIBLE LogicObject, tag is for THIS, targetTag is what the TARGETS have
    public LogicObject(Point pos, Point size, boolean collide, Color c, String tag, String targetTag){
        super(pos, size, true, true, collide, c, tag);
        this.targetTag = targetTag;
    }

    // creates an INVISIBLE LogicObject, tag is for THIS, targetTag is what the TARGETS have
    public LogicObject(String tag, String targetTag){
        this(World.ZERO, World.ZERO, tag, targetTag);
    }

    // creates an INVISIBLE LogicObject, tag is for THIS, targetTag is what the TARGETS have
    public LogicObject(Point pos, Point size, String tag, String targetTag){
        super(pos, size, false, false, false, Color.MAGENTA, tag);
        this.targetTag = targetTag;
    }

    // gets the target tag for saving and loading
    public String getTargetTag() {
        return targetTag;
    }

    // called every frame
    public void update(double dt){}

    // called when other LogicObjects activates this one
    public void activate(){}

    // called when other LogicObjects deactivates this one
    public void deactivate(){}

    // called by Level once all cubes have been initialised
    public void initSync(){}
}
