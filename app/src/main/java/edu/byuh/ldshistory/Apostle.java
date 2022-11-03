package edu.byuh.ldshistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.text.format.DateFormat;

public class Apostle {
	
	private String name;
//	private Calendar birth;
//	private Calendar death;
	private String birth;
	private String death;
	private Bitmap photo ;
	private String bio;
	private int id;
	int resID;

	public Apostle() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Apostle) {
			Apostle o = (Apostle)other;
			return (this.name.equals(o.name) && this.birth.equals(o.birth));
		}
		return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getDeath() {
		return death;
	}

	public void setDeath(String death) {
		this.death = death;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getId() {
		return id;
	}

	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}

//	public HashMap<Calendar, String> getTitle() {
//		return titles;
//	}
//
//	public void setTitle(Calendar date, String title) {
//		this.titles.put(date, title);
//	}

//	public HashMap<Integer, Event> getEvent() {
//		return events; 
//	}
//
//	public void setEvent(Integer id, Event event) {
//		
//		events.put(id, event);
//	}
	
	
	
	

}
