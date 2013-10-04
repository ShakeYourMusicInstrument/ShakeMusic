package com.android.shakemusic;

import java.util.Locale;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class Lang extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lang);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 1:

			Locale locale = new Locale("us");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			Toast.makeText(this, "Locale in English!", Toast.LENGTH_LONG).show();
			break;

		case 2:

			Locale locale2 = new Locale("al");
			Locale.setDefault(locale2);
			Configuration config2 = new Configuration();
			config2.locale = locale2;
			getBaseContext().getResources().updateConfiguration(config2,
					getBaseContext().getResources().getDisplayMetrics());
			Toast.makeText(this, "Lokale në Shqip!", Toast.LENGTH_LONG).show();
			break;

		case 3:

			Locale locale3 = new Locale("es");
			Locale.setDefault(locale3);
			Configuration config3 = new Configuration();
			config3.locale = locale3;
			getBaseContext().getResources().updateConfiguration(config3,
					getBaseContext().getResources().getDisplayMetrics());
			Toast.makeText(this, "Local en Espagna!", Toast.LENGTH_LONG).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
