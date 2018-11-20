package hw3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import pixeljelly.scanners.Location;
import pixeljelly.scanners.RasterScanner;

public class create {
	
	BufferedImage image;
	File URL_FILENAME;
	File DB_FILENAME;
	File RESPONSE_FILENAME;
	String[] urls;
	int NR;
	int NG;
	int NB;
	
	public create(File URL_FILENAME, File DB_FILENAME, int NR, int NG, int NB){
		this.URL_FILENAME = URL_FILENAME;
		this.DB_FILENAME = DB_FILENAME;
		this.NR = NR;
		this.NG = NG;
		this.NB = NB;
	
	}
	
	public void read(){
		int count = 0;
		try {
			Scanner scanner = new Scanner(URL_FILENAME);
			while(scanner.hasNextLine()){
				String temp = scanner.nextLine();
				count ++;				
			}
			urls = new String[count];
			 scanner = new Scanner(URL_FILENAME);
			//put all the urls into urls array
			while(scanner.hasNextLine()){
				String[] tokens;
				String tempUrl= scanner.nextLine();
				tokens =tempUrl.split(" ");
				for(int i=0;i<urls.length;i++){
					if(urls[i] == null){
						urls[i] = tokens[2];
					}
				}
				
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(){
		try {
			FileWriter outfile = new FileWriter (DB_FILENAME);
			outfile.write(NR + " " + NG + " " + NB + "\n");
			
			Scanner scanner = new Scanner(URL_FILENAME);
			int count=0;
			while( scanner.hasNextLine() ){
				count ++;
				String tempUrl = scanner.nextLine();
				outfile.write(tempUrl + " ");
				String[] tokens =tempUrl.split(" ");
				float[] tempHis = Histogram(tokens[2]);
				for(int j = 0; j < tempHis.length; j++){
					outfile.write(tempHis[j] + " ");
					
				}
				outfile.write("\n");
					
			}
			outfile.close(); 
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public float[] Histogram(String url){
		try {
			image = ImageIO.read(new URL(url));
			double R1,G1,B1;
			int Nr = (int) Math.pow(2, NR);
			int Ng = (int) Math.pow(2, NG);
			int	Nb = (int) Math.pow(2, NB);
			float[] histogram = new float[Nr*Ng*Nb];
			int index;
			for(Location pt: new RasterScanner(image,false)){
				R1 = Math.floor((new Color(image.getRGB(pt.col, pt.row)).getRed())*Nr/256);
				G1 = Math.floor((new Color(image.getRGB(pt.col, pt.row)).getGreen())*Ng/256);
				B1 = Math.floor((new Color(image.getRGB(pt.col, pt.row)).getBlue())*Nb/256);
				index = (int) (R1*(Ng*Nb)+G1*(Nb)+B1);
				histogram[index] ++;
			}
			
			for(int i = 0; i < histogram.length; i++){
				histogram[i] =  histogram[i]*1f/(image.getWidth()*image.getHeight());
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


}
