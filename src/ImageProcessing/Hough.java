package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

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

    public int[] computeAccumulator(PApplet ctx, PImage source) {
        int width = source.width;
        int height = source.height;
        int phiDim = phiDim();
        int rDim = rDim(source);
        int halfR = rDim / 2;
        int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];

        double[] sinTable = new double[phiDim];
        double[] cosTable = new double[phiDim];
        //juicy performances
        for (int theta = phiDim - 1; theta >= 0; theta--)
        {
            double thetaRadians = theta * Math.PI / phiDim;
            sinTable[theta] = Math.sin(thetaRadians);
            cosTable[theta] = Math.cos(thetaRadians);
        }

        ctx.println("rDim: " + rDim);
        ctx.println("phiDim: " + phiDim);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (ctx.brightness(source.pixels[y * width + x]) != 0) {
                    for (int theta = phiDim - 1; theta >= 0; theta--){
                        double r = cosTable[theta] * x + sinTable[theta] * y;
                        int rScaled = (int)Math.round(r * halfR / rDim) + halfR;
                        accumulator[theta * phiDim + rScaled]++;
                    }

//                    for (int phi = 0; phi < phiDim; ++phi) {
//                        for (int r = 0; r < rDim; ++r) {
//                            if (goesThrough(x,y,r,phi)) {
//                                accumulator[phi * phiDim + r]++;
//                            }
//                        }
//                    }
                    // ...determine here all the lines (r, phi) passing through
                    // pixel (x,y), convert (r,phi) to coordinates in the
                    // accumulator, and increment accordingly the accumulator.
                }
            }
        }
//        for (int phi = 0; phi < phiDim; ++phi) {
//            for (int r = 0; r < rDim; ++r) {
//                ctx.print(accumulator[phi * phiDim + r] + " ");
//            }
//            ctx.println("\n");
//        }

        return accumulator;
    }

    public int phiDim() {
        return (int) (2* Math.PI / phiStep);
    }

    public int rDim(PImage img) {
        return (int) (((img.width + img.height) * 2 + 1) / rStep);
    }

    public static PImage drawAccumulator(PApplet ctx, int[] acc, int rDim, int phiDim) {
        PImage houghImg = ctx.createImage(rDim + 2, phiDim + 2, ctx.ALPHA);
        for (int i = 0; i < acc.length; i++) {
            houghImg.pixels[i] = ctx.color(ctx.min(255, acc[i]));
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
