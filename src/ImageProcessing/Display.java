package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

public class Display extends PApplet {
    PImage img;
    public void setup() {
        size(800, 600);
        img = loadImage("board1.jpg");
        noLoop();
    }

    public void draw() {
        image(img, 0, 0);
    }
}

