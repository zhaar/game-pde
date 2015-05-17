package ImageProcessing.GenericProcess;

import ImageProcessing.Hough;
import ImageProcessing.Utils;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;

public class HoughLinesProcess implements GenericProcess {

    private final PApplet ctx;
    private final Hough hough;
    private final int minVotes;

    public HoughLinesProcess(PApplet context, float rStep, float phiStep, int minVotes) {
        this.ctx = context;
        this.hough = new Hough(rStep, phiStep);
        this.minVotes = minVotes;
    }

    @Override
    public PImage mutableCompute(PImage source, PImage target) {
        Utils.ArrayData accumulator = hough.computeAccumulator(ctx, source);
        List<Utils.Pair<Integer, Integer>>  lines = Hough.improvedCandidates(accumulator, minVotes);
        List<Utils.Pair<Integer, Integer>> sortedLines = Hough.sortAndTake(lines, 100);

        return null;
    }

    @Override
    public PImage immutableCompute(PImage source) {
        return null;
    }
}
