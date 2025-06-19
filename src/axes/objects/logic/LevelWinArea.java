package axes.objects.logic;

import axes.objects.Point;
import axes.objects.World;

public class LevelWinArea extends LogicObject{

    public LevelWinArea(Point pos, Point size, String tag){
        super(pos, size, tag, "");
    }
    @Override
    public void update(double dt){
        if(this.isCollidingWith(World.level.getMovableCubeWithTag("Player"), false)&&enabled){
            World.level.win = true;
        }
    }
}
