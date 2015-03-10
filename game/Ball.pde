class Ball {
  int radius;
  PVector pos;
  PVector vel = new PVector(0, 0, 0);
  PVector friction;
  PVector gravityForce = new PVector(0, 0, 0);

  Ball(int radius) {
    this.radius = radius;
    this.pos = new PVector(0, radius + 2.5, 0);
  }

  void update() {
    gravityForce.x = sin(-radians(rz)) * gravityConstant;
    gravityForce.z = sin(radians(rx)) * gravityConstant;
    friction = vel.get();
    friction.mult(-1);
    friction.normalize();
    friction.mult(frictionMagnitude);
    vel.add(friction);
    vel.add(gravityForce);
    
    if (pos.x > BOX_DIMENSIONS/2 || pos.x < -BOX_DIMENSIONS/2) {
      vel.x = -vel.x;
    }
    if (pos.z > BOX_DIMENSIONS/2 || pos.z < -BOX_DIMENSIONS/2) {
      vel.z = -vel.z;
    }
    
    pos.add(vel);
  }

  void draw() {
    translate(pos.x, pos.y, pos.z);
    sphere(radius);
  }
}
