package edu.byuh.ldshistory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class ReadCSV {

	HashMap <Integer, Apostle> apostleList = new  HashMap <Integer, Apostle> (); 
	//private ArrayList<Apostle> apostleList = new ArrayList<Apostle> ();
	private Bitmap apostleImage;
	
	public ReadCSV(AssetManager am, Context mainActivity, int bWidth, int bHeight ) {
		
		ConvertToObject(am, mainActivity, bWidth, bHeight);
		//printApostleList();
	}
	
//	public void resizeAllImages(int w, int h) {
//		for (Apostle a : apostleList.values()) {
//			Bitmap oldImage = a.getPhoto();
//			Bitmap newImage = Bitmap.createScaledBitmap(oldImage, w, h, true);
//			a.setPhoto(newImage);
//		}
//	}
	
	public void ConvertToObject (AssetManager am, Context mainActivity, int bWidth, int bHeight ) {
		String CSVFile = "apostle"; // the csv file
		//AssetManager assetManager = getAssets();
		//BufferedReader br = null;
		//String line ="";
		String splitBy= "\\|";
		
		try{ 
			//br=new BufferedReader(new FileReader(CSVFile));
			InputStream is = am.open(CSVFile);
			//CSVReader csv = new CSVReader (new InputStreamReader(is));
			Scanner s = new Scanner(is);
			//while ((line = br.readLine()) != null ){
			while (s.hasNextLine()) {
				//split by comma, apostleAttributes comtain a single line of the csv file
				String line = s.nextLine();
				String[] apostleAttributes = line.split(splitBy);
				
				//instanciate a single apostle object
				Apostle apostle = new Apostle();
				//set the arrtibutes of each apostle
				apostle.setId(apostleAttributes[0]);
				Integer IDnumber  = new Integer (apostleAttributes[0]); // used for the hashmap
				apostle.setName(apostleAttributes[1]);
				apostle.setBirth(apostleAttributes[2]);
				apostle.setDeath(apostleAttributes[3]);
				//get the file name of the apostle picture and change it to a bitmap object
				apostle.resID = mainActivity.getResources().getIdentifier(apostleAttributes[4].trim(), "drawable", mainActivity.getPackageName());
				apostleImage = BitmapFactory.decodeResource(mainActivity.getResources(), apostle.resID);
				apostleImage = Bitmap.createScaledBitmap(apostleImage, bWidth, bHeight, true);
				
				apostle.setPhoto(apostleImage);
				
//				Log.d("the line",line);
//				Log.d("CS203",apostleAttributes[5]);
				try {
					//AssetManager assets = getResources().getAssets();
					//InputStream inputfile = assets.open("language-en/eventdesc");
					//Scanner s = new Scanner(inputfile);
					InputStream is1 = am.open(mainActivity.getResources().getString(R.string.language_option)+"/"+apostleAttributes[5]);
					
					int size = is1.available();//get the size of the file.by block,not very reliable
					byte[] buffer = new byte[size];
					is1.read(buffer);
					//change byte buffer to string
					String text = new String (buffer);
					apostle.setBio(text);
				}
				
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				catch (IOException e){
					e.printStackTrace();
				}
				//add each apostle object to the apostleList
				apostleList.put(IDnumber, apostle);
			}
			is.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
