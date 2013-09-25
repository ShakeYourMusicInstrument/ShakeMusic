package com.android.shakemusic;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		/*
		 * onClick Guitar
		 */
		ImageButton guitar = (ImageButton)findViewById(R.id.guitar);
	  	guitar.setOnClickListener(new OnClickListener() {
	  	public void onClick(View v) {
	  			Intent guitar = new Intent(MainActivity.this, GuitarActivity.class);
	  			startActivity(guitar);
	  	}});
	  	
	  	/*
		 * onClick Play
		 */
		ImageButton play = (ImageButton)findViewById(R.id.play);
	  	play.setOnClickListener(new OnClickListener() {
	  	public void onClick(View v) {
	  			Intent play = new Intent(MainActivity.this, PlayActivity.class);
	  			startActivity(play);
	  	}});
	  	
	  	/*
		 * onClick Piano
		 */
		ImageButton piano = (ImageButton)findViewById(R.id.piano);
	  	piano.setOnClickListener(new OnClickListener() {
	  	public void onClick(View v) {
	  			Intent piano = new Intent(MainActivity.this, PianoActivity.class);
	  			startActivity(piano);
	  	}});
	  	
	  	
	  	return true;
		
	}

}
