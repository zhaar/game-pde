class Surface {
  PGraphics bottomRect;
  
  Surface() {
    bottomRect = createGraphics(width, 100, P2D);
  }
  
  void drawSurface() {
    bottomRect.beginDraw();
    bottomRect.fill(0);
    //bottomRect.background(255, 255, 255);
    bottomRect.rect(0, 0, 700, 100);
    bottomRect.endDraw();
  }

  void draw() {
    drawSurface();
    image(bottomRect, 0, 0);
  }
}

