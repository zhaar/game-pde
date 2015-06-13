package ImageProcessing;

import ImageProcessing.GenericProcess.FilterProcess;
import ImageProcessing.GenericProcess.HoughCopy;
import ImageProcessing.GenericProcess.LinesProcess;
import UI.HScrollbar;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Capture;

import java.util.List;
import java.util.stream.Collectors;

public class ImageProcessing extends PApplet {

    private static final float phiStep = 0.06f;
    private static final float rStep = 2.5f;

    Capture cam;
    PImage hough;
    PImage sobel;
    Utils.ArrayData acc;
    Hough h;
    HoughCopy hc;
    PImage img, originalImg;
    PGraphics lines;
    HScrollbar scrollbar;
    List<Utils.Pair<Integer,Integer>> candidates;
    List<Utils.Pair<Integer,Integer>> pairs;

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
        lines = createGraphics(originalImg.width, originalImg.height);
        scrollbar = new HScrollbar(this, 0, 380, 1400, 20);
        h = new Hough(rStep, phiStep);
        hc = new HoughCopy(this, phiStep, rStep);

        //noLoop();
    }

    private void transformImage() {
        img = originalImg;

//        img = FilterProcess.brightnessBinaryThreshold(this, (int) (scrollbar.getPos() * 240)).immutableCompute(img);
//        img = ImageConvolution.gauss(img, this);
//        img = ImageConvolution.gauss(img, this);
//        img = FilterProcess.brightnessBinaryThreshold(this, (int) (scrollbar.getPos() * 160)).immutableCompute(img);
        sobel = ImageConvolution.sobel(img, this);

//        acc = h.computeAccumulator(this, sobel);
//        hough = Hough.drawAccumulator(this, acc);
//        lines = new LinesProcess(this, img.width, img.height).mutableComputeAsVector(Hough.bestCandidates(Hough.dataAsVector(acc), 200), lines);
//        hough.resize(400, 400);
    }
    
    public void draw() {
        long time = System.currentTimeMillis();

//        if (cam.available() == true) {
//            cam.read();
//        }
//        img = cam.get();
//        pushMatrix();
//        scale(-1.0f, 1.0f);
//        image(ImageConvolution.sobel(img, this),-img.radius,0);
//        popMatrix();
        transformImage();
//        image(originalImg, 0, 0);
        image(img,0,0);
//        image(sobel, img.width, 0);
//        image(hough, img.width + sobel.width, 0);
        image(hc.drawLines(hc.filterLines(hc.compute(sobel),150), lines).get(), 0,0);
//        candidates = Hough.sortAndTake(Hough.improvedCandidates(acc, 200), 6);
//        assert candidates.size() <= 6;
//        Hough.drawLinesFromBestCandidates(this, candidates , sobel.width, phiStep, rStep, acc.radius-2);
//        Hough.drawIntersections(this, Hough.getIntersections(candidates.parallelStream().map(p -> Hough.arrayIndexToPair(acc, p, rStep, phiStep)).collect(Collectors.toList())));
        scrollbar.display();
        scrollbar.update();
//        Hough.drawLinesFromAccumulator(this, acc, img.width,phiStep, rStep);

        System.out.println("ms/f: " + (System.currentTimeMillis() - time));

    }
}
