class Ball extends Mover {
  Ball(int radius) {
    this.radius = radius;
    this.pos = new PVector(0, radius + 2.5, 0);
  }

  void checkBounds(){
    if (pos.x > BOX_DIMENSIONS/2) {
      vel.x = -coef*vel.x;
      pos.x = BOX_DIMENSIONS/2;
    } else if (pos.x < -BOX_DIMENSIONS/2) {
      vel.x = -coef*vel.x;
      pos.x = -BOX_DIMENSIONS/2;
    }
    if (pos.z > BOX_DIMENSIONS/2) {
      vel.z = -coef*vel.z;
      pos.z = BOX_DIMENSIONS/2;
    } else if (pos.z < -BOX_DIMENSIONS/2) {
      vel.z = -coef*vel.z;
      pos.z = -BOX_DIMENSIONS/2;
    }
  }
  
  void checkCylinderCollision() {
    PVector vel2;
    for (int i = 0; i < cylinders.size(); i++) {
      float minDistance = radius + cylinders.get(i).cylinderBaseSize;
      float distanceX = pos.x - cylinders.get(i).X;
      float distanceZ = pos.z - cylinders.get(i).Z;
      PVector n = new PVector(distanceX, 0, distanceZ);
      PVector nSave;
      n.normalize();
      nSave = new PVector(n.x, n.y, n.z);
      
      if (sqrt(distanceX*distanceX + distanceZ*distanceZ) < minDistance){
        n.mult(2*vel.dot(n));
        vel.sub(n);
        int cylX = cylinders.get(i).X;
        int cylZ = cylinders.get(i).Z;
        nSave.mult(cylinders.get(i).cylinderBaseSize + radius);
        pos.x = cylX + nSave.x;
        pos.z = cylZ + nSave.z;
      }
    }
  }

  void draw() {
    translate(pos.x, pos.y, pos.z);
    sphere(radius);
  }
}
