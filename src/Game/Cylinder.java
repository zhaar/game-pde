package Game;

import processing.core.*;

public class Cylinder {
  float cylinderBaseSize = 50;
  float cylinderHeight = 50;
  int cylinderResolution = 40;
  int X;
  int Z;

  PShape openCylinder = new PShape();

  protected final PApplet ctx;

  public Cylinder(PApplet context, int X, int Z) {
    this.ctx = context;
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
      angle = (PApplet.TWO_PI / cylinderResolution) * i;
      x[i] = PApplet.sin(angle) * cylinderBaseSize;
      y[i] = PApplet.cos(angle) * cylinderBaseSize;
    }
    ctx.stroke(0);
    ctx.noFill();

    openCylinder = ctx.createShape(PApplet.GROUP);

    PShape side = ctx.createShape();
    side.beginShape(PApplet.QUAD_STRIP);
    //draw the border of the cylinder
    for (int i = 0; i < x.length; i++) {
      side.vertex(x[i], y[i], 0);
      side.vertex(x[i], y[i], cylinderHeight);
    }
    side.endShape();

    PShape bottom = ctx.createShape();
    bottom.beginShape(PApplet.TRIANGLE_FAN);
    bottom.vertex(0, 0, 0);
    for (int i = 0; i < x.length; i++) {
      bottom.vertex(x[i], y[i], 0);
    }
    bottom.endShape();

    PShape top = ctx.createShape();
    top.beginShape(PApplet.TRIANGLE_FAN);
    top.vertex(0, 0, cylinderHeight);
    for (int i = 0; i < x.length; i++) {
      top.vertex(x[i], y[i], cylinderHeight);
    }
    top.endShape();

    openCylinder.addChild(side);
    openCylinder.addChild(top);
    openCylinder.addChild(bottom);
    ctx.noStroke();
    ctx.fill(255);
  }

  void draw() {
    ctx.translate(X, 0, Z);
    ctx.rotateX(-PApplet.PI / 2);
    ctx.shape(openCylinder);
    ctx.rotateX(PApplet.PI / 2);
    ctx.translate(-X, 0, -Z);
  }
}

