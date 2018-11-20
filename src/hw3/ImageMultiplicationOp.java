package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.ops.BinaryImageOp;
import pixeljelly.ops.PluggableImageOp;


public class ImageMultiplicationOp extends BinaryImageOp implements PluggableImageOp{

	public ImageMultiplicationOp(BufferedImage left) {
		super(left);
		
	}
	
	public ImageMultiplicationOp() {
		super(null);
		
	}

	@Override
	public String getAuthorName() {
		
		return "Siyan Ding";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage left) {
		
		return new ImageMultiplicationOp(left);
	}

	@Override
	public int combine(int s1, int s2) {
		
		return (int) ((s1/255f) * (s2/255f) * 255f);
	}
}
