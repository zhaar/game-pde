package ImageProcessing.GenericProcess;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.ArrayList;

import static processing.core.PConstants.*;

public class GenericImageProcess extends ProcessFromImage<PImage> {

//    protected final PApplet ctx;
//    private final PixelFunction fn;

    public GenericImageProcess(final PApplet context, PixelFunction pixelFunction) {
        super(context, pixelFunction, arg -> context.createImage(arg.width, arg.height, context.RGB), (acc, value, index) -> acc.pixels[index] = value);
    }
}
