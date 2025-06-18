package axes.objects.logic;

import java.util.ArrayList;

import axes.objects.Point;
import axes.objects.World;
import axes.objects.cubes.Cube;
import axes.util.Util;

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

    public TriggerAreaType getType() {
        return type;
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

    public String[] getFilterTags() {
        return filterTags;
    }

    @Override
    public void initSync(){
        targets = World.level.getAllLOBJsWithTag(targetTag);
    }
}
