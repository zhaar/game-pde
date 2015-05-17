package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;

public class Assignment9 extends PApplet {

    private static final float phiStep = 0.06f;
    private static final float rStep = 2.5f;

    Capture cam;
    PImage hough;
    PImage sobel;
    Utils.ArrayData acc;
    Hough h;
    PImage img, originalImg;

    public void setup() {

//        String[] cameras = Capture.list();
//        if (cameras.length == 0) {
//            println("There are no cameras available for capture.");
//            exit(); } else {
//            int goodCam = 0;
//            println("Available cameras:");
//            for (int i = 0; i < cameras.length; i++) {
//                println(cameras[i]);
//                if (cameras[i].contains("640x"))
//                    goodCam = i;
//            }
//            cam = new Capture(this, cameras[goodCam]);
//            cam.start();
//        }
        size(1400, 400);
        originalImg = loadImage("board1.jpg");
        originalImg.resize(500, 400);
        img = originalImg;
        ThresholdFilter tFilter = new ThresholdFilter(this, ThresholdFilter.binaryThreshold(120), img);
        img = tFilter.applyFilter();
        img = ImageConvolution.gauss(img, this);
        tFilter = new ThresholdFilter(this, ThresholdFilter.binaryThreshold(50), img);
        img = tFilter.applyFilter();
        sobel = ImageConvolution.sobel(img, this);

        h = new Hough(phiStep, rStep);
        acc = h.computeAccumulator(this, sobel);
        hough = Hough.drawAccumulator(this, acc);
        hough.resize(400, 400);
        noLoop();
    }

    public void draw() {
//        if (cam.available() == true) {
//            cam.read();
//        }
//        img = cam.get();
//        pushMatrix();
//        scale(-1.0f, 1.0f);
//        image(ImageConvolution.sobel(img, this),-img.radius,0);
//        popMatrix();

        image(img, 0, 0);
        image(sobel, img.width, 0);
        image(hough, img.width+sobel.width, 0);
//        Hough.drawLinesFromAccumulator(this, acc, sobel.width, phiStep, rStep);
        Hough.drawLinesFromBestCandidates(this, Hough.sortAndTake(Hough.bestCandidates(acc, 200), 1000), sobel.width, phiStep, rStep, acc.radius);
    }
}
