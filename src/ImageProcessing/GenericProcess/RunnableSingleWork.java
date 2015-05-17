package ImageProcessing.GenericProcess;

public class RunnableSingleWork implements Runnable{
    int id;
    int x0;
    int y0;
    int xlength;
    int ylength;
    int threshold;

    int[][] imgaddr;
    int[][] rimgaddr;

    int xblock;
    int yblock;

    // Constructor to pass arguments
    RunnableSingleWork(int id, int[][] imgaddr, int[][] rimgaddr, int[] ipos, int yblock, int xblock, int threshold){
        this.id = id;
        this.x0 = ipos[1];
        this.y0 = ipos[0];
        this.xlength = ipos[3];
        this.ylength = ipos[2];
        this.threshold = threshold;

        this.imgaddr = imgaddr;
        this.rimgaddr = rimgaddr;

        this.xblock = xblock;
        this.yblock = yblock;
    }

    // Override this method to implement your function
    @Override
    public void run() {
        //Your implementation
        int clampX;
        int clampY;

        for (int y = y0; y < (y0 + yblock); y++){
            for (int x = x0; x < (x0 + xblock); x++) {

                int convrX = 0;
                int convrY = 0;

                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 3; i++){
                        // Clamp the image coordinates
                        clampX = (x+i-1 < 0) ? 0 : ((x+i-1 > xlength-1) ? xlength-1 : x+i-1);
                        clampY = (y+j-1 < 0) ? 0 : ((y+j-1 > ylength-1) ? ylength-1 : y+j-1);
                        // Do the actual calculation
                        convrX += imgaddr[clampY][clampX] * SobelProcess.kernelX[j][i];
                        convrY += imgaddr[clampY][clampX] * SobelProcess.kernelY[j][i];
                    }
                }

                if (Math.abs(convrX) + Math.abs(convrY) > threshold){
                    rimgaddr[y][x] = 255;
                } else {
                    rimgaddr[y][x] = 0;
                }

            }
        }
    }

}