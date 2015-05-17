package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

import static ImageProcessing.Utils.*;
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

        ArrayData acc = new ArrayData(rMax + 2, phiMax + 2);

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
                        int normalized = Math.round((r + acc.radius) / 2);
                        acc.accumulate(normalized, angle, 1);
                    }
                }
            }
        }
        return acc;
    }

    public int[] computeAccumulator2(PApplet ctx, PImage source) {
        int radius = source.width;
        int height = source.height;
        int phiMax = phiDim();
        int rMax = rDim(source);

        int[] accumulator = new int[(phiMax + 2) * (rMax + 2)];

        float[] sinTable = new float[phiMax];
        float[] cosTable = new float[phiMax];
        for(int angle = 0; angle < phiMax; ++angle) {
            sinTable[angle] = sin(PI * angle/phiMax);
            cosTable[angle] = cos(PI * angle/phiMax);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < radius; x++) {
                if (ctx.brightness(source.pixels[y * source.width + x]) != 0) {
                    for (int angle = 0; angle < phiMax; ++angle) {
                        float r = x * cosTable[angle] + y * sinTable[angle];
                        int normalized = Math.round((r + rMax) / 2);
                        accumulator[angle * rMax + normalized] += 1;
                    }
                }
            }
        }
        return accumulator;
    }

    public static List<Pair<Integer, Integer>> getIntersections(List<Pair<Integer, Integer>> lines) {
        ArrayList<Pair<Integer, Integer>> intersections = new ArrayList<>();

        for (int i = 0; i < lines.size() - 1; i++) {
            Pair<Integer, Integer> line1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Pair<Integer, Integer> line2 = lines.get(j);
                int r1 = line1._1();
                int r2 = line2._1();
                int phi1 = line1._2();
                int phi2 = line2._2();
                float d = cos(phi2) * sin(phi1) - cos(phi1) * sin(phi2);
                float x = r2 * sin(phi1) - r1 * sin(phi2);
                float y = -r2 * cos(phi1) + r1 * cos(phi2);
                intersections.add(new Pair<>(Math.round(x / d), Math.round(y / d)));
            }
        }
        return intersections;
    }


    public static List<Pair<Integer, Integer>> sortAndTake(List<Pair<Integer, Integer>> list, int count) {
        Collections.sort(list, (o1, o2) -> (o2._2() > o2._2() || o1._2() == o2._2() && o1._1() < o2._1()) ? -1 : 1);
        return list.stream().limit(count).collect(Collectors.toList());
    }

    /**
     * Returns a list of pairs of a given length corresponding to the best candidates in the accumulator
     * @param acc the line accumulator
     * @param minValue minimum values the candidates must conform to
     * @return a list of the best candidates as pairs of index in accumulator and value
     */
    public static List<Pair<Integer, Integer>> bestCandidates(int[] acc, int minValue) {
        ArrayList<Pair<Integer, Integer>> arr = new ArrayList<>(acc.length);
        for (int index = 0; index < acc.length; ++index) {
            arr.add(new Pair<>(index, acc[index]));
        }
        return arr.stream().filter(p -> p._2() > minValue).collect(Collectors.toList());
    }

    public static List<Pair<Integer, Integer>> bestCandidates(ArrayData acc, int minValue) {
        return bestCandidates(acc.dataArray, minValue);
    }


    public static List<Pair<Integer,Integer>> improvedCandidates(ArrayData acc, int minVote) {
        int neighboorhood = 10;
        ArrayList<Pair<Integer, Integer>> candidates = new ArrayList<>();
        for (int accR = 0; accR < acc.radius; accR++) {
            for (int accPhi = 0; accPhi < acc.angle; accPhi++) {

                int idx = (accPhi + 1) * acc.radius + accR + 1;

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

    public int phiDim() {
        return (int) (Math.PI / phiStep);
    }

    public int rDim(PImage img) {
        return (int) (((img.width + img.height) * 2 + 1) / rStep);
    }

    public static PImage drawAccumulator(PApplet ctx, int[] acc, int rDim, int phiDim) {
        System.out.println("drawing accumulator image of size " + rDim + " , ");
        PImage houghImg = ctx.createImage(rDim + 2, phiDim + 2, ALPHA);
        for (int i = 0; i < acc.length; i++) {
            houghImg.pixels[i] = ctx.color(min(255, acc[i]));
        }
        houghImg.updatePixels();
        return houghImg;
    }

    public static PImage drawAccumulator(PApplet ctx, ArrayData acc) {
//        PImage houghImg = ctx.createImage(acc.radius, acc.height, ALPHA);
//        for (int i = 0; i < acc.dataArray.length; i++) {
//            houghImg.pixels[i] = ctx.color(min(255, acc.dataArray[i]));
//        }
//        houghImg.updatePixels();
        return drawAccumulator(ctx, acc.dataArray, acc.radius - 2, acc.angle - 2);
    }

    public static void drawLinesFromAccumulator(PApplet ctx, ArrayData arr, int imgWidth, float phiStep, float rStep) {
        drawLinesFromAccumulator(ctx, arr.dataArray, imgWidth, phiStep, rStep, arr.radius);
    }

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
            System.out.println("drawing candidate " + candidate );
            int i = candidate._2();
            int accPhi = (i / (rDim + 2)) - 1;
            int accR = i - (accPhi + 1) * (rDim + 2) - 1;
            float r = (accR - (rDim - 1) * 0.5f) * rStep;
            float phi = accPhi * phiStep;
            drawLinePolar(ctx, r, phi, imgWidth);
        });
    }


    public static void drawLinePolar(PApplet ctx, float r, float phi, int imgWidth) {
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
            ctx.fill(255, 128, 0);
            ctx.ellipse(p._1(), p._2(), 10, 10);
        });
    }
}
