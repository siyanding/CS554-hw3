package hw3;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.ops.BinaryImageOp;
import pixeljelly.ops.PluggableImageOp;

public class ImageDarkenOnlyOp extends BinaryImageOp implements PluggableImageOp{

	public ImageDarkenOnlyOp(BufferedImage left) {
		super(left);
		
	}
	
	public ImageDarkenOnlyOp() {
		super(null);
		
	}
	
	@Override
	public String getAuthorName() {
		
		return "Siyan Ding";
	}

	@Override
	public BufferedImageOp getDefault(BufferedImage left) {
		
		return new ImageDarkenOnlyOp(left);
	}

	@Override
	public int combine(int s1, int s2) {
		if(s1 < s2){
			return s1;
		}else{
			return s2;
		}		
	}
}
