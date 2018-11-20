package hw3;

import java.awt.Color;
import java.awt.Rectangle;
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

public class ChromaKeyOp extends BinaryImageOp implements BufferedImageOp, pixeljelly.ops.PluggableImageOp {
	
	private BufferedImage background;
	private Color chromaKey;
	
	public ChromaKeyOp(){
		super(null);
	}
	
	public ChromaKeyOp( BufferedImage background, Color chromaKey ){
		super(background);
		this.background = background;
		this.chromaKey = chromaKey;
	}
	
	public BufferedImageOp getDefault( BufferedImage src ){
		
		return new ChromaKeyOp(null,Color.green);
		 
	}
	
	public String getAuthorName(){
		
		return "Siyan Ding";
	}
	
	public void setChromaKey( Color c ){
		
		chromaKey = c;
	}
	
	public void setBackground( BufferedImage background ){
		
		this.background = background; 
	}
	
	public Color getChromaKey( ){
		
		return chromaKey;
	}
	
	public BufferedImage getBackground( ){
		
		return background;
		
	}

	public BufferedImage createCompatibleDestImage(Rectangle bounds, ColorModel destCM) {

		return new BufferedImage(
				destCM,
				destCM.createCompatibleWritableRaster(bounds.width, bounds.height),
				destCM.isAlphaPremultiplied(),
				null);
		
	}
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		Rectangle srcBounds = src.getRaster().getBounds();
		Rectangle backBounds = background.getRaster().getBounds();
		Rectangle intersection = srcBounds.intersection(backBounds);
		
		if(background == null){
			background = new BufferedImage(
							src.getColorModel(),
							src.getRaster(),
							src.getColorModel().isAlphaPremultiplied(),
							null);
			
			for(Location pt: new RasterScanner(background,false)){
				background.setRGB(pt.col, pt.row, Color.white.getRGB());
			}
		}
		if(dest == null){
			if(src.getRaster().getNumBands() < background.getRaster().getNumBands()){
				dest = createCompatibleDestImage(intersection, src.getColorModel());
			}else{
				dest = createCompatibleDestImage(intersection, background.getColorModel());
			}
		}
		
		for(Location pt : new RasterScanner(dest, true)){
			int s1 = src.getRaster().getSample(pt.col, pt.row, pt.band);
			int s2 = background.getRaster().getSample(pt.col, pt.row, pt.band);
			float alpha = 1 - Distance(new Color(src.getRGB(pt.col, pt.row)), chromaKey);
			dest.getRaster().setSample(pt.col, pt.row, pt.band, combine(s1, s2, alpha));
		}
		
		return dest;
	}

	public float Distance(Color Pc, Color chromaKey2) {
		float dis = (float) Math.sqrt(
				Math.pow((Pc.getRed() - chromaKey2.getRed()), 2 ) +
				Math.pow((Pc.getGreen() - chromaKey2.getGreen()), 2 ) +
				Math.pow((Pc.getBlue() - chromaKey2.getBlue()), 2 )
				);
		dis = (float) (dis / Math.sqrt( 3f * 255f *255f ));
		return dis;
	}

	private int combine(int s1, int s2, float alpha) {
		
		return (int) (alpha * s1 + (1-alpha) * s2);
	}

	@Override
	public int combine(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
}
