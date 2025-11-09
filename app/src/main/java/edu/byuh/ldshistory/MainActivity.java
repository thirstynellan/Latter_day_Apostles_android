package edu.byuh.ldshistory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends Activity implements OnSeekBarChangeListener,View.OnClickListener {

	private final static String STATE_KEY = "STATE";

	private JView jv;
	private SeekBar timeSlider;
	private ImageButton fowButton;
	private ImageButton bacButton;

	public static int STARTING_YEAR = 1832;
	public static int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
	private HashMap<Calendar, String> texts = new HashMap();
	private Calendar dates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		log("at the top of onCreate");

		// set the seekBar
		setContentView(R.layout.main);

		jv = (JView) findViewById(R.id.jView1);
		int diff = CURRENT_YEAR - STARTING_YEAR;
		timeSlider = (SeekBar) findViewById(R.id.seekBar1);
		timeSlider.setMax(diff);
		timeSlider.setProgress(diff);
		timeSlider.setOnSeekBarChangeListener(this);

		if (savedInstanceState == null) {
			log("No saved date; defaulting to today's date");
			jv.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
			jv.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			jv.setYear(CURRENT_YEAR);
		} else {
			final String savedDate = savedInstanceState.getString(STATE_KEY);
			final String[] parts = savedDate.split("/");
			log("restoring date: " + savedDate);
			jv.setYear(Integer.parseInt(parts[0]));
			jv.setMonth(Integer.parseInt(parts[1]));
			jv.setDay(Integer.parseInt(parts[2]));
		}

		fowButton = (ImageButton) findViewById(R.id.fowbutton);
		bacButton = (ImageButton) findViewById(R.id.bacbutton);

		fowButton.setOnClickListener((android.view.View.OnClickListener) this);
		bacButton.setOnClickListener((android.view.View.OnClickListener) this);
		readEvent();
		updateTextView();
		log("at the bottom of onCreate");

		//Due to Edge-to-Edge: move the toasts down, so they appear
		//below the toolbar
		ViewCompat.setOnApplyWindowInsetsListener(jv, (v, windowInsets) -> {
			Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			mlp.leftMargin = insets.left;
			mlp.topMargin = insets.top;
			mlp.rightMargin = insets.right;
			v.setLayoutParams(mlp);
			return WindowInsetsCompat.CONSUMED;
		});

		//Due to Edge-to-Edge: Move the slider up, so it appears
		//above the navigation bar.
		ViewCompat.setOnApplyWindowInsetsListener(timeSlider, (v, windowInsets) -> {
			Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
			// Apply the insets as a margin to the view. This solution sets only the
			// bottom, left, and right dimensions, but you can apply whichever insets are
			// appropriate to your layout. You can also update the view padding if that's
			// more appropriate.
			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			mlp.leftMargin = insets.left;
			mlp.bottomMargin = insets.bottom;
			mlp.rightMargin = insets.right;
			v.setLayoutParams(mlp);

			// Return CONSUMED if you don't want the window insets to keep passing
			// down to descendant views.
			return WindowInsetsCompat.CONSUMED;
		});
	}



//	@Override
//	public void onConfigurationChanged (Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		Log.d("CS203", "inside onConfigurationChanged!!!!");
//	}

	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		//Log.d("CS203", "rotating!!!");
		int y = jv.getYear();
		int m = jv.getMonth();
		int d = jv.getDay();
		outState.putString(STATE_KEY, y + "/" + m + "/" + d);
		log("Fold/unfold detected! Saving current date: " + y + "/" + m + "/" + d);
	}


	@Override
	public void onResume() {
		log("at the top of onResume");
		super.onResume();
		jv.cancelFakeToast();
		log("at the bottom of onResume");
	}

//	@Override
//	public void onStart() {
//		super.onStart();
//		log("inside onStart");
//	}
//
//	@Override
//	public void onRestart() {
//		super.onRestart();
//		log("inside onRestart");
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		log("inside ondestroy");
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
//		log("inside onStop");
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		log("at the top of onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);
		log("at the bottom of onCreateOptionsMenu");
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {
		if (fromUser) {
			//only set the month and day if the user touched the
			//slider bar. If done via the calendar widget, the
			//month and day are set in onOptionsItemSelected.
			if (progress == 0) {
				//if user sets slider to 1832, go to March 8.
				jv.setMonth(3);
				jv.setDay(8);
			} else if (progress == (CURRENT_YEAR-STARTING_YEAR)) {
				//if user sets slider to current year, go to today's date.
				jv.setMonth(Calendar.getInstance().get(Calendar.MONTH)+1);
				jv.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			} else {
				jv.setMonth(1);
				jv.setDay(1); //default the slider bar as the first day of each year
			}
		}
		jv.setYear(progress + STARTING_YEAR);
		jv.respondToDateChange();
		updateTextView();
		//enable back and forward button
		bacButton.setEnabled(true);
		fowButton.setEnabled(true);
	}
	public void updateTextView() {
		int year = jv.getYear();
		int day = jv.getDay();
		int month =jv.getMonth();
		Calendar dates = new GregorianCalendar(year, month - 1, day);
		//set max and min bounds disabling button
		String month_name = dates.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.getDefault());
		TextView tv = (TextView) findViewById(R.id.textView2);
		tv.setText(day
				+ " " +
				month_name
				+ " " +
				year);
	}

	public void setYear(int year) {
		//FIXME dirty hack; duplicate code from onProgressChanged.
		if (year == STARTING_YEAR) {
			//if user sets slider to 1832, go to March 8.
			jv.setMonth(3);
			jv.setDay(8);
		} else if (year == CURRENT_YEAR) {
			//if user sets slider to current year, go to today's date.
			jv.setMonth(Calendar.getInstance().get(Calendar.MONTH)+1);
			jv.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		} else {
			jv.setMonth(1);
			jv.setDay(1);//default the slider bar as the first day of each year
		}
		timeSlider.setProgress(year-STARTING_YEAR);
		Log.d("mytest", ""+(year-STARTING_YEAR));
		//make the same with fow, back button
		updateTextView();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_go_to_today:
				// start datePicker with dialog box
				DatePickerDialog dpd = new DatePickerDialog(this,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
												  int monthOfYear, int dayOfMonth) {
								//Log.d("CS203", "inside ondateset");
								jv.setMonth(monthOfYear+1);//the month is sends to JView is 0 based
								jv.setDay(dayOfMonth);
								//no need to call jv.setYear b/c onProgressChanged does that.
								//sync with sliderbar
								if (timeSlider.getProgress() == (year-STARTING_YEAR)) {
									//if the user is changing the day or month but not the year,
									//then calling timeSlider.setProgress() will not trigger
									//a call to onProgressChanged. So force the jview to
									//update manually.
									jv.respondToDateChange();
									updateTextView();

								} else {
									timeSlider.setProgress(year - STARTING_YEAR);
								}
							}

						}, CURRENT_YEAR, 1, 1);

				dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
				dpd.getDatePicker().setMinDate(-4349080800000L);//need api 11 up, time calculates by Unix time,
				//use calendar object and turn to a long and
				//get the the seconds , before 1970 is negative, after is positive
				//-4256373600000L
				dpd.show();
				dpd.updateDate(jv.getYear(),jv.getMonth()-1, jv.getDay());
				break;

			case R.id.action_about://method chaining
				new AlertDialog.Builder(this)
						.setTitle(R.string.action_about)
						.setMessage(R.string.about_detail)
						.setNeutralButton(R.string.ok,null)
						.show();
				break;
//			case R.id.action_prefs:
//				Intent i = new Intent(this, MyPreferences.class);
//				startActivity(i);
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	//time progress button
	@Override
	public void onClick(View v) {
		Date dateShown = new Date(jv.getYear(),jv.getMonth(),jv.getDay());
		Date dateBegan = new Date(1832,3,8);

		switch (v.getId()) {
			case R.id.bacbutton:
				fowButton.setEnabled(true);
				cancelToast();
				if (dateShown.after(dateBegan)){
					jv.goBack();
					updateTextView();
					String text = texts.get(jv.getDate());
					timeSlider.setProgress(jv.getYear()-STARTING_YEAR);
					showToast(text);
					//Log.d("test", ""+jv.getYear());
				}else {
					bacButton.setEnabled(false);
					showToast(R.string.toast_begin);
				} break;
			case R.id.fowbutton:
				cancelToast();
				bacButton.setEnabled(true);
				if(jv.goForward()==false){
					setYear(CURRENT_YEAR);
					showToast(R.string.toast_current);
					fowButton.setEnabled(false);
				} else{
					String text = texts.get(jv.getDate());
					timeSlider.setProgress(jv.getYear()-STARTING_YEAR);
					showToast(text);
				}
				updateTextView();
		}
	}

	private void showToast(CharSequence words) {
		jv.createFakeToast(words);
		Log.d("***FAKE_TOAST***", words.toString());
	}

	private void cancelToast() {
		jv.cancelFakeToast();
	}

	public void showToast(int id) {
		showToast(getString(id));
	}

	public void readEvent(){
		try {
			AssetManager assets = getResources().getAssets();
			InputStream inputfile = assets.open(getResources().getString(R.string.language_option)+"/eventdesc");
			Scanner s = new Scanner(inputfile);
			while(s.hasNext()){
				String line = s.nextLine();
				String splitBy= "\\|";
				String[] events = line.split(splitBy);
				int year = Integer.parseInt(events[0].substring(0, 4));
				int month = Integer.parseInt(events[0].substring(4, 6));
				int day = Integer.parseInt(events[0].substring(6, 8));
				//Log.d("prints the dates" , year+"/"+month+"/"+ day);
				dates = new GregorianCalendar(year,month-1,day);
				//Log.d("CS203", "" +events[1]);
				texts.put(dates, events[1]);
			}
			s.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public static float findThePerfectFontSize(float dim) {
		float fontSize = 1;
		Paint p = new Paint();
		p.setTextSize(fontSize);
		while (true) {
			float asc = -p.getFontMetrics().ascent;
			if (asc > dim) {
				break;
			}
			fontSize++;
			p.setTextSize(fontSize);
		}
		return fontSize;
	}

	private void log(String s) {
		Log.d("CS203", s);
	}

}