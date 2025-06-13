package axies;

public class Camera{
   public Vector2D pos;
   public VectorMD rot;
   public double zoom = 1.0;
   public Camera(Vector2D pos, VectorMD rot){
      this.pos = pos;
      this.rot = rot;
   }
   public void move(boolean u, boolean l, boolean d, boolean r, boolean q, boolean e, boolean i, boolean o,  double movespeed, double rotspeed, double zoomspeed){
      Vector2D movement = new Vector2D(0,0);
      if(u) movement = movement.add(new Vector2D(0,-1));
      if(l) movement = movement.add(new Vector2D(-1,0));
      if(d) movement = movement.add(new Vector2D(0,1));
      if(r) movement = movement.add(new Vector2D(1,0));
      VectorMD rotation = new VectorMD(1,0);
      if(q) rotation = rotation.addD(new VectorMD(1,rotspeed));
      if(e) rotation = rotation.addD(new VectorMD(1,-rotspeed));
      pos = pos.add(movement.normal().convert().addD(rot).mult(movespeed*zoom).convert());
      rot = rot.addD(rotation);
      if(i) zoom*=1/zoomspeed;
      if(o) zoom*=zoomspeed;
   }
}