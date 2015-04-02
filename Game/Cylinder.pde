class Cylinder {
  float cylinderBaseSize = 50;
  float cylinderHeight = 50;
  int cylinderResolution = 40;
  int X;
  int Z;

  PShape openCylinder = new PShape();

  Cylinder() {}

  Cylinder(int X, int Z) {
    this.X = X;
    this.Z = Z;
    this.create();
  }

  void create() {
    float angle;
    float[] x = new float[cylinderResolution + 1];
    float[] y = new float[cylinderResolution + 1];

    //get the x and y position on a circle for all the sides
    for (int i = 0; i < x.length; i++) {
      angle = (TWO_PI / cylinderResolution) * i;
      x[i] = sin(angle) * cylinderBaseSize;
      y[i] = cos(angle) * cylinderBaseSize;
    }
    stroke(0);
    noFill();

    openCylinder = createShape(GROUP);

    PShape side = createShape();
    side.beginShape(QUAD_STRIP);
    //draw the border of the cylinder
    for (int i = 0; i < x.length; i++) {
      side.vertex(x[i], y[i], 0);
      side.vertex(x[i], y[i], cylinderHeight);
    }
    side.endShape();

    PShape bottom = createShape();
    bottom.beginShape(TRIANGLE_FAN);
    bottom.vertex(0, 0, 0);
    for (int i = 0; i < x.length; i++) {
      bottom.vertex(x[i], y[i], 0);
    }
    bottom.endShape();

    PShape top = createShape();
    top.beginShape(TRIANGLE_FAN);
    top.vertex(0, 0, cylinderHeight);
    for (int i = 0; i < x.length; i++) {
      top.vertex(x[i], y[i], cylinderHeight);
    }
    top.endShape();

    openCylinder.addChild(side);
    openCylinder.addChild(top);
    openCylinder.addChild(bottom);
    noStroke();
    fill(255);
  }

  void draw() {
    translate(X, 0, Z);
    rotateX(-PI/2);
    shape(openCylinder);
    rotateX(PI/2);
    translate(-X, 0, -Z);
  }
}

