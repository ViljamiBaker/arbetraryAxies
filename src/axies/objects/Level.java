package axies.objects;

import java.util.ArrayList;

import axies.objects.cubes.Cube;
import axies.objects.cubes.MoveableCube;
import axies.objects.logic.LogicObject;

public class Level {
    public Cube[] cubes;
    public Model[] models;
    public MoveableCube[] moveableCubes;
    public LogicObject[] logicObjects;
    public Cube[] allCubes;

    public boolean win = false;

    public Level(Cube[] cubes, MoveableCube[] moveableCubes, Model[] models, LogicObject[] logicObjects){
        this.cubes = cubes;
        this.moveableCubes = moveableCubes;
        this.models = models;
        this.logicObjects = logicObjects;
        calcAllCubes();
    }

    private void calcAllCubes(){
        ArrayList<Cube> allCubes = new ArrayList<>();
        for (Cube cube : cubes) {
            allCubes.add(cube);
        }
        for (Cube cube : moveableCubes) {
            allCubes.add(cube);
        }
        for (Model m : models) {
            for (Cube cube : m.cubes) {
                allCubes.add(cube);
            }
        }
        this.allCubes = allCubes.toArray(new Cube[0]);
    }

    public Level initSync(){
        for (LogicObject logicObject : logicObjects) {
            logicObject.initSync();
        }
        return this;
    }

    public void update(double dt){
        
        for (LogicObject logicObject : logicObjects) {
            logicObject.update(dt);
        }

        for (MoveableCube mc: moveableCubes) {
            mc.update(dt);
        }
        for (int i2 = 0; i2 < moveableCubes.length; i2++) {
            for (int j = i2+1; j < moveableCubes.length; j++) {
                MoveableCube.resolveCollision(moveableCubes[j], moveableCubes[i2]);
            }
        }
        for (MoveableCube mc: moveableCubes) {
            for (Cube c : cubes) {
                c.resolveCollision(mc);
            }
            for (Model m : models) {
                MoveableCube.resolveCollision(mc, m);
            }
        }
    }

    public boolean isWithinCubes(Cube test){
        for (Cube c : cubes) {
            if(c.isCollidingWith(test,true)){
                return true;
            }
        }
        return false;
    }

    public Cube getCubeWithTag(String tag){
        for (Cube cube : cubes) {
            if(cube.getTag().equals(tag)){
                return cube;
            }
        }
        return null;
    }

    public MoveableCube getMovableCubeWithTag(String tag){
        for (MoveableCube cube : moveableCubes) {
            if(cube.getTag().equals(tag)){
                return cube;
            }
        }
        return null;
    }

    public Cube[] getCubesWithTag(String tag, int expected){
        int index = 0;
        Cube[] cubes = new Cube[expected];
        for (Cube cube : cubes) {
            if(cube.getTag().equals(tag)){
                if(index == expected){
                    System.out.println("WARN: more than " + expected + " cubes with tag: " + tag);
                    return cubes;
                }
                cubes[index] = cube;
                index++;
            }
        }
        if(index<expected){
            System.out.println("WARN: less than " + expected + " cubes with tag: " + tag);
        }
        return cubes;
    }

    public Cube[] getAllCubesWithTag(String tag){
        ArrayList<Cube> cubes = new ArrayList<>();
        for (Cube cube : cubes) {
            if(cube.getTag().equals(tag)){
                cubes.add(cube);
            }
        }
        return cubes.toArray(new Cube[0]);
    }

    public LogicObject getLOBJWithTag(String tag){
        for (LogicObject logicObject : logicObjects) {
            if(logicObject.getTag().equals(tag)){
                return logicObject;
            }
        }
        return null;
    }

    public LogicObject[] getLOBJsWithTag(String tag, int expected){
        int index = 0;
        LogicObject[] lobjs = new LogicObject[expected];
        for (LogicObject logicObject : logicObjects) {
            if(logicObject.getTag().equals(tag)){
                if(index == expected){
                    System.out.println("WARN: more than " + expected + " logicObjects with tag: " + tag);
                    return lobjs;
                }
                lobjs[index] = logicObject;
                index++;
            }
        }
        if(index<expected){
            System.out.println("WARN: less than " + expected + " logicObjects with tag: " + tag);
        }
        return lobjs;
    }

    public LogicObject[] getAllLOBJsWithTag(String tag){
        ArrayList<LogicObject> lobjs = new ArrayList<>();
        for (LogicObject logicObject : logicObjects) {
            if(logicObject.getTag().equals(tag)){
                lobjs.add(logicObject);
            }
        }
        return lobjs.toArray(new LogicObject[0]);
    }

    public void addMovableCube(MoveableCube c){
        MoveableCube[] newmc = new MoveableCube[moveableCubes.length+1];
        for (int i = 0; i < moveableCubes.length; i++) {
            newmc[i] = moveableCubes[i];
        }
        newmc[moveableCubes.length] = c;
        moveableCubes = newmc;
        calcAllCubes();
    }
}
