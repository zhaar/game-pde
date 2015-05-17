package ImageProcessing.GenericProcess;

import processing.core.PApplet;

import java.util.function.Predicate;

public class FilterProcess extends GenericImageProcess {

    public FilterProcess(PApplet context, ThresholdFunction fn) {
        super(context, ((pixel, source, index) -> fn.apply(context, pixel)));
    }

    public static FilterProcess binaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, binaryThresholdFunction(value));
    }

    public static FilterProcess invertedBinaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, invertedBinaryThresholdFunction(value));
    }

    public static FilterProcess truncateThreshold(PApplet context, int value) {
        return new FilterProcess(context, truncateThresholdFunction(value));
    }

    public static FilterProcess toZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, toZeroThresholdFunction(value));
    }

    public static FilterProcess invertedToZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, invertedToZeroThresholdFunction(value));
    }


    private static ThresholdFunction binaryThresholdFunction(final int threshold) {
        return (ctx, pixelValue) -> (ctx.brightness(pixelValue) < threshold) ? 0 : 255;
    }

    private static ThresholdFunction invertedBinaryThresholdFunction(final int threshold) {
        return (ctx, pixelValue) -> ctx.brightness(pixelValue) > threshold ? 0 : 255;
    }

    private static ThresholdFunction truncateThresholdFunction(final int threshold) {
        return (ctx, pixelValue) -> Math.min(threshold, pixelValue);
    }

    private static ThresholdFunction toZeroThresholdFunction(final int threshold) {
        return (ctx, pixelValue) -> ctx.brightness(pixelValue) > threshold ? pixelValue : 0;
    }

    private static ThresholdFunction invertedToZeroThresholdFunction( final int threshold) {
        return (ctx, pixelValue) -> ctx.brightness(pixelValue) < threshold ? pixelValue : 0;
    }

    @FunctionalInterface
    public interface ThresholdFunction {
        int apply(PApplet context, int pixelValue);
    }
}
