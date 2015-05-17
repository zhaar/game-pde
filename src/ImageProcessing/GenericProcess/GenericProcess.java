package ImageProcessing.GenericProcess;

import processing.core.PImage;

public interface GenericProcess {
    PImage mutableCompute(PImage source, PImage target);
    PImage immutableCompute(PImage source);
}
