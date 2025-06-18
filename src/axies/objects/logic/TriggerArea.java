package axies.objects.logic;

import java.util.ArrayList;

import axies.objects.Point;
import axies.objects.World;
import axies.objects.cubes.Cube;
import axies.util.Util;

// activates while stuff is within the area
public class TriggerArea extends LogicObject{

    String[] filterTags;

    ArrayList<Cube> cubesWithin = new ArrayList<>();

    TriggerAreaType type;

    public enum TriggerAreaType{
        ONOFF,
        ON
    }

    // creates a TriggerArea covering from pos to size that targets targetTag and looks for filterTag
    // if filterTag = "" then no filter is used
    public TriggerArea(Point pos, Point size, TriggerAreaType type, String targetTag, String... filterTags){
        super(pos, size, "", targetTag);
        this.filterTags = filterTags;
        this.type = type;
    }

    public TriggerArea(Point pos, Point size, String tag, TriggerAreaType type, String targetTag, String... filterTags){
        super(pos, size, tag, targetTag);
        this.filterTags = filterTags;
        this.type = type;
    }

    @Override
    public void update(double dt){

        ArrayList<Cube> newCubesWithin = new ArrayList<>();

        for (Cube cube : World.level.allCubes) {
            if((Util.arrayContains(filterTags, cube.getTag())||filterTags.length==0)&&this.isCollidingWith(cube, false)){
                newCubesWithin.add(cube);
            }
        }

        if(newCubesWithin.size() == 0&& cubesWithin.size() != 0){
            if(type == TriggerAreaType.ONOFF)
            for (Cube target : targets) {
                ((LogicObject)target).deactivate();
            }
        }

        if(newCubesWithin.size() != 0&& cubesWithin.size() == 0){
            for (Cube target : targets) {
                ((LogicObject)target).activate();
            }
        }

        cubesWithin.clear();
        cubesWithin.addAll(newCubesWithin);
    }

    @Override
    public void initSync(){
        targets = World.level.getAllLOBJsWithTag(targetTag);
    }
}
