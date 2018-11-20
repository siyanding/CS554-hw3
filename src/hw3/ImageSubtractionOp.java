package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.ops.BinaryImageOp;
import pixeljelly.ops.PluggableImageOp;

public class ImageSubtractionOp extends BinaryImageOp implements PluggableImageOp{

	public ImageSubtractionOp(BufferedImage left) {
		super(left);
		
	}
	
	public ImageSubtractionOp() {
		super(null);
		
	}
	
	@Override
	public String getAuthorName() {
		
		return "Siyan Ding";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage left) {
		
		return new ImageSubtractionOp(left);
	}
	
	@Override
	public int combine(int s1, int s2) {
		
		return Math.abs(s1 - s2);
	}
}
