package ImageProcessing.GenericProcess;

import processing.core.PApplet;
import processing.core.PImage;
import sun.net.www.content.text.Generic;

import java.util.ArrayList;

import static processing.core.PConstants.RGB;

public class ProcessFromImage<T> implements GenericProcess<PImage, T>{

    protected final PApplet ctx;
    private final PixelFunction fn;
    private final InitialValueGenerator<T, PImage> initalValueGenerator;
    private final Combinator<T, Integer> combinator;

    public ProcessFromImage(PApplet context, PixelFunction pixelFunction, InitialValueGenerator<T, PImage> init, Combinator<T, Integer> combinator) {
        this.ctx = context;
        this.fn = pixelFunction;
        this.combinator = combinator;
        initalValueGenerator = init;
    }

    public T mutableCompute(PImage source, T acc) {
        source.loadPixels();
//        int size = source.width * source.height;
        int nbThreads = 16;
        ArrayList<Thread> threads = new ArrayList<>(nbThreads);
        for (int i = 0; i < nbThreads; ++i) {
            int beg = i/nbThreads * source.pixels.length;
            int end = (i+1)/nbThreads * source.pixels.length;
            threads.add(new Thread(processFraction(beg, end, source, acc)));
        }
//        target.updatePixels();
        return acc;
    }

    private Runnable processFraction(int beginning, int end, PImage source, T target) {
        return () -> {
            for (int i = beginning; i < end; i++) {
                int pixel = source.pixels[i];
                combinator.combo(target, fn.computePixel(pixel, source, i), i);
//                accumulator.accumulate(acc, fn.computePixel(pixel, source, i), i);
//                target.pixels[i] = ctx.color(fn.computePixel(pixel, source, i));
            }
        };
    }

    public T compute(PImage source) {
        return mutableCompute(source, initalValueGenerator.generate(source));
    }

    @FunctionalInterface
    public interface PixelFunction {
        Integer computePixel(Integer pixel, PImage source, int index);
    }

    @FunctionalInterface
    public interface InitialValueGenerator<T, K> {
        T generate(K arg);
    }

    @FunctionalInterface
    public interface Combinator<T, K> {
        void combo(T acc, K value, int index);
    }
}
