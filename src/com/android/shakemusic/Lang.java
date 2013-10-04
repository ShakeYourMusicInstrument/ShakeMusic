package com.android.shakemusic;

import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Lang extends PreferenceActivity {

	 Locale myLocale;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lang);

	}

	@Override
	protected void onListItemClick (ListView l, View v, int pos, long id) {
		if (pos == 1) {
			 
            Toast.makeText(l.getContext(),
                    "You have selected English", Toast.LENGTH_SHORT)
                    .show();
            setLocale("en");
        } else if (pos == 2) {

            Toast.makeText(l.getContext(),
                    "You have selected Albanian", Toast.LENGTH_SHORT)
                    .show();
            setLocale("al");
        } else if (pos == 3) {

            Toast.makeText(l.getContext(),
                    "You have selected Spanish", Toast.LENGTH_SHORT)
                    .show();
            setLocale("es");
        }
	}
	
	 public void setLocale(String lang) {
		 
	        myLocale = new Locale(lang);
	        Resources res = getResources();
	        DisplayMetrics dm = res.getDisplayMetrics();
	        Configuration conf = res.getConfiguration();
	        conf.locale = myLocale;
	        res.updateConfiguration(conf, dm);
	        Intent refresh = new Intent(this, Lang.class);
	        startActivity(refresh);
	    }

}
