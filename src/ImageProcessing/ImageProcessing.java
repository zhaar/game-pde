package ImageProcessing;

import ImageProcessing.GenericProcess.FilterProcess;
import UI.HScrollbar;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;
import processing.video.*;

import java.util.List;
import java.util.stream.Collectors;

public class ImageProcessing extends PApplet {

    private static final float phiStep = 0.06f;
    private static final float rStep = 2.5f;

    Movie cam;
//    Capture cam;
    PImage hough;
    PImage sobel;
    Utils.ArrayData acc;
    Hough h;
    PImage img, originalImg;
    HScrollbar scrollbar;
    List<Utils.Pair<Integer,Integer>> candidates;
    List<Utils.Pair<Integer,Integer>> pairs;

    public void setup() {

        String[] cameras = Capture.list();
        if (cameras.length == 0) {
            println("There are no cameras available for capture.");
            exit(); } else {
            int goodCam = 0;
            println("Available cameras:");
            for (int i = 0; i < cameras.length; i++) {
                println(cameras[i]);
                if (cameras[i].contains("640x"))
                    goodCam = i;
            }
//            cam = new Capture(this, cameras[goodCam]);
//            cam.start();
            
            cam = new Movie(this, "Game\\data\\testvideo.mp4");
            cam.loop();
        }
        size(1400, 400);
        originalImg = loadImage("board1.jpg");
        originalImg.resize(500, 400);
        scrollbar = new HScrollbar(this, 0, 380, 1400, 20);
        //noLoop();
    }

    private void transformImage() {
        img = originalImg;
        img = FilterProcess.binaryThreshold(this, (int) (scrollbar.getPos() * 240)).immutableCompute(img);
        img = ImageConvolution.gauss(img, this);
        img = ImageConvolution.gauss(img, this);
        img = FilterProcess.binaryThreshold(this, (int) (scrollbar.getPos() * 160)).immutableCompute(img);
        sobel = ImageConvolution.sobel(img, this);

        h = new Hough(rStep, phiStep);
        acc = h.computeAccumulator(this, sobel);
        hough = Hough.drawAccumulator(this, acc);
        hough.resize(400, 400);
    }
    
    public void draw() {
        long time = System.currentTimeMillis();

        if (cam.available() == true) {
            cam.read();
        }
        originalImg = cam.get();
//        pushMatrix();
//        scale(-1.0f, 1.0f);
//        image(ImageConvolution.sobel(img, this),-img.radius,0);
//        popMatrix();
        transformImage();
        image(originalImg, 0, 0);
        image(sobel, img.width, 0);
        image(hough, img.width + sobel.width, 0);
        candidates = Hough.sortAndTake(Hough.improvedCandidates(acc, 200), 6);
        assert candidates.size() <= 6;
        Hough.drawLinesFromBestCandidates(this, candidates , sobel.width, phiStep, rStep, acc.radius-2);
        Hough.drawIntersections(this, Hough.getIntersections(candidates.parallelStream().map(p -> Hough.arrayIndexToPair(acc, p, rStep, phiStep)).collect(Collectors.toList())));
        scrollbar.display();
        scrollbar.update();

//        System.out.println("ms/f: " + (System.currentTimeMillis() - time));

    }
}
