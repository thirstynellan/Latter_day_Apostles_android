package edu.byuh.ldshistory;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class MyPreferences extends PreferenceActivity {

	public static final String OPT_ANIMATION = "animation";
	public static final String OPT_FAKETOAST = "faketoast";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}

	//Get the current value of the animation option
	public static boolean getAnimationPreference(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_ANIMATION, true);
	}

	//Get the current value of the faketoast option
	public static boolean getToastPreference(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_FAKETOAST, true);
	}

}
