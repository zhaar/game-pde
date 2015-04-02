class Surface {
  PGraphics surface;
  
  Surface(int w, int h) {
    surface = createGraphics(w, h, P2D);
  }
  
  void drawSurface(int rgb) {
    surface.beginDraw();
    surface.background(rgb);
    surface.endDraw();
  }

  void draw(int x, int y, int rgb) {
    drawSurface(rgb);
    image(surface, x, y);
  }
}

