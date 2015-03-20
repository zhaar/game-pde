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

  void draw() {
    translate(pos.x, pos.y, pos.z);
    sphere(radius);
  }
}
