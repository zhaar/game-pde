class StupidTree extends Cylinder{
  PShape tree;

  StupidTree(int X, int Z) {
    this.X = X;
    this.Z = Z;
    this.cylinderBaseSize = 10;
    this.create();
  }

  void create() {
    tree = loadShape("stupidTree.obj");
    tree.scale(40);

  }

  void draw() {
    translate(X, 0, Z);
    shape(tree);
    translate(-X, 0, -Z);
  }
}

