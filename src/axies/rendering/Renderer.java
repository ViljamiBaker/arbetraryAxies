package axies.rendering;

import javax.swing.JFrame;

import axies.objects.Model;
import axies.objects.Point;
import axies.objects.World;
import axies.objects.cubes.Cube;
import axies.objects.cubes.MoveableCube;
import axies.objects.cubes.TextCube;
import axies.util.Util;
import axies.util.Vector2D;
import axies.util.VectorMD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Renderer extends JFrame{
    Graphics g;
    Camera camera;
    keyListener kl = new keyListener();
    mouseListener ml = new mouseListener();
    private Vector2D center = new Vector2D(500,400);

    boolean normalising = true;
    boolean drawAllLines = false;
    boolean drawAllPoints = false;

    boolean camLock = false;

    MoveableCube player = new MoveableCube(new Point(1,70,1,0.01), new Point(0.9,0.9,0.9,0.9), 0.98, 0.93, "Player");

    MoveableCube cubeHolding = null;

    int selectedAxis = -1;

    int movingAxis = 0;

    public Renderer(){
        this.setTitle("yowza");
        this.setSize(1000,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        g = this.getGraphics();
        camera = new Camera(new Vector2D(0,0), new VectorMD(1,Math.PI));
        camera.zoom = 0.033;
        this.addKeyListener(kl);
        this.addMouseListener(ml);

        World.ZERO = new Point();
        setBGValues();

        World.enabledDims[0] = true;
        World.enabledDims[1] = true;
        World.enabledDims[2] = true;

        World.level.addMovableCube(player);
    }

    public class mouseListener implements MouseListener{
        public boolean button1Down = false;
        public boolean button2Down = false;
        public boolean button3Down = false;
        public void mouseExited(MouseEvent e){}
        public void mouseReleased(MouseEvent e){
            if(e.getButton() == MouseEvent.BUTTON1){
                button1Down = false;
            }
            if(e.getButton() == MouseEvent.BUTTON2){
                button2Down = false;
            }
            if(e.getButton() == MouseEvent.BUTTON3){
                button3Down = false;
            }
        }
        public void mousePressed(MouseEvent e){
            if(e.getButton() == MouseEvent.BUTTON1){
                button1Down=true;
                int lowestDistIndex = -1;
                double lowestDist = 1000000;
                for (int i = 0; i < World.axisCount; i++) {
                    if(!World.enabledDims[i])continue;
                    double dist = World.axies[i].add(convertFromCamera(new Vector2D(getMousePosition().x,getMousePosition().y)).n()).magnitude();
                    if(dist<lowestDist){
                        lowestDistIndex = i;
                        lowestDist = dist;
                    }
                }
                selectedAxis = lowestDistIndex;
            }
            if(e.getButton() == MouseEvent.BUTTON2){
                button2Down=true;
                selectedAxis = -1;
            }
            if(e.getButton() == MouseEvent.BUTTON3){
                button3Down=true;
            }
        }
        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
    }

    Vector2D convertToCamera(Vector2D initial){
        return initial.add(camera.pos.n()).convert().addD(-camera.rot.D).convert().div(camera.zoom).add(center);
    }

    Vector2D convertFromCamera(Vector2D initial){
        return initial.add(center.n()).multiply(camera.zoom).convert().addD(camera.rot.D).convert().add(camera.pos);
    }

    int[] keys1to9 = {0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39};

    public void run(){
        double dt = 0;
        while (true) {
            double t1 = System.nanoTime();
            try {Thread.sleep((long)Math.min(0.0, 16-dt));} catch (Exception e) {}
            // drawing
            paint();
            camera.move(
                kl.isKeyDown(KeyEvent.VK_W),
                kl.isKeyDown(KeyEvent.VK_A),
                kl.isKeyDown(KeyEvent.VK_S),
                kl.isKeyDown(KeyEvent.VK_D),
                false,
                false,
                kl.isKeyDown(KeyEvent.VK_R),
                kl.isKeyDown(KeyEvent.VK_F),
                5.0,
                0,
                1.02
            );

            // player movement
            if(kl.isKeyDown(KeyEvent.VK_I)) {
                player.addVelocityAxis(0, -0.2);
            }
            if(kl.isKeyDown(KeyEvent.VK_K)) {
                player.addVelocityAxis(0, 0.2);
            }

            if(kl.isKeyDown(KeyEvent.VK_L)) {
                player.addVelocityAxis(2, 0.2);
            }
            if(kl.isKeyDown(KeyEvent.VK_J)) {
                player.addVelocityAxis(2, -0.2);
            }

            if(kl.isKeyDown(KeyEvent.VK_O)) {
                player.addVelocityAxis(3, 0.2);
            }
            if(kl.isKeyDown(KeyEvent.VK_U)) {
                player.addVelocityAxis(3, -0.2);
            }

            if(kl.isKeyPressed(KeyEvent.VK_SPACE)&&player.isOnGround()) {
                player.addVelocityAxis(World.gravityAxis, 30);
            }

            if(camLock) camera.pos = (World.convertPointVector2D(player.getMidpoint()));

            if(kl.isKeyPressed(KeyEvent.VK_P)) {
                if(cubeHolding != null){
                    cubeHolding = null;
                }else{
                    int lowestDistIndex = -1;
                    double lowestDist = Double.MAX_VALUE;
                    for (int i = 0; i < World.level.moveableCubes.length; i++) {
                        if(World.level.moveableCubes[i].equals(player)) continue;
                        double dist = 0;
                        for (int j = 0; j < World.axisCount; j++) {
                            dist += Math.abs(player.getMidpointAxis(j)-World.level.moveableCubes[i].getMidpointAxis(j));
                        }
                        if(dist<lowestDist){
                            lowestDistIndex = i;
                            lowestDist = dist;
                        }
                    }
                    if(lowestDist<=1.5)
                        cubeHolding = World.level.moveableCubes[lowestDistIndex];
                }
            }

            if(cubeHolding != null){
                cubeHolding.setPosition(new Point(player.getPosition()).add(new Point(0,1,0)));
                cubeHolding.setVelocity(new Point());
            }

            // axies transformations

            if(ml.button3Down&&selectedAxis!=-1){
                World.axies[selectedAxis] = convertFromCamera(new Vector2D(getMousePosition().x,getMousePosition().y));
            }
            for (int i = 0; i < World.axisCount; i++) {
                World.enabledDims[i] = (kl.isKeyPressed(keys1to9[i]) ? !World.enabledDims[i]:World.enabledDims[i]);
            }

            if(kl.isKeyPressed(KeyEvent.VK_N))normalising = !normalising;
            if(kl.isKeyPressed(KeyEvent.VK_Z))drawAllLines = !drawAllLines;
            if(kl.isKeyPressed(KeyEvent.VK_X))drawAllPoints = !drawAllPoints;

            if(kl.isKeyPressed(KeyEvent.VK_C))camLock = !camLock;
            
            if(kl.isKeyPressed(KeyEvent.VK_Y)){
                movingAxis++;
                if(movingAxis>=World.axisCount){
                    movingAxis=World.axisCount-1;
                }
            }
            if(kl.isKeyPressed(KeyEvent.VK_H)){
                movingAxis--;
                if(movingAxis<0){
                    movingAxis=0;
                }
            }

            if(normalising) {
                for (int i = 0; i < World.axies.length; i++) {
                    World.axies[i] = World.axies[i].normal();
                }
            }

            if(kl.isKeyDown(KeyEvent.VK_T)) {
                Vector2D[] nwa = new Vector2D[World.axisCount];
                for (int i = 0; i < World.axies.length; i++) {
                    Point convPoint = new Point();
                    convPoint.setAxis(i, 1);
                    convPoint.rotateAbout(1, 0.03);
                    nwa[i] = World.convertPointVector2D(convPoint);
                }
                World.axies = nwa;
            }

            if(kl.isKeyDown(KeyEvent.VK_G)) {
                Vector2D[] nwa = new Vector2D[World.axisCount];
                for (int i = 0; i < World.axies.length; i++) {
                    Point convPoint = new Point();
                    convPoint.setAxis(i, 1);
                    convPoint.rotateAbout(1, -0.03);
                    nwa[i] = World.convertPointVector2D(convPoint);
                }
                World.axies = nwa;
            }

            kl.update();

            World.level.update(dt);

            double t2 = System.nanoTime();
            dt = Math.min((t2-t1)/1000000000.0,0.05);
            //System.out.println(dt);
        }
    }

    void fillCircle(Vector2D p, Graphics g){
        g.fillOval((int)(p.x-0.1/camera.zoom),(int)(p.y-0.1/camera.zoom),(int)(0.2/camera.zoom),(int)(0.2/camera.zoom));
    }

    void drawCircle(Vector2D p, Graphics g){
        g.drawOval((int)(p.x-0.1/camera.zoom),(int)(p.y-0.1/camera.zoom),(int)(0.2/camera.zoom),(int)(0.2/camera.zoom));
    }

    void drawLine(Vector2D p1, Vector2D p2, Graphics g){
        g.drawLine((int)(p1.x),(int)(p1.y),(int)(p2.x),(int)(p2.y));
    }

    Vector2D cf(Point p){
        return convertToCamera(World.convertPointVector2D(p));
    }

    Color[] axisColors = {Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.MAGENTA};

    int gridCount = 10;

    int[][] pairs;

    void setBGValues(){
        pairs = new int[Util.triangleNum(World.axisCount)][2];
        int index = 0;
        for (int i = 0; i < World.axisCount; i++) {
            for (int j = i+1; j < World.axisCount; j++) {
                pairs[index] = new int[] {i,j};
                index++;
            }
        }
    }

    void drawBackGround(Graphics g){
        for (int i = 0; i < World.axisCount; i++) {
            Point point = new Point();
            point.setAxis(i, 1);
            g.setColor(axisColors[i]);
            if(selectedAxis==i){
                fillCircle(cf(point), g);
            }else{
                drawCircle(cf(point), g);
            }
            point.setAxis(i, 10);
            drawLine(cf(World.ZERO), cf(point) ,g);
        }
        for (int h = 0; h < pairs.length; h++) {
            Color ac1 = axisColors[pairs[h][0]];
            Color ac2 = axisColors[pairs[h][1]];
            g.setColor(new Color((ac1.getRed()+ac2.getRed())/2, (ac1.getGreen()+ac2.getGreen())/2, (ac1.getBlue()+ac2.getBlue())/2, 128));
            for (int i = 1; i <= gridCount; i++) {
                Point v1 = new Point();
                Point v2 = new Point();

                v1.setAxis(pairs[h][0], i);
                v2.setAxis(pairs[h][0], i);
                v2.setAxis(pairs[h][1], 10);
                drawLine(cf(v1),cf(v2),g);

                Point h1 = new Point();
                Point h2 = new Point();

                h1.setAxis(pairs[h][1], i);
                h2.setAxis(pairs[h][1], i);
                h2.setAxis(pairs[h][0], 10);
                drawLine(cf(h1),cf(h2),g);
            }
        }
    }

    private void drawCube(Cube cube, Graphics g){
        if(!(cube.drawLines()||drawAllLines)&&!(cube.drawPoints()||drawAllPoints))return;
        Color c = cube.getColor();
        double distance = 0;
        int disabledDimCount = 1;
        for (int i = 0; i < World.axisCount; i++) {
            if(!World.enabledDims[i]){
                if(!cube.isWithinAxis(i,player.getPositionAxis(i)+0.45)){
                    disabledDimCount++;
                    double dist2 = Math.min(
                        Math.abs(player.getPositionAxis(i)+player.getSizeAxis(i)-cube.getPositionAxis(i)),
                            Math.abs(player.getPositionAxis(i)-cube.getPositionAxis(i)-cube.getSizeAxis(i)));
                    dist2 = dist2*dist2;
                    distance+=dist2;
                }
            }
        }
        distance = Math.sqrt(distance);
        double opacity = Math.max(1.0-distance, 0)/disabledDimCount;

        g.setColor(new Color(c.getRed(),c.getBlue(),c.getGreen(),(int)(opacity*255.0)));
        Point[] points = new Point[Cube.vertexes.length];
        for (int i = 0; i < Cube.vertexes.length; i++) {
            points[i] = new Point();
            for (int j = 0; j < Cube.vertexes[0].length; j++) {
                points[i].setAxis(j,cube.getPositionAxis(j)+cube.getSizeAxis(j)*Cube.vertexes[i][j]);
            }
            if(cube.drawPoints()||drawAllPoints)
            drawCircle(cf(points[i]), g);
        }

        if(cube.drawLines()||drawAllLines)
        for (int i = 0; i < Cube.edges.length; i++) {
            drawLine(cf(points[Cube.edges[i][0]]), cf(points[Cube.edges[i][1]]), g);
        }

        if(cube instanceof TextCube){
            if(player.getMidpoint().dist(cube.getMidpoint())<=((TextCube)cube).radius()||player.isCollidingWith(cube, false)){
                g.setColor(Color.BLACK);        
                Vector2D converted = cf(cube.getMidpoint());
                String text = ((TextCube)cube).text();
                g.drawString(text, (int)converted.x - g.getFontMetrics().stringWidth(text)/2, (int)converted.y - g.getFontMetrics().getHeight()/2);
            }
        }
        g.setColor(c);
    }

    private void drawModel(Model m, Graphics g){
        for (Cube c : m.getVisableCubes()) {
            drawCube(c, g);
        }
        /*
        Color c = g.getColor();
        if(m.drawPoints())
        for (int i = 0; i < m.getVertexes().length; i++) {
            drawCircle(cf(m.getVertexes()[i]), g);
        }

        for (int i = 0; i < m.getEdges().length; i++) {
            double distance = 0;
            int disabledDimCount = 1;
            
            for (int j = 0; j < World.axisCount; j++) {
                Point p1 = m.getVertexes()[m.getEdges()[i][0]];
                Point p2 = m.getVertexes()[m.getEdges()[i][1]];
                if(!World.enabledDims[j]){
                    if(!Util.within(Math.min(p1.getAxis(j),p2.getAxis(j))-0.5,Math.max(p1.getAxis(j),p2.getAxis(j))+0.5,player.getMidpointAxis(j))){
                        disabledDimCount++;
                        double dist2 = Math.min(
                            Math.abs(player.getPositionAxis(j)+player.getSizeAxis(j)-p1.getAxis(j)),
                                Math.abs(player.getPositionAxis(j)-p2.getAxis(j)));
                        dist2 = dist2*dist2;
                        distance+=dist2;
                    }
                }
            }
            distance = Math.sqrt(distance);
            double opacity = Math.max(1.0-distance, 0)/disabledDimCount;
    
            g.setColor(new Color(c.getRed(),c.getBlue(),c.getGreen(),(int)(opacity*255.0)));
            drawLine(cf(m.getVertexes()[m.getEdges()[i][0]]), cf(m.getVertexes()[m.getEdges()[i][1]]), g);
        }
        g.setColor(c);*/
    }

    public void paint(){
        if(g == null)return;

        BufferedImage buff = new BufferedImage(1000, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics bg = buff.getGraphics();
        bg.setColor(Color.WHITE);
        bg.fillRect(0, 0, 1000, 800);

        drawBackGround(bg);

        bg.setColor(Color.BLACK);

        for (Point point: World.points) {
            drawCircle(cf(point), bg);
        }

        for (Cube cube: World.level.cubes) {
            drawCube(cube, bg);
        }

        for (Cube cube: World.level.logicObjects) {
            drawCube(cube, bg);
        }

        for (Model model: World.level.models) {
            drawModel(model, bg);
        }

        for (MoveableCube cube: World.level.moveableCubes) {
            drawCube(cube, bg);
            //drawLine(cf(cube.getPosition()), cf(new Point(cube.getVelocity()).add(cube.getVelocity())), bg);
        }

        bg.setColor(new Color(123,78,23));
        drawCube(player, bg);

        g.drawImage(buff, 0, 0, null);
    }


    public static void main(String[] args) {
        new Renderer().run();
    }
}
