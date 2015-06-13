package ImageProcessing.GenericProcess;

import processing.core.PApplet;

public class FilterProcess extends GenericImageProcess {

    public FilterProcess(PApplet context, ThresholdFunction fn) {
        super(context, ((pixel, source, index) -> fn.apply(context, pixel)));
    }

    public static PixelFunction hue(final PApplet ctx) {
        return pixel -> (int) ctx.hue(pixel);
    }

    public static PixelFunction brightness(final PApplet ctx) {
        return pixel -> (int) ctx.brightness(pixel);
    }

    public static FilterProcess hueBinaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, binaryThresholdFunction(value, hue(context)));
    }

    public static FilterProcess hueInvertedBinaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, invertedBinaryThresholdFunction(value, hue(context)));
    }

    public static FilterProcess hueTruncateThreshold(PApplet context, int value) {
        return new FilterProcess(context, truncateThresholdFunction(value, hue(context)));
    }

    public static FilterProcess hueToZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, toZeroThresholdFunction(value, hue(context)));
    }

    public static FilterProcess hueInvertedToZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, invertedToZeroThresholdFunction(value, hue(context)));
    }

    public static FilterProcess brightnessBinaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, binaryThresholdFunction(value, brightness(context)));
    }

    public static FilterProcess brightnessInvertedBinaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, invertedBinaryThresholdFunction(value, brightness(context)));
    }

    public static FilterProcess brightnessTruncateThreshold(PApplet context, int value) {
        return new FilterProcess(context, truncateThresholdFunction(value, brightness(context)));
    }

    public static FilterProcess brightnessToZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, toZeroThresholdFunction(value, brightness(context)));
    }

    public static FilterProcess brightnessInvertedToZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, invertedToZeroThresholdFunction(value, brightness(context)));
    }

    private static ThresholdFunction binaryThresholdFunction(final int threshold, PixelFunction pixel) {
        return (ctx, pixelValue) -> (pixel.apply(pixelValue) < threshold) ? 0 : 255;
    }

    private static ThresholdFunction invertedBinaryThresholdFunction(final int threshold, PixelFunction pixel) {
        return (ctx, pixelValue) -> pixel.apply(pixelValue) > threshold ? 0 : 255;
    }

    private static ThresholdFunction truncateThresholdFunction(final int threshold, PixelFunction pixel) {
        return (ctx, pixelValue) -> Math.min((int) pixel.apply(threshold), pixelValue);
    }

    private static ThresholdFunction toZeroThresholdFunction(final int threshold, PixelFunction pixel) {
        return (ctx, pixelValue) -> pixel.apply(pixelValue) > threshold ? pixelValue : 0;
    }

    private static ThresholdFunction invertedToZeroThresholdFunction(final int threshold, PixelFunction pixel) {
        return (ctx, pixelValue) -> pixel.apply(pixelValue) < threshold ? pixelValue : 0;
    }

    public interface PixelFunction {
        int apply(int pixel);
    }

    @FunctionalInterface
    public interface ThresholdFunction {
        int apply(PApplet context, int pixelValue);
    }
}
