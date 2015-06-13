package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

public class GrossHough extends PApplet {

    PImage img;
    PImage hough;
    PImage sobel;

    public void setup() {

        size(800, 600);
        img = loadImage("board1.jpg");
        sobel = ImageConvolution.sobel(img,this);
        noLoop();
    }

    public void hough(PImage edgeImg) {
        float discretizationStepsPhi = 0.06f; float discretizationStepsR = 2.5f;
        // dimensions of the accumulator
        int phiDim = (int) (Math.PI / discretizationStepsPhi);
        int rDim = (int) (((edgeImg.width + edgeImg.height) * 2 + 1) / discretizationStepsR);
        int halfR = rDim >>> 1;
//        // our accumulator (with a 1 pix margin around)
        int[] accumulator = new int[(phiDim) * (rDim)];
        float[] sinTable = new float[phiDim];
        float[] cosTable = new float[phiDim];
        for(int angle = 0; angle < phiDim; ++angle) {
            sinTable[angle] = sin(PI * angle/phiDim);
            cosTable[angle] = cos(PI * angle/phiDim);
        }
        float rmax = 0;
// Fill the accumulator: on edge points (ie, white pixels of the edge // image), store all possible (r, phi) pairs describing lines going // through the point.
        for (int y = 0; y < edgeImg.height; y++) {
            for (int x = 0; x < edgeImg.width; x++) {
// Are we on an edge?
                if (brightness(edgeImg.pixels[y * edgeImg.width + x]) != 0) {
                    for (int angle = 0; angle < phiDim-2; ++angle) {
                        double r = cosTable[angle] * x + sinTable[angle] * y;
//                        int rScaled = (int)Math.round(r * halfRAxisSize / maxRadius) + halfRAxisSize;
//                        float r = x * cosTable[angle] + y * sinTable[angle];
                        int normalized = (int) Math.round((r/2 + rDim/2));
                        rmax = max(rmax, normalized);
                        accumulator[angle * rDim + normalized]+=1;
                    }
                }
            }
        }
        float hyp = sqrt(width * width + height * height);
        println("rmax = " + rmax);
        println("hyp = " + hyp);
        println("rdim = " + rDim);
//        int[] accumulator = new Hough(discretizationStepsR, discretizationStepsPhi).computeAccumulator(this, sobel).dataArray;

        hough = createImage(rDim, phiDim, ALPHA);
        for (int i = 0; i < accumulator.length; i++) {
            hough.pixels[i] = color(min(255, accumulator[i]));
        }
        hough.updatePixels();
        hough.resize(800,600);

        for (int idx = 0; idx < accumulator.length; idx++) {
            if (accumulator[idx] > 200) {
                int accPhi = (int) (idx / (rDim + 2)) - 1;
                int accR = idx - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * 0.5f) * discretizationStepsR;
                float phi = accPhi * discretizationStepsPhi;
                // Cartesian equation of a line: y = ax + b
                // in polar, y = (-cos(phi)/sin(phi))x + (r/sin(phi))
                // => y = 0 : x = r / cos(phi)
                // => x = 0 : y = r / sin(phi)
// compute the intersection of this line with the 4 borders of // the image
                int x0 = 0;
                int y0 = (int) (r / sin(phi));
                int x1 = (int) (r / cos(phi));
                int y1 = 0;
                int x2 = edgeImg.width;
                int y2 = (int) (-cos(phi) / sin(phi) * x2 + r / sin(phi)); int y3 = edgeImg.width;
                int x3 = (int) (-(y3 - r / sin(phi)) * (sin(phi) / cos(phi)));
                // Finally, plot the lines
                stroke(204,102,0); if (y0 > 0) {
                    if (x1 > 0)
                        line(x0, y0, x1, y1);
                    else if (y2 > 0)
                        line(x0, y0, x2, y2);
                    else
                        line(x0, y0, x3, y3);
                }
                else {
                    if (x1 > 0) {
                        if (y2 > 0)
                            line(x1, y1, x2, y2); else
                            line(x1, y1, x3, y3);
                    }
                    else
                        line(x2, y2, x3, y3);
                }
            }
        }
    }

    public void draw() {
        image(img, 0, 0);
        hough(sobel);
        image(hough, 0, 0);

    }
}
