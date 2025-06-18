package axes;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import axes.objects.Level;
import axes.objects.Model;
import axes.objects.Point;
import axes.objects.World;
import axes.objects.cubes.Cube;
import axes.objects.cubes.MoveableCube;
import axes.objects.cubes.TextCube;
import axes.objects.cubes.Union;
import axes.objects.cubes.Water;
import axes.objects.logic.CubeMover;
import axes.objects.logic.GenericLogicOBJ;
import axes.objects.logic.LogicObject;
import axes.objects.logic.TriggerArea;
import axes.objects.logic.TriggerMover;
import axes.objects.logic.TriggerArea.TriggerAreaType;

// LEVEL THINGY
// Number of dims, number of boxes, number of Movable boxes, Number of Logic objects
// point is stored as a list of doubles as long as Number of dims
// boxtype, boxargs
// for n of boxes
//    Cube, Position, Size, drawPoints, drawLines, Collidable, Color, tag
//    Water, Position, Size, tag
//    TextCube, Position, Size, radius, text, tag
//    Union, Position, Size, drawPoints, drawLines, Collidable, Color, tag, Cube2 (saved as if they were a cube)
// for n of Mboxes
//    MovableBox, position, size, drag, groundDrag, tag
// for n of LogicOBJ
//    TriggerArea, pos, size, tag, type, targetTag, nroftags, filterTags
//    TriggerMover, tag, targetTag, p1, p2, time
//    CubeMover, tag, targetTag, p1, p2, time
//    GenericLogicOBJ: print out that it can't be read :(

public class Loader {

    private static void savePoint(Point p, FileWriter fw) throws IOException{
        for (int i = 0; i < World.axisCount; i++) {
            fw.write(p.getAxis(i) + ",");
        }
    }

    private static void saveBool(boolean b, FileWriter fw) throws IOException{
        fw.write((b?1:0)+",");
    }

    private static void saveColor(Color c, FileWriter fw) throws IOException{
        fw.write(c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ",");
    }

    private static Point loadPoint(Scanner sc) throws IOException{
        Point p = new Point();
        for (int i = 0; i < World.axisCount; i++) {
            p.setAxis(i,sc.nextDouble());
        }
        return p;
    }

    private static boolean loadBool(Scanner sc) throws IOException{
        return sc.nextInt()==1;
    }

    private static Color loadColor(Scanner sc) throws IOException{
        return new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
    }

    private static enum TYPE{
        CUBE,
        WATER,
        TXTCUBE,
        UNION,
        MOVABLEBOX,
        TRIGGERAREA,
        TRIGGERMOVER,
        CUBEMOVER,
        GENERICLOGICOBJ
    }

    private static void saveCube(Cube c, FileWriter fw) throws IOException{
        if(c instanceof TriggerArea){
            fw.write(TYPE.TRIGGERAREA.toString() + ",");
            savePoint(c.getPosition(), fw);
            savePoint(c.getSize(), fw);
            fw.write(c.getTag()+",");
            fw.write(((TriggerArea)c).getType().toString()+",");
            fw.write(((TriggerArea)c).getTargetTag()+",");
            fw.write(((TriggerArea)c).getFilterTags().length+",");
            for (int i = 0; i < ((TriggerArea)c).getFilterTags().length; i++) {
                fw.write(((TriggerArea)c).getFilterTags()[i]+",");
            }
        }else if(c instanceof TriggerMover){
            fw.write(TYPE.TRIGGERMOVER.toString() + ",");
            fw.write(c.getTag()+",");
            fw.write(((TriggerMover)c).getTargetTag()+",");
            savePoint(((TriggerMover)c).getP1(), fw);
            savePoint(((TriggerMover)c).getP2(), fw);
            fw.write(((TriggerMover)c).getTime()+",");
        }else if(c instanceof CubeMover){
            fw.write(TYPE.CUBEMOVER.toString() + ",");
            fw.write(c.getTag()+",");
            fw.write(((CubeMover)c).getTargetTag()+",");
            savePoint(((CubeMover)c).getP1(), fw);
            savePoint(((CubeMover)c).getP2(), fw);
            fw.write(((CubeMover)c).getTime()+",");
        }else if(c instanceof GenericLogicOBJ){
            System.out.println("Cant save generic obj :(");
        }else if(c instanceof MoveableCube){
            fw.write(TYPE.MOVABLEBOX.toString() + ",");
            savePoint(c.getPosition(), fw);
            savePoint(c.getSize(), fw);
            fw.write(((MoveableCube)c).getDrag()+",");
            fw.write(((MoveableCube)c).getGroundDrag()+",");
            fw.write(c.getTag()+",");
        }else if(c instanceof Union){
            fw.write(TYPE.UNION.toString() + ",");
            savePoint(c.getPosition(), fw);
            savePoint(c.getSize(), fw);
            saveBool(c.drawPoints(),fw);
            saveBool(c.drawLines(),fw);
            saveBool(c.collidable(),fw);
            saveColor(c.getColor(),fw);
            fw.write(c.getTag()+",");
            saveCube(((Union)c).getNegation(), fw);
        }else if(c instanceof Water){
            fw.write(TYPE.WATER.toString() + ",");
            savePoint(c.getPosition(), fw);
            savePoint(c.getSize(), fw);
            fw.write(c.getTag()+",");
        }else if(c instanceof TextCube){
            fw.write(TYPE.TXTCUBE.toString() + ",");
            savePoint(c.getPosition(), fw);
            savePoint(c.getSize(), fw);
            fw.write(((TextCube)c).getRadius()+",");
            fw.write("\""+((TextCube)c).getText()+"\",");
            fw.write(c.getTag()+",");
        }else if(c instanceof Cube){
            fw.write(TYPE.CUBE.toString() + ",");
            savePoint(c.getPosition(), fw);
            savePoint(c.getSize(), fw);
            saveBool(c.drawPoints(),fw);
            saveBool(c.drawLines(),fw);
            saveBool(c.collidable(),fw);
            saveColor(c.getColor(),fw);
            fw.write(c.getTag()+",");
        }
    }

    private static Cube loadCube(Scanner sc) throws IOException{
        Cube rc;
        Point pos;
        Point size;
        String tag;
        String targetTag;
        Point p1; 
        Point p2; 
        double time;
        boolean drawPoints;
        boolean drawLines;
        boolean collidable;
        Color c;
        switch (TYPE.valueOf(sc.next())) {
            case TRIGGERAREA:
                pos = loadPoint(sc);
                size = loadPoint(sc);
                tag = sc.next();
                TriggerAreaType tatype = TriggerAreaType.valueOf(sc.next());
                targetTag = sc.next();
                int filterTagLength = sc.nextInt();
                String[] filterTags = new String[filterTagLength];
                for (int i = 0; i < filterTagLength; i++) {
                    filterTags[i] = sc.next();
                }
                rc = new TriggerArea(pos, size, tag, tatype, targetTag, filterTags);
                break;
            case TRIGGERMOVER:
                tag = sc.next();
                targetTag = sc.next();
                p1 = loadPoint(sc);
                p2 = loadPoint(sc);
                time = sc.nextDouble();
                rc = new TriggerMover(tag, targetTag, p1, p2, time);
                break;
            case CUBEMOVER:
                tag = sc.next();
                targetTag = sc.next();
                p1 = loadPoint(sc);
                p2 = loadPoint(sc);
                time = sc.nextDouble();
                rc = new CubeMover(tag, targetTag, p1, p2, time);
                break;
            case MOVABLEBOX:
                pos = loadPoint(sc);
                size = loadPoint(sc);
                double drag = sc.nextDouble();
                double groundDrag = sc.nextDouble();
                tag = sc.next();
                rc = new MoveableCube(pos, size, drag, groundDrag, tag);
                break;
            case UNION:
                pos = loadPoint(sc);
                size = loadPoint(sc);
                drawPoints = loadBool(sc);
                drawLines = loadBool(sc);
                collidable = loadBool(sc);
                c = loadColor(sc);
                tag = sc.next();
                rc = new Union(new Cube(pos, size, drawPoints, drawLines, collidable, c, tag), loadCube(sc));
                break;
            case WATER:
                pos = loadPoint(sc);
                size = loadPoint(sc);
                tag = sc.next();
                rc = new Water(pos, size, tag);
                break;
            case TXTCUBE:
                pos = loadPoint(sc);
                size = loadPoint(sc);
                double radius = sc.nextDouble();
                String text = sc.next();
                tag = sc.next();
                rc = new TextCube(pos, size, radius, text, tag);
                break;
            case CUBE:
                pos = loadPoint(sc);
                size = loadPoint(sc);
                drawPoints = loadBool(sc);
                drawLines = loadBool(sc);
                collidable = loadBool(sc);
                c = loadColor(sc);
                tag = sc.next();
                rc = new Cube(pos, size, drawPoints, drawLines, collidable, c, tag);
                break;
            default:
                rc = null;
                System.out.println("Failed to load cube");
                break;
        }
        return rc;
    }

    public static void save(){
        try{
            File datafile = new File(System.getProperty("user.dir") + "/src/axes/levelData/leveldat" + World.level.name + ".txt");
            datafile.createNewFile();
            FileWriter fw = new FileWriter(datafile, true);
            fw.write(World.axisCount + ",");
            fw.write(World.level.cubes.length + ",");
            fw.write(World.level.moveableCubes.length + ",");
            fw.write(World.level.logicObjects.length + ",");
            for (int i = 0; i < World.level.cubes.length; i++) {
                Cube c = World.level.cubes[i];
                saveCube(c, fw);
            }
            for (int i = 0; i < World.level.moveableCubes.length; i++) {
                Cube c = World.level.moveableCubes[i];
                saveCube(c, fw);
            }
            for (int i = 0; i < World.level.logicObjects.length; i++) {
                Cube c = World.level.logicObjects[i];
                saveCube(c, fw);
            }
            fw.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static Level load(String name){
        Level l = null;
        try {
            File datafile = new File(System.getProperty("user.dir") + "/src/axes/levelData/leveldat" + name + ".txt");
            Scanner sc = new Scanner(datafile);
            sc.useDelimiter(",");
            int axisCount = sc.nextInt();
            World.axisCount = axisCount;
            int cCount = sc.nextInt();
            int mcCount = sc.nextInt();
            int lobjCount = sc.nextInt();
            Cube[] cubes = new Cube[cCount];
            MoveableCube[] movableCubes = new MoveableCube[mcCount];
            LogicObject[] logicObjects = new LogicObject[lobjCount];
            for (int i = 0; i < cCount; i++) {
                cubes[i] = loadCube(sc);
            }
            for (int i = 0; i < mcCount; i++) {
                movableCubes[i] = (MoveableCube)loadCube(sc);
            }
            for (int i = 0; i < lobjCount; i++) {
                logicObjects[i] = (LogicObject)loadCube(sc);
            }
            World.level = new Level(name, cubes, movableCubes, new Model[0], logicObjects);
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return l;
    }
}
