package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.ops.BinaryImageOp;
import pixeljelly.ops.PluggableImageOp;

public class ImageScreenBlendOp extends BinaryImageOp implements PluggableImageOp{
	
	public ImageScreenBlendOp(BufferedImage left) {
		super(left);
		
	}
	
	public ImageScreenBlendOp() {
		super(null);
		
	}

	@Override
	public String getAuthorName() {
		
		return "Siyan Ding";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage left) {
		
		return new ImageScreenBlendOp(left);
	}

	@Override
	public int combine(int s1, int s2) {
		
		return (int) (1-((1-s1/255f) * (1-s2/255f)));
	
		
	}
}
