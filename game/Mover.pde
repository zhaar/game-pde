abstract class Mover {
  int radius;
  PVector pos;
  PVector vel = new PVector(0, 0, 0);
  PVector friction;
  PVector gravityForce = new PVector(0, 0, 0);
  float coef = 0.6;
  
  void update() {
    gravityForce.x = sin(-radians(rz)) * gravityConstant;
    gravityForce.z = sin(radians(rx)) * gravityConstant;
    friction = vel.get();
    friction.mult(-1);
    friction.normalize();
    friction.mult(frictionMagnitude);
    vel.add(friction);
    vel.add(gravityForce);
    
    checkBounds();
    
    pos.add(vel);
  }
  
  abstract void checkBounds();
  abstract void checkCylinderCollision();
}
