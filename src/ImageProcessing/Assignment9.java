package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

public class Assignment9 extends PApplet {

    private static final float phiStep = 0.06f;
    private static final float rStep = 2.5f;

    PImage hough;
    PImage sobel;
    int[] acc;
    Hough h;
    PImage img;

    public void setup() {
        size(1600, 600);
        img = loadImage("board1.jpg");
        sobel = ImageConvolution.sobel(img, this);
        h = new Hough(phiStep, rStep);
        acc = h.computeAccumulator(this, sobel);
        hough = Hough.drawAccumulator(this, acc, h.rDim(sobel), h.phiDim());
        noLoop();
    }

    public void draw() {
        image(img, 0, 0);
        image(hough, img.width, 0);
//        Hough.drawLinesFromAccumulator(this, acc, sobel.width, h.rDim(sobel), phiStep, rStep);
    }
}
