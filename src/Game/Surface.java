package Game;

import processing.core.*;

public class Surface {

  private final PGraphics surface;
  protected final PApplet ctx;

  public Surface(PApplet context, int w, int h) {
    this.ctx = context;
    surface = ctx.createGraphics(w, h, ctx.P2D);
  }

  void drawSurface(int rgb) {
    surface.beginDraw();
    surface.background(rgb);
    surface.endDraw();
  }

  void draw(int x, int y, int rgb) {
    drawSurface(rgb);
    ctx.image(surface, x, y);
  }
}

