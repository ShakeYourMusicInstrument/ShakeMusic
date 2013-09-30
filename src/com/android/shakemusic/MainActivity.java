package com.android.shakemusic;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater=getMenuInflater();
	    inflater.inflate(R.menu.main, menu);	
	    return true;
		
	}
		/*
		 * onClick Guitar
		 */
		
	  	public void onClickGuitar(View v) {
	  			Intent guitar = new Intent(MainActivity.this, GuitarActivity.class);
	  			startActivity(guitar);
	  	}
	  	
	  	/*
		 * onClick Play
		 */
		
	  	public void onClickPlay(View v) {
	  			Intent play = new Intent(MainActivity.this, PlayActivity.class);
	  			startActivity(play);
	  	}
	  	
	  	/*
		 * onClick Piano
		 */
		
	  	public void onClickPiano(View v) {
	  			Intent piano = new Intent(MainActivity.this, PianoActivity.class);
	  			startActivity(piano);
	  	}
	  	

}
