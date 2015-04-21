package Game;

import processing.core.*;

public class Ball extends Mover {

  private final Game ctx;
  private int radius;
  private PVector pos;

  public Ball(Game context, int radius) {
    this.ctx = context;
    this.radius = radius;
    this.pos = new PVector(0, radius + 2.5f, 0);
  }

  void checkBounds(){
    if (pos.x > Game.BOX_DIMENSIONS/2) {
      vel.x = -coef*vel.x;
      pos.x = Game.BOX_DIMENSIONS/2;
    } else if (pos.x < -Game.BOX_DIMENSIONS/2) {
      vel.x = -coef*vel.x;
      pos.x = -Game.BOX_DIMENSIONS/2;
    }
    if (pos.z > Game.BOX_DIMENSIONS/2) {
      vel.z = -coef*vel.z;
      pos.z = Game.BOX_DIMENSIONS/2;
    } else if (pos.z < -Game.BOX_DIMENSIONS/2) {
      vel.z = -coef*vel.z;
      pos.z = -Game.BOX_DIMENSIONS/2;
    }
  }
  
  public void checkCylinderCollision() {
    PVector vel2;
    for (Cylinder c : ctx.cylinders) {
      float minDistance = radius + c.cylinderBaseSize;
      float distanceX = pos.x - c.X;
      float distanceZ = pos.z - c.Z;
      PVector n = new PVector(distanceX, 0, distanceZ);
      PVector nSave;
      n.normalize();
      nSave = new PVector(n.x, n.y, n.z);
      
      if (ctx.sqrt(distanceX * distanceX + distanceZ * distanceZ) < minDistance){
        n.mult(2*vel.dot(n));
        vel.sub(n);
        int cylX = c.X;
        int cylZ = c.Z;
        nSave.mult(c.cylinderBaseSize + radius);
        pos.x = cylX + nSave.x;
        pos.z = cylZ + nSave.z;
      }
    }
  }

  public void draw() {
    ctx.translate(pos.x, pos.y, pos.z);
    ctx.sphere(radius);
  }
}
