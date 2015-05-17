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

//    public SobelProcess(PApplet context, int threshold) {
//        PixelFunction sobelFunction = (pixel, source, index) -> {
//            int convrX = 0;
//            int convrY = 0;
//            int clampX;
//            int clampY;
//
//            for (int j = 0; j < 3; j++) {
//                for (int i = 0; i < 3; i++) {
//                    // Clamp the image coordinates
//                    clampX = (x + i - 1 < 0) ? 0 : ((x + i - 1 > xlength - 1) ? xlength - 1 : x + i - 1);
//                    clampY = (y + j - 1 < 0) ? 0 : ((y + j - 1 > ylength - 1) ? ylength - 1 : y + j - 1);
//                    // Do the actual calculation
//                    convrX += source.pixels[clampY][clampX] * SobelProcess.kernelX[j][i];
//                    convrY += imgaddr[clampY][clampX] * SobelProcess.kernelY[j][i];
//                }
//            }
//
//            return (Math.abs(convrX) + Math.abs(convrY) > threshold) ? 255 : 0;
//        };
//        super(context, sobelFunction);
//    }

}
