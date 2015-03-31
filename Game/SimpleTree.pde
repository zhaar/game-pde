class SimpleTree extends Cylinder{

  PShape tree;

  SimpleTree(int X, int Z) {
    this.X = X;
    this.Z = Z;
    this.create();
  }

  void create() {
    
    
    tree = loadShape("simpleTree.obj");
    tree.scale(40);

  }

  void draw() {
    translate(X, 0, Z);
    shape(tree);
    translate(-X, 0, -Z);
  }
}

