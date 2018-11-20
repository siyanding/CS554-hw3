package hw3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import javax.imageio.ImageIO;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class query {
	String Q_URL;
	File DB_FILENAME, RESPONSE_FILENAME;
	int k;
	BufferedImage src;
	private String[] urls;
	int NR,NG,NB;
	float[] HisDis;
	String[][] result;
	
	public query(String Q_URL, File DB_FILENAME, File RESPONSE_FILENAME, int K ){
		this.Q_URL = Q_URL;
		this.DB_FILENAME = DB_FILENAME;
		this.RESPONSE_FILENAME = RESPONSE_FILENAME;
		this.k =  K;
	}
	
	public void read() throws IOException {
		int count = 0;
		String[] tokens;
		try {
			Scanner scanner = new Scanner(DB_FILENAME);
			String tempN = scanner.nextLine();
			tokens = tempN.split(" ");
			NR = Integer.valueOf(tokens[0]);
			NG = Integer.valueOf(tokens[1]);
			NB = Integer.valueOf(tokens[2]);
			while(scanner.hasNextLine()){
				String temp = scanner.nextLine();
				count ++;				
			}
			urls = new String[count];
			
			scanner = new Scanner(DB_FILENAME);
			result = new String[count][2];
			String temp = scanner.nextLine();
			float[] h2 = Histogram(Q_URL);
			float[] h1 = new float[(int) ( Math.pow(2,NR) *Math.pow(2,NG) *Math.pow(2,NB) )];
			HisDis = new float[count];
			int i=0;
			//put all the urls into urls array
			while( scanner.hasNextLine() && i<urls.length ) {
				String tempUrl= scanner.nextLine();
				tokens = tempUrl.split(" ");
			
						urls[i] = tokens[2];

					
					for( int j = 0; j< h1.length; j++ ) {
						h1[j] = Float.valueOf(tokens[j+3]);
					}
					
					
					HisDis[i] = getHisDistance(h1,h2);
					result[i][0] = tokens[0] +" "+tokens[1]+" "+tokens[2];
					result[i][1] = String.valueOf(HisDis[i]);
					
					i++;
				}				
			
			
			Arrays.sort(result, new Comparator<String[]>(){//二维数组按照某列进行排序,你也可以采用Map
	            public int compare(String[] o1, String[] o2) {//任何多维数组可看成一个一维数组,一维数组中每个元素是一个一维数组
	                return o1[1].compareTo(o2[1]);//比较：大于0则表示升序
	            }
	        } );
			
			scanner.close();
			
			
			write( result);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write( String[][] result) throws IOException {
		
		FileWriter outfile = new FileWriter (RESPONSE_FILENAME);
		
		outfile.write("<!DOCTYPE html><html><head><title>Pictures</title><link href=\"style.css\" rel=\"stylesheet\"></head><body>");
		outfile.write("<div class=\"img\">Query file: <br><a href=\"");
		
		outfile.write(Q_URL );
		outfile.write("\" class=\"flickr\"></a><a href=\"" );
		outfile.write(Q_URL );
		outfile.write(" \"><img style=\"max-height=400px; max-width:400px;\" src=\"");
		outfile.write(Q_URL );
		outfile.write(" \"></a></div>");
		
		
		for( int i=0; i< k && i< result.length; i++) {
			String[] url = result[i][0].split(" ");
			outfile.write("<div style=\"    display: inline-block;  margin: 5px; padding: 0;\" class=\"img\">Distace: "
					+ result[i][1]
					+ "<br><a style=\"    display: block;  width: 10px; height: 10px; background-color: red;\" href=\"");
			
			outfile.write( url[0] );
			outfile.write("\" class=\"flickr\"></a><a href=\"" );
			outfile.write( url[1] );
			outfile.write(" \"><img style=\"max-height=400px; max-width:400px;\"  src=\"");
			outfile.write( url[2] );
			outfile.write(" \"></a></div>");
			
		}
		
		
		outfile.write("</body></html>");
		outfile.close();
		
	}
	
	public float[] Histogram(String url){
		try {
			src = ImageIO.read(new URL(url));			
			double R1,G1,B1;
			int Nr = (int) Math.pow(2, NR);
			int Ng = (int) Math.pow(2, NG);
			int	Nb = (int) Math.pow(2, NB);
			float[] histogram = new float[Nr*Ng*Nb];
			int index;
			for(Location pt: new RasterScanner(src,false)){
				R1 = Math.floor((new Color(src.getRGB(pt.col, pt.row)).getRed())*Nr/256);
				G1 = Math.floor((new Color(src.getRGB(pt.col, pt.row)).getGreen())*Ng/256);
				B1 = Math.floor((new Color(src.getRGB(pt.col, pt.row)).getBlue())*Nb/256);
				index = (int) (R1*(Ng*Nb)+G1*(Nb)+B1);
				histogram[index] ++;
				for(int i = 0; i < histogram.length; i++){
					histogram[i] =  (histogram[i] * 1f)/(src.getWidth() * src.getHeight());
				}
			}
			return histogram;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public float[][] A(float[] h1, float[] h2){
		float[][] A = new float[h1.length][h1.length];
		int Nr = (int) Math.pow(2, NR);
		int Ng = (int) Math.pow(2, NG);
		int Nb = (int) Math.pow(2, NB);
				
		for(int i = 0; i < h1.length; i++){
			for(int j = 0; j<h1.length; j++){
				float dr = (float) (256-(256/Nr)/2);
				float dg = (float) (256-(256/Ng)/2);
				float db = (float) (256-(256/Nb)/2);
				int ri = (int)(((i/(Ng*Nb))*256/Nr)+(128/Nr));
				int gi = (int)((((i%(Ng*Nb))/Nb)*(256/Ng))+(128/Ng));
				int bi = (int)(((i%Nb)*(256/Nb))+128/Nb);
				int rj = (int)(((j/(Ng*Nb))*256/Nr)+(128/Nr));
				int gj = (int)((((j%(Ng*Nb))/Nb)*(256/Ng))+(128/Ng));
				int bj = (int)(((j%Nb)*(256/Nb))+128/Nb);
				Color colori = new Color(ri, gi, bi);
				Color colorj = new Color(rj, gj, bj);
				
				A[i][j] = (float) (1-(Distance(colori,colorj))/(Math.sqrt(Math.pow(dr, 2) + Math.pow(dg, 2) + Math.pow(db, 2))));
			}
		}	
		
		return A;
		
	}
	
	public float Distance(Color c1, Color c2) {
		float dis = (float) Math.sqrt(
				Math.pow((c1.getRed() - c2.getRed()), 2 ) +
				Math.pow((c1.getGreen() - c2.getGreen()), 2 ) +
				Math.pow((c1.getBlue() - c2.getBlue()), 2 )
				);
		dis = (float) (dis / Math.sqrt( 3f * 255f *255f ));
		return dis;
	}
	
	public float getHisDistance(float[] h1, float[] h2){
		float HisDis=0;
		float[] tempHis = new float[h1.length];
		float[][] tempA = new float[h1.length][h1.length];
		tempA = A(h1,h2);
		float temp = 0;
		for(int i = 0; i < h1.length; i++){
			for(int j = 0; j < h1.length; j++){
				temp += h1[i] * tempA[j][i];
			}
			
			HisDis+= temp* h2[i];
		}
		return HisDis;
	}
	
}
