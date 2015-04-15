package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Utility class needs a constructor to get a draw context
 */
public final class ImageUtils {
    private final PApplet ctx;

    public ImageUtils(PApplet context) {
        this.ctx = context;
    }

    public PImage applyThreshold(PImage source, PImage target, ThresholdFunction f) {
        source.loadPixels();
        for(int i = 0; i < source.width * source.height; i++) {
            int p = source.pixels[i];
            float brightness = ctx.brightness(p);
            target.pixels[i] = ctx.color(f.apply((int) brightness));
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
