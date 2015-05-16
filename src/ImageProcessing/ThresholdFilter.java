package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

public class ThresholdFilter {
    private final PApplet ctx;
    private final ThresholdFunction f;
    private final PImage source;
    private PImage cachedResult;

    public ThresholdFilter(PApplet context, ThresholdFunction f, PImage source) {
        this.ctx = context;
        this.f = f;
        this.source = source;
    }

    public PImage updateThresholdFunction(ThresholdFunction fn, PImage target) {
        if (fn.equals(f)) {
            return cachedResult == null ? applyFilter(target) : cachedResult;
        } else {
            return applyFilter(target);
        }
    }

    public PImage updateThresholdFunction(ThresholdFunction fn) {
        return updateThresholdFunction(fn, ctx.createImage(source.width, source.height, ctx.RGB));
    }

    public PImage applyFilter(PImage target) {
        source.loadPixels();
        for (int i = 0; i < source.width * source.height; i++) {
            int p = source.pixels[i];
            float brightness = ctx.brightness(p);
            target.pixels[i] = ctx.color(f.apply((int) brightness));
        }
        target.updatePixels();
        cachedResult = target;
        return target;
    }

    public PImage applyFilter() {
        return applyFilter(ctx.createImage(source.width, source.height, ctx.RGB));
    }

    public static ThresholdFunction binaryThreshold(final int threshold) {
        return pixelValue -> pixelValue < threshold ? 0 : 255;
    }

    public static ThresholdFunction invertedBinaryThreshold(final int threshold) {
        return pixelValue -> pixelValue > threshold ? 0 : 255;
    }

    public static ThresholdFunction truncateThreshold(final int threshold) {
        return pixelValue -> Math.min(threshold, pixelValue);
    }

    public static ThresholdFunction toZeroThreshold(final int threshold) {
        return pixelValue -> pixelValue > threshold ? pixelValue : 0;
    }

    public static ThresholdFunction invertedToZeroThreshold(final int threshold) {
        return pixelValue -> pixelValue < threshold ? pixelValue : 0;
    }

    @FunctionalInterface
    public interface ThresholdFunction {
        int apply(int pixelValue);
    }
}
