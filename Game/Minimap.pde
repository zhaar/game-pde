class Minimap extends Surface {
  float ratio;
  PGraphics circle;
  
  Minimap(int w, int h) {
    super(w, h);
    ratio = BOX_DIMENSIONS/(float)w;
    circle = createGraphics(50, 50, P2D);
  }
  
  void drawCylinders(int x, int z) {
      draw(x, z);
  }
  
  void drawCircle(int x, int z) {
    circle.beginShape();
    //circle.background(255);
    circle.fill(255);
    circle.ellipse(30, 30, 10, 10);
    circle.endShape();
  }
  
  void draw(int x, int y) {
    drawCircle(x, y);
    image(circle, x, y);
  }
}
