package Game;

import processing.core.*;

class SimpleTree extends Cylinder{

  PShape tree;

  public SimpleTree(PApplet context, int X, int Z) {
    super(context, X, Z);
    this.create();
  }

  void create() {
    tree = ctx.loadShape("simpleTree.obj");
    tree.scale(40);
  }

  void draw() {
    ctx.translate(X, 0, Z);
    ctx.shape(tree);
    ctx.translate(-X, 0, -Z);
  }
}

