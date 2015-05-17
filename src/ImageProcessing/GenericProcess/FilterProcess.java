package ImageProcessing.GenericProcess;

import processing.core.PApplet;

import java.util.function.Predicate;

public class FilterProcess extends GenetricImageProcess{

    public FilterProcess(PApplet context, Predicate<Integer> filterfunction, ThresholdFilter.ThresholdFunction fn) {
        super(context, (pixel, source, index) -> {
            if (filterfunction.test(pixel)) {
                return fn.apply(pixel);
            } else {
                return pixel;
            });
    }

    public FilterProcess binaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, ThresholdFilter.binaryThreshold(value));
    }

    public FilterProcess invertedBinaryThreshold(PApplet context, int value) {
        return new FilterProcess(context, ThresholdFilter.invertedBinaryThreshold(value));
    }

    public FilterProcess truncateThreshold(PApplet context, int value) {
        return new FilterProcess(context, ThresholdFilter.truncateThreshold(value));
    }

    public FilterProcess toZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, ThresholdFilter.toZeroThreshold(value));
    }

    public FilterProcess invertedToZeroThreshold(PApplet context, int value) {
        return new FilterProcess(context, ThresholdFilter.invertedToZeroThreshold(value));
    }
}
