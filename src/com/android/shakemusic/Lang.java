package com.android.shakemusic;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Lang extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lang);
		
	}

}
