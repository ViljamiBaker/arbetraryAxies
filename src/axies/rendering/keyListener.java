package axies.rendering;

import java.util.HashSet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyListener implements KeyListener{
   HashSet<Integer> keysHeld = new HashSet<>();
   HashSet<Integer> keysHeld2 = new HashSet<>();
   public keyListener(){}
   @Override
   public void keyPressed(KeyEvent e){
      keysHeld.add(e.getKeyCode());
   }
   @Override
   public void keyReleased(KeyEvent e){
      keysHeld.remove(e.getKeyCode());
   }
   @Override
   public void keyTyped(KeyEvent e){}
   
   public boolean isKeyDown(int key){
      return keysHeld.contains(key);
   }

   public boolean isKeyPressed(int key){
      return (keysHeld.contains(key)&&!keysHeld2.contains(key));
   }

   public void update(){
      keysHeld2.clear();
      keysHeld2.addAll(keysHeld);
   }
}