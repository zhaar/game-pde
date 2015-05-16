package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

public class Assignment9 extends PApplet {

    PImage hough;
    PImage sobel;
    int[] acc;
    Hough h;

    public void setup() {
        size(800, 600);
        PImage img = loadImage("board1.jpg");
        sobel = ImageConvolution.sobel(img, this);
        h = new Hough(0.06f, 2.5f);
        acc = h.computeAccumulator(this, sobel);
        hough = Hough.drawAccumulator(this, acc, h.rDim(sobel), h.phiDim());

        noLoop();
    }

    public void draw() {
        image(sobel, 0, 0);
        Hough.drawAccumulator(this, acc, h.rDim(sobel), h.phiDim());
    }
}
