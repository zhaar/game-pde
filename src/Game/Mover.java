package Game;

import processing.core.*;

public abstract class Mover {
  int radius;
  PVector pos = new PVector(0, 0, 0);;
  PVector vel = new PVector(0, 0, 0);
  PVector friction;
  PVector gravityForce = new PVector(0, 0, 0);
  float coef = 0.6f;

  void update(float frictionMagnitude, float rx, float rz) {
    gravityForce.x = PApplet.sin(-PApplet.radians(rz)) * Game.gravityConstant;
    gravityForce.z = PApplet.sin(PApplet.radians(rx)) * Game.gravityConstant;
    friction = vel.get();
    friction.mult(-1);
    friction.normalize();
    friction.mult(frictionMagnitude);
    vel.add(friction);
    vel.add(gravityForce);
    
    checkBounds();
    checkCylinderCollision();
    
    pos.add(vel);
  }
  
  abstract void checkBounds();
  abstract void checkCylinderCollision();
}
