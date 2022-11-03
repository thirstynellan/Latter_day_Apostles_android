package edu.byuh.ldshistory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ReadList {

	//a hash map that each key is the year of event, and the key points to the aposlte lis of that year
	HashMap <Calendar, ArrayList<Apostle>> presidencyList = new  HashMap <Calendar, ArrayList<Apostle> > ();
	HashMap <Calendar, ArrayList<Apostle>> twelveList = new  HashMap <Calendar, ArrayList<Apostle> > ();
	
	//HashMap <Long, ArrayList<Apostle>> presidencyList = new  HashMap <Long, ArrayList<Apostle> > ();
	//HashMap <Long, ArrayList<Apostle>> twelveList = new  HashMap <Long, ArrayList<Apostle> > ();
	
	public ReadList(AssetManager am, ReadCSV csv) {
		
		ConvertToObject(am, csv);
		//printApostleList();
	}
	
	public void ConvertToObject (AssetManager am, ReadCSV csv ){
		//Calendar daet = new GregorianCalendar(1832, 2, 8);
		//Log.d("Long date", ""+daet.getTimeInMillis());
		//Calendar dae = new GregorianCalendar(2014, 0, 1);
		//Log.d("Long date", ""+dae.getTimeInMillis());
		//Log.d("in calendar format", ""+dae.getTime());
		String CSVFile = "list"; // the csv file
		//AssetManager assetManager = getAssets();
		//BufferedReader br = null;
		//String line ="";
		String splitBy= "\\|";
		String splitBy1 = "%";
		
		try{ 
			InputStream is = am.open(CSVFile);
			Scanner s = new Scanner(is);
			while (s.hasNextLine()) {
				//split by comma, apostleAttributes comtain a single line of the csv file
				String line = s.nextLine();
				String[] listAttribtes = line.split(splitBy1);
				String list1= listAttribtes[0];
				//Log.d("text file split",list1);
				String[] presidency;
				presidency = list1.split(splitBy);
				for (int i=0; i<presidency.length; i++) {
					Log.d("prints" , presidency[i]);
				}
					
				String[] twelve = null;
				if (listAttribtes[1].length() != 0) {
					String list2 = listAttribtes[1];
					//Log.d("text file split",list2);
					twelve = list2.split(splitBy);
					/*for (int i=0; i<twelve.length; i++) {
						Log.d("prints" , twelve[i]);
					}*/
				}
				int year = Integer.parseInt(presidency[0].substring(0,4));
				int month = Integer.parseInt(presidency[0].substring(4,6));
				int day = Integer.parseInt(presidency[0].substring(6,8));
				//Log.d("prints the dates" , year+"/"+month+"/"+ day);
				Calendar dates = new GregorianCalendar(year,month-1,day);
				//String dateString = year+" "+month+" "+day;
				//Log.d("Date" , dateString);
				//Long date = dates.getTimeInMillis();
			
				//int year = 
				//list.setYear(year);
				//list.setMonth(month);
				//list.setDay(day);
				//convert to calendar 
				//month is 0 based when set this way
				//Calendar date = new GregorianCalendar(Integer.parseInt(dates[2]), Integer.parseInt(dates[0])-1, Integer.parseInt(dates[1]));
				//Log.d("The Calendar date is ", ""+date.getTime());
				//key of the hash map
				//Integer year = new Integer(dates[2]);
				ArrayList <Apostle> presidencies = new ArrayList <Apostle>();
				ArrayList <Apostle> twelves = new ArrayList <Apostle>();
				for (int i=1; i<presidency.length; i++) {//exclude presidency[0] which  is the year
					for (int j=1; j<=csv.apostleList.size();j++) {
						if (presidency[i].equals(csv.apostleList.get(j).getName()) ){
							//Log.d("the presidency list", csv.apostleList.get(j).getName());
							presidencies.add(csv.apostleList.get(j));//add that apostle to the apostles instead of instanciating new apostle
						}
					}
					
				}
				if(twelve != null){
				for (int i=0; i<twelve.length; i++) {
					for (int j=1; j<=csv.apostleList.size();j++) {
						if (twelve[i].equals(csv.apostleList.get(j).getName()) ){
							//Log.d("Robert D", csv.apostleList.get(j).getName());
							twelves.add(csv.apostleList.get(j));//add that apostle to the apostles instead of instanciating new apostle
						}
					}
					
				}
				}
				
				//add apostles(of each year) into the hashmap
				presidencyList.put(dates, presidencies);
				twelveList.put(dates,twelves);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	// use id number of each aposlt to link the list to the csv apostle, instead of name to avoid duplicated names. 
	// dilimiter in the list file to seperate the first presidency and the 12  
	//bigger lisde bar 
	//date picker 
//	public void printApostleList( ) {
//		Log.d("zise of the hash", ""+presidencyList.size());
//		for (int i =2014; i>=1977; i--) { 
//			for (int j=0; j<presidencyList.get(i).size(); j++){
//				Log.d("List","i="+ i + "j="+ j + "; "+presidencyList.get(i).get(j).getName());
//				}
//		}
//		for (int i =2014; i>=1977; i--) { 
//			for (int j=0; j<twelveList.get(i).size(); j++){
//				Log.d("List","i="+ i + "j="+ j + "; "+twelveList.get(i).get(j).getName());
//				}
//		}
//	}
	
	
	//line 62 -66 Lorenzo snow 7 joseph f smith
	//double check birthday from 107 and on

	
}
