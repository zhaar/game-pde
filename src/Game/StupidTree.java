package Game;

import processing.core.*;

public class StupidTree extends Cylinder {

  PShape tree;

  public StupidTree(PApplet context, int X, int Z) {
    super(context, X, Z);
    this.cylinderBaseSize = 10;
    this.create();
  }

  void create() {
    tree = ctx.loadShape("stupidTree.obj");
    tree.scale(40);
  }

  void draw() {
    ctx.translate(X, 0, Z);
    ctx.shape(tree);
    ctx.translate(-X, 0, -Z);
  }
}

