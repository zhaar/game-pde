package ImageProcessing.GenericProcess;

import processing.core.PApplet;
import processing.core.PImage;

import static processing.core.PConstants.*;

public class GenericImageProcess {

    private final PApplet ctx;
    private final PixelFunction fn;

    public GenericImageProcess(PApplet context, PixelFunction pixelFunction) {
        this.ctx = context;
        this.fn = pixelFunction;
    }

    public PImage mutableCompute(PImage source, PImage target) {
        source.loadPixels();
        int size = source.width * source.height;
        for (int i = 0; i < size; i++) {
            int pixel = source.pixels[i];
            target.pixels[i] = ctx.color(fn.computePixel(pixel, source, i));
        }
        target.updatePixels();
        return target;
    }

    public PImage immutableCompte(PImage source) {
        return mutableCompute(source, ctx.createImage(source.width, source.height, RGB));
    }

    @FunctionalInterface
    public interface PixelFunction {
        Integer computePixel(Integer pixel, PImage source, int index);
    }
}
