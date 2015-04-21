package ImageProcessing;

import UI.HScrollbar;
import processing.core.PApplet;
import processing.core.PImage;


public class Threshold extends PApplet {

    private final ImageUtils utils = new ImageUtils(this);
    private HScrollbar scrollbar;
    private PImage result;
    private ThresholdFilter filter;

    public void setup() {
        size(800, 600);
        scrollbar = new HScrollbar(this, 0, 580, 800, 20);
        PImage source = loadImage("board1.jpg");
        filter = new ThresholdFilter(this, ThresholdFilter.binaryThreshold(128), source);
        result = filter.applyFilter();
    }

    public void draw() {
        background(color(0, 0, 0));

        result = filter.updateThresholdFunction(ThresholdFilter.binaryThreshold((int) (scrollbar.getPos() * 255)));
        image(result, 0, 0);
        scrollbar.display();
        scrollbar.update();
    }
}
