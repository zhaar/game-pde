package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static ImageProcessing.Utils.*;
import static processing.core.PApplet.*;

public class Hough {

    //Minimum number of pixels a line must go through to make the cut
    private static final int ACCUMULATOR_THRESHOLD = 200;
    private final float phiStep;
    private final float rStep;

    public Hough(float rStep, float phiStep) {
        this.phiStep = phiStep;
        this.rStep = rStep;
    }

    public int phiDim() {
        return (int) (Math.PI / phiStep);
    }

    public int rDim(PImage img) {
        return (int)Math.ceil(Math.hypot(img.width, img.height));
    }

    /**
     * Accumulator produces an array of the size r * phi.<br/>
     * The r axis ranges from -rMax to + rMax and is normalized to <br/>
     * 0 to 2rMax. Angle goes from 0 to PI
     * @param ctx drawing context
     * @param source source image
     * @return an array with signature array[radius][angle] = votes
     */
    public ArrayData computeAccumulator(PApplet ctx, PImage source) {
        int width = source.width;
        int height = source.height;
        int phiMax = phiDim();
        int rMax = rDim(source);

        ArrayData acc = new ArrayData(rMax, phiMax);

        float[] sinTable = new float[phiMax];
        float[] cosTable = new float[phiMax];
        for(int angle = 0; angle < phiMax; ++angle) {
            sinTable[angle] = sin(PI * angle/phiMax);
            cosTable[angle] = cos(PI * angle/phiMax);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (ctx.brightness(source.pixels[y * source.width + x]) != 0) {
                    for (int angle = 0; angle < phiMax; ++angle) {
                        float r = x * cosTable[angle] + y * sinTable[angle];
                        int normalized = Math.round((r/2 + acc.radius/2));
                        acc.accumulate(normalized, angle, 1);
                    }
                }
            }
        }
        return acc;
    }

    /**
     * Converts a dataArray as a list of Pairs of type (radius, angle)
     * @param arr
     * @return
     */
    public static List<Pair<Float, Float>> dataAsPairs(ArrayData arr) {
        ArrayList<Pair<Float, Float>> vectors = new ArrayList<>(arr.dataArray.length);
        for (int radius = 0; radius < arr.radius; ++radius) {
            for (int angle = 0; angle < arr.angle; ++angle) {
                vectors.add(new Pair<Float, Float>(radius - arr.radius/2f, PI/angle));
            }
        }
        return vectors;
    }

    /**
     * returns a list of vectors of form (radius, angle, votes)
     * @param arr data from hough accumulator
     * @return list of vectors for each line in the accumulator
     */
    public static List<PVector> dataAsVector(ArrayData arr) {
        ArrayList<PVector> vectors = new ArrayList<>(arr.dataArray.length);
        for (int radius = 0; radius < arr.radius; ++radius) {
            for (int angle = 0; angle < arr.angle; ++angle) {
                vectors.add(new PVector(angle/arr.angle * PI, 2*radius - arr.radius, arr.get(radius, angle)));
            }
        }
        return vectors;
    }

    /**
     * Converts a Pair of the form (accumulatorIndex, votes) to (radius, phi)
     * @param array
     * @param pair
     * @return
     */
    public static Pair<Float, Float> arrayIndexToPair(ArrayData array, Pair<Integer, Integer> pair, float rStep, float phiStep) {
        int index = pair._1;
        int accPhi = (index / array.radius) - 1;
        int accR = index - (accPhi + 1) * array.radius - 1;
        float r = (accR - (array.radius-3) * 0.5f) * rStep;
        float phi = accPhi * phiStep;
//        int phi = index % array.angle;
//        int radius = index/array.radius ;
        return new Pair<>(r, phi);
    }

    /**
     * computes the list of all intersection between the given lines
     * Lines must be in (radius, phi) form for this to work
     * @param lines list of lines computed from hough
     * @return list of intersections
     */
    public static List<Pair<Integer, Integer>> getIntersections(List<Pair<Float, Float>> lines) {
        ArrayList<Pair<Integer, Integer>> intersections = new ArrayList<>();

        for (int i = 0; i < lines.size() - 1; i++) {
            Pair<Float, Float> line1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Pair<Float, Float> line2 = lines.get(j);
                float r1 = line1.r;
                float r2 = line2.r;
                float phi1 = line1.phi;
                float phi2 = line2.phi;
                float d = cos(phi2) * sin(phi1) - cos(phi1) * sin(phi2);
                float x = r2 * sin(phi1) - r1 * sin(phi2);
                float y = -r2 * cos(phi1) + r1 * cos(phi2);
                intersections.add(new Pair<>(Math.round(x / d), Math.round(y / d)));
            }
        }
        return intersections;
    }

    public static List<Pair<Integer, Integer>> sortAndTake(List<Pair<Integer, Integer>> list, int count) {
        Collections.sort(list, (o1, o2) -> (o1._2 > o2._2 || o1._2 == o2._2 && o1._1 < o2._1) ? -1 : 1);
        return list.stream().limit(count).collect(Collectors.toList());
    }

    /**
     * Returns a list of pairs of a given length corresponding to the best candidates in the accumulator
     * @param lines the lines form the accumulator along with their vote count
     * @param minValue minimum values the candidates must conform to
     * @return a list of the best candidates as pairs of (accumulatorIndex, votes)
     */
    public static List<PVector> bestCandidates(List<PVector> lines, int minValue) {
        return lines.stream().filter(p -> p.z > minValue).collect(Collectors.toList());
    }

    /**
     * Improved version for picking best candidates, uses cluster approximation to detect nearby lines.
     * @param acc line accumulator
     * @param minVote minimum vote count each line must conform to
     * @return a list of the best candidates as pairs of (accumulatorIndex, votes)
     */
    public static List<Pair<Integer,Integer>> improvedCandidates(ArrayData acc, int minVote) {
        int neighboorhood = 10;
        ArrayList<Pair<Integer, Integer>> candidates = new ArrayList<>();
        for (int accR = 0; accR < acc.radius - 2; accR++) {
            for (int accPhi = 0; accPhi < acc.angle - 2; accPhi++) {

                int idx = (accPhi + 1) * (acc.radius) + accR + 1;

                if (acc.dataArray[idx] > minVote) {
                    boolean bestCandidate = true;
                    for (int dPhi = -neighboorhood / 2; dPhi < (neighboorhood / 2) + 1; dPhi++) {
                        if (accPhi + dPhi < 0 || accPhi + dPhi >= acc.angle) {
                            continue;
                        }
                        for (int dR = -neighboorhood / 2; dR < (neighboorhood / 2) + 1; dR++) {
                            if (accR + dR < 0 || accR + dR >= acc.radius) {
                                continue;
                            }
                            int neighbourIdx = (accPhi + dPhi + 1) * (acc.radius + 2) + accR + dR + 1;
                            if (acc.dataArray[idx] < acc.dataArray[neighbourIdx]) {
                                bestCandidate = false;
                                break;
                            }
                        }
                        if (!bestCandidate) {
                            break;
                        }
                    }
                    if (bestCandidate) {
                        candidates.add(new Pair<>(idx, acc.dataArray[idx]));
                    }
                }
            }
        }
        return candidates;
    }

    public static PImage drawAccumulator(PApplet ctx, int[] acc, int rDim, int phiDim) {
        PImage houghImg = ctx.createImage(rDim + 2, phiDim + 2, ALPHA);
        for (int i = 0; i < acc.length; i++) {
            houghImg.pixels[i] = ctx.color(min(255, acc[i]));
        }
        houghImg.updatePixels();
        return houghImg;
    }

    public static PImage drawAccumulator(PApplet ctx, ArrayData acc) {
        return drawAccumulator(ctx, acc.dataArray, acc.radius - 2, acc.angle - 2);
    }

    public static void drawLinesFromAccumulator(PApplet ctx, ArrayData arr, int imgWidth, float phiStep, float rStep) {
        drawLinesFromAccumulator(ctx, arr.dataArray, imgWidth, phiStep, rStep, arr.radius);
    }

    /**
     * Draw the lines from a line accumulator into the given drawing context
     * @param ctx Drawing context
     * @param acc line accumulator
     * @param imgWidth max width in the drawing context
     * @param phiStep discrete angle step
     * @param rStep discrete radius step
     * @param rDim radius count
     */
    public static void drawLinesFromAccumulator(PApplet ctx, int[] acc, int imgWidth, float phiStep, float rStep, int rDim) {
        int size = acc.length;
        for (int i = 0; i < size; ++i) {
            if (acc[i] > ACCUMULATOR_THRESHOLD) {
                int accPhi = (i / (rDim + 2)) - 1;
                int accR = i - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * 0.5f) * rStep;
                float phi = accPhi * phiStep;
                drawLinePolar(ctx, r,phi, imgWidth);
            }
        }
    }

    public static void drawLinesFromBestCandidates(PApplet ctx, List<Pair<Integer, Integer>> bestCandidates, int imgWidth, float phiStep, float rStep, int rDim) {
        bestCandidates.forEach(candidate -> {
            int i = candidate._2;
            int accPhi = (i / (rDim + 2)) - 1;
            int accR = i - (accPhi + 1) * (rDim + 2) - 1;
            float r = (accR - (rDim - 1) * 0.5f) * rStep;
            float phi = accPhi * phiStep;
            drawLinePolar(ctx, r, phi, imgWidth);
        });
    }


    private static void drawLinePolar(PApplet ctx, float r, float phi, int imgWidth) {
        int x0 = 0;
        int y0 = (int) (r / sin(phi));
        int x1 = (int) (r / cos(phi));
        int y1 = 0;
        int x2 = imgWidth;
        int y2 = (int) (-cos(phi) / sin(phi) * x2 + r / sin(phi));
        int y3 = imgWidth;
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

    public static void drawIntersections(PApplet ctx, List<Pair<Integer, Integer>> intesections) {
        intesections.forEach(p -> {
            System.out.println("drawing intersection at " + p);
            ctx.fill(255, 128, 0);
            ctx.ellipse(p._1, p._2, 10, 10);
        });
    }
}
