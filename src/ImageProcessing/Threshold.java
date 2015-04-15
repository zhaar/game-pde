package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;


public class Threshold extends PApplet {

    PImage result;
    public void setup() {
        size(800, 600);
        PImage img = loadImage("board1.jpg");
        result = applyThreshold(img, createImage(img.width, img.height, RGB), binaryThreshold(128));
        noLoop();
    }

    public void draw() {
        image(result, 0, 0);
    }

    public PImage applyThreshold(PImage source, PImage target, ThresholdFunction f) {
        source.loadPixels();
        target.loadPixels();
        for(int i = 0; i < source.width * source.height; i++) {
            int p = source.pixels[i];
            float brightness = brightness(p);
            System.out.println("pixel value: " + p + ", red: " + red(p) + ", green: " + green(p) + ", blue: " + blue(p));
            System.out.println("brightness: " + brightness);
            target.pixels[i] = color(f.apply((int) brightness));
        }
        target.updatePixels();
        return target;
    }

    public ThresholdFunction binaryThreshold(int threshold) {
        return pixelValue ->  pixelValue < threshold ? 0 : 255 ;
    }

    public ThresholdFunction invertedBinaryThreshold(int threshold) {
        return pixelValue -> pixelValue > threshold ? 0 : 255;
    }

    public ThresholdFunction truncateThreshold(int threshold) {
        return pixelValue -> Math.min(threshold, pixelValue);
    }

    public ThresholdFunction toZeroThreshold(int threshold) {
        return pixelValue -> pixelValue > threshold ? pixelValue : 0;
    }

    public ThresholdFunction invertedToZeroThreshold(int threshold) {
        return pixelValue -> pixelValue < threshold ? pixelValue : 0;
    }

    public interface ThresholdFunction {
        int apply(int pixelValue);
    }
}
