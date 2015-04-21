package Game;

import processing.core.*;

class Minimap extends Surface {
  float ratio;
  PGraphics circle;
  
  Minimap(PApplet context, int w, int h) {
    super(context, w, h);
    ratio = Game.BOX_DIMENSIONS/(float)w;
    circle = ctx.createGraphics(50, 50, ctx.P2D);
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
    ctx.image(circle, x, y);
  }
}
