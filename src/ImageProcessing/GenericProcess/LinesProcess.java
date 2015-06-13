package ImageProcessing.GenericProcess;

import ImageProcessing.Utils;
import ImageProcessing.Utils.Pair;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.util.List;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.RGB;

public class LinesProcess {

    private final PApplet ctx;
    private final int width;
    private final int height;

    /**
     *
     * @param ctx drawing context.
     * @param width target image width
     * @param height target image height
     */
    public LinesProcess(PApplet ctx, int width, int height) {
        this.ctx = ctx;
        this.width = width;
        this.height = height;
    }


    public PGraphics mutableComputeAsPair(List<Pair<Float, Float>> source, PGraphics target) {
        target.beginDraw();
        source.stream().forEach(pair -> drawLine(pair, target));
//        target.stroke(0,0,0);
//        target.line(0, target.height/2, target.width, target.height/2);
        target.endDraw();
        return target;
    }

    public PGraphics immutableComputeAsPair(List<Pair<Float, Float>> source) {
        return mutableComputeAsPair(source, ctx.createGraphics(width, height));
    }

    public PGraphics mutableComputeAsVector(List<PVector> source, PGraphics target) {
        target.beginDraw();
        source.stream().map(vector -> new Pair<>(vector.x, vector.y)).forEach(pair -> drawLine(pair, target));
        target.endDraw();
        return target;
    }

    public PGraphics immutableComputeAsVector(List<PVector> source) {
        return mutableComputeAsVector(source, ctx.createGraphics(width, height));
    }

    private static void drawLine(Pair<Float, Float> line, PGraphics target){
        float angle = line.phi;
        float px = line.r * cos(angle);
        float py = line.r * sin(angle);
        float maxLength = sqrt(target.width * target.width + target.height * target.height);
        float p1_x = px + maxLength * cos(angle);
        float p1_y = py + maxLength * sin(angle);
        float p2_x = px - maxLength * cos(angle);
        float p2_y = py - maxLength * sin(angle);
        target.stroke(204, 102, 0);
        target.line(p1_x, p1_y, p2_x, p2_y);
    }

    private static void drawCourseLine(Pair<Float, Float> line, PGraphics target) {
        float r = line.r;
        float phi = line.phi;
        int x0 = 0;
        int y0 = (int) Math.round((r / sin(phi)));
        int x1 = (int) Math.round((r / cos(phi)));
        int y1 = 0;
        int x2 = target.width;
        int y2 = (int) Math.round((-cos(phi) / sin(phi) * x2 + r / sin(phi)));
        int y3 = target.width;
        int x3 = (int) Math.round((-(y3 - r / sin(phi)) * (sin(phi) / cos(phi))));
        // Finally, plot the lines
        target.stroke(204,102,0);

        if (y0 > 0) {
            if (x1 > 0)
                target.line(x0, y0, x1, y1);
            else if (y2 > 0)
                target.line(x0, y0, x2, y2);
            else
                target.line(x0, y0, x3, y3);
        }
        else {
            if (x1 > 0) {
                if (y2 > 0)
                    target.line(x1, y1, x2, y2); else
                    target.line(x1, y1, x3, y3);
            }
            else
                target.line(x2, y2, x3, y3);
        }
    }
}
