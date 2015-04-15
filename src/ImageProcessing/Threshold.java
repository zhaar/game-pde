package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;


public class Threshold extends PApplet {

    private final ImageUtils utils = new ImageUtils(this);
    PImage result;
    public void setup() {
        size(800, 600);
        PImage img = loadImage("board1.jpg");
        result = utils.applyThreshold(img, createImage(img.width, img.height, RGB), utils.binaryThreshold(128));
        noLoop();
    }

    public void draw() {
        image(result, 0, 0);
    }
}
