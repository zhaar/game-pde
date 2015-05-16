package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

import static ImageProcessing.Utils.*;
import static ImageProcessing.Utils.goesThrough;
import static processing.core.PApplet.*;

public class Hough {

    private static final int ACCUMULATOR_THRESHOLD = 200;
    private final float phiStep;
    private final float rStep;

    public Hough(float phiStep, float rStep) {
        this.phiStep = phiStep;
        this.rStep = rStep;
    }

    public ArrayData computeAccumulator(PApplet ctx, PImage source) {
        int width = source.width;
        int height = source.height;
        int phiMax = phiDim();
        int rMax = rDim(source);

        ArrayData acc = new ArrayData(phiMax + 2, rMax + 2);
        float minr = 0;
        float maxr = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (ctx.brightness(source.pixels[y * source.width + x]) != 0) {
                    for (int angle = 0; angle < phiMax; ++angle) {
                        float r = x * PApplet.cos(angle) + y * PApplet.sin(angle);
                        int normalized = Math.round((r + acc.height) / 2);
                        minr = Math.min(normalized, minr);
                        maxr = Math.max(normalized, maxr);
                        acc.accumulate(angle, normalized, 1);
                    }
                }
            }
        }
        System.out.println("maximum radius " + rMax);
        System.out.println("minr: " + minr);
        System.out.println("maxr: " + maxr);
        return acc;
    }

    public int phiDim() {
        return (int) (Math.PI / phiStep);
    }

    public int rDim(PImage img) {
        return (int) (((img.width + img.height) * 2 + 1) / rStep);
    }

    public static PImage drawAccumulator(PApplet ctx, int[] acc, int rDim, int phiDim) {
        System.out.println("drawing accumulator image of size " + rDim + " , ");
        PImage houghImg = ctx.createImage(rDim + 2, phiDim + 2, ctx.ALPHA);
        for (int i = 0; i < acc.length; i++) {
            houghImg.pixels[i] = ctx.color(ctx.min(255, acc[i]));
        }
        houghImg.updatePixels();
        return houghImg;
    }

    public static PImage drawAccumulator(PApplet ctx, ArrayData acc) {
        PImage houghImg = ctx.createImage(acc.width, acc.height, ctx.ALPHA);
        for (int i = 0; i < acc.dataArray.length; i++) {
            houghImg.pixels[i] = ctx.color(ctx.min(255, acc.dataArray[i]));
        }
        houghImg.updatePixels();
        return houghImg;
    }

    public static void drawLinesFromAccumulator(PApplet ctx, int[] acc, int width,
                                                 int rDim, float phiStep, float rStep) {
        int size = acc.length;
        for (int i = 0; i < size; ++i) {
            if (acc[i] > ACCUMULATOR_THRESHOLD) {

                int accPhi = (int) (i / (rDim + 2)) - 1;
                int accR = i - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * 0.5f) * rStep;
                float phi = accPhi * phiStep;
                int x0 = 0;
                int y0 = (int) (r / sin(phi));
                int x1 = (int) (r / cos(phi));
                int y1 = 0;
                int x2 = width;
                int y2 = (int) (-cos(phi) / sin(phi) * x2 + r / sin(phi));
                int y3 = width;
                int x3 = (int) (-(y3 - r / sin(phi)) * (sin(phi) / cos(phi)));

                ctx.stroke(204, 102, 0);
                if (y0 > 0) {
                    if (x1 > 0)
                        ctx.line(x0, y0, x1, y1);
                    else if (y2 > 0)
                        ctx.line(x0, y0, x2, y2);
                    else
                        ctx.line(x0, y0, x3, y3);
                } else {
                    if (x1 > 0) {
                        if (y2 > 0) {
                            ctx.line(x1, y1, x2, y2);
                        } else {
                            ctx.line(x1, y1, x3, y3);
                        }
                    } else {
                        ctx.line(x2, y2, x3, y3);
                    }
                }
            }
        }
    }
}
