package ImageProcessing.GenericProcess;


import processing.core.PApplet;

public class SobelProcess extends GenericImageProcess{

    public static int[][] kernelX = {
            {-1, 0, +1},
            {-2, 0, +2},
            {-1, 0, +1}
    };

    public static int[][] kernelY = {
            {-1, -2, -1},
            { 0,  0,  0},
            {+1, +2, +1}
    };

    public SobelProcess(PApplet context, PixelFunction pixelFunction) {
        super(context, pixelFunction);
    }

}
