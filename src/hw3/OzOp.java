package hw3;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pixeljelly.ops.BinaryImageOp;
import pixeljelly.ops.NullOp;
import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class OzOp extends NullOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp{
	 char direction;
	 public OzOp(){
			this('H');
	}
	 
	 public OzOp( char direction ){
			try{
				if(direction != 'H' && direction != 'V' && direction != 'R'){
					throw new Exception("direction must one of H, V, R");
				}
				this.direction = direction;		
			}catch(Exception e){
				System.out.println(e);
			}
			
		 
	 }
	 
	 public BufferedImageOp getDefault( BufferedImage src ){		 
		 return new OzOp('H');		 
	 }
	 
	 public String getAuthorName(){
		return "Siyan Ding";
	 }
	 
	 public char getDirection( ){
		return direction;
	 }
	 
	 public void setDirection( char direction ){
		 this.direction = direction;
	 }
	 

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		if(dest == null){
			dest = createCompatibleDestImage(src, src.getColorModel());
		}
		for(Location pt : new RasterScanner(src,false)){
			Color tempColor = new Color(src.getRGB(pt.col, pt.row));
			float[] hsv = new float[3];
			hsv = tempColor.RGBtoHSB(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), hsv);
			switch(direction){
			case 'H':
				hsv[1] = pt.col * 1.0f/src.getWidth();
				break;
			case 'V':
				hsv[1] = pt.row * 1.0f/src.getHeight();
				break;
			case 'R':
				double x = Math.floor((src.getWidth() - 1f)/2);
				double y = Math.floor((src.getHeight() - 1f)/2);
				double dis = Math.sqrt(Math.pow((x * 1f - pt.col), 2) + Math.pow((y * 1f - pt.row), 2))
						/Math.sqrt(x * x + y * y);
				hsv[1] = (float)dis;
				break;
			}
			dest.setRGB(pt.col, pt.row, tempColor.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
				
		} 
		return dest;
	}
}
