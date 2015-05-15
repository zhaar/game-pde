package ImageProcessing;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageConvolution {
	public static PImage convolute(PImage img, float[][] kernel, PApplet pApplet) {
		
		float weight = 1.f;
		PImage result = pApplet.createImage(img.width, img.height, PImage.ALPHA);
		
		int N = 3;
		for(int x = N/2; x < img.width - N/2; x++){
			for(int y = N/2; y < img.height - N/2; y++){
				float r_intensity = 0;
				float g_intensity = 0;
				float b_intensity = 0;
				for(int i = x - N/2;  i <= x + N/2; i++){
					for(int j = y - N/2; j <= y + N/2; j++){
						r_intensity += pApplet.red(img.pixels[j * img.width + i]) * kernel[i - x + N/2][j - y + N/2];
						g_intensity += pApplet.green(img.pixels[j * img.width + i]) * kernel[i - x + N/2][j - y + N/2];
						b_intensity += pApplet.blue(img.pixels[j * img.width + i]) * kernel[i - x + N/2][j - y + N/2];
					}
				}
				r_intensity /= weight;
				g_intensity /= weight;
				b_intensity /= weight;
				
				result.pixels[y * img.width + x] = pApplet.color(r_intensity, g_intensity, b_intensity);
			}
		}
		return result;
	}
	
	public static PImage sobel(PImage img, PApplet pApplet){
		float[][] hKernel = { { 0, 1, 0 }, { 0, 0, 0 }, { 0, -1, 0 } };
		float[][] vKernel = { { 0, 0, 0 }, { 1, 0, -1 }, { 0, 0, 0 } };
		PImage result = pApplet.createImage(img.width, img.height, PImage.ALPHA);
		// clear the image
		for (int i = 0; i < img.width * img.height; i++) {
			result.pixels[i] = pApplet.color(0);
		}
		float max = 0;
		float[] buffer = new float[img.width * img.height];
		
		PImage vImage = convolute(img, vKernel, pApplet);
		
		for (int y = 2; y < img.height - 2; y++) { // Skip top and bottom edges
			for (int x = 2; x < img.width - 2; x++) { // Skip left and right
				if (buffer[y * img.width + x] > (int) (max * 0.3f)) { 

					result.pixels[y * img.width + x] = pApplet.color(255);
				} else {
					result.pixels[y * img.width + x] = pApplet.color(0);
				}
			}
		}
		return result;
	}
}
