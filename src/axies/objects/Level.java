package axies.objects;

public class Level {
    public Cube[] cubes;
    public Model[] models;
    public MoveableCube[] moveableCubes;

    public Level(Cube[] cubes, MoveableCube[] moveableCubes, Model[] models){
        this.cubes = cubes;
        this.moveableCubes = moveableCubes;
        this.models = models;
    }

    public void update(MoveableCube player, double dt){
        
        for (MoveableCube mc: moveableCubes) {
            mc.update(dt);
        }
        for (int i2 = 0; i2 < moveableCubes.length; i2++) {
            MoveableCube.resolveCollision(player, moveableCubes[i2]);
            for (int j = i2+1; j < moveableCubes.length; j++) {
                MoveableCube.resolveCollision(moveableCubes[j], moveableCubes[i2]);
            }
        }
        for (MoveableCube mc: moveableCubes) {
            for (Cube c : cubes) {
                MoveableCube.resolveCollision(mc, c);
            }
            for (Model m : models) {
                MoveableCube.resolveCollision(mc, m);
            }
        }
    }

    public boolean isWithinCubes(Cube test){
        for (Cube c : cubes) {
            if(test.isCollidingWith(c,true)){
                return true;
            }
        }
        return false;
    }
}
