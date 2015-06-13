package ImageProcessing.GenericProcess;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class HoughAccumulatorProcess extends ProcessFromImage<ArrayList<Integer>> {

//    private final Hough hough;
    private final int minVotes;

    public HoughAccumulatorProcess(PApplet context, float rStep, float phiStep, int minVotes) {
        super(context, new PixelFunction() {
            @Override
            public Integer computePixel(Integer pixel, PImage source, int index) {
                return null;
            }
        }, new InitialValueGenerator<ArrayList<Integer>, PImage>() {
            @Override
            public ArrayList<Integer> generate(PImage arg) {
                return null;
            }
        }, new Combinator<ArrayList<Integer>, Integer>() {
            @Override
            public void combo(ArrayList<Integer> acc, Integer value, int index) {
                acc.set(index, acc.get(index) + 1);
            }
        });
//        super(context, (pixel, source, index) -> 0);
//        this.hough = new Hough(rStep, phiStep);
        this.minVotes = minVotes;
    }


    @Override
    public ArrayList<Integer> compute(PImage source) {
        return null;
    }
}
