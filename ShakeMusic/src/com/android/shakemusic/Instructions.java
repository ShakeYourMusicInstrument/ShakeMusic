package com.android.shakemusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Instructions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instruction);
	}
	
	public void Listen(View v) {
			Intent listen = new Intent(Instructions.this, Voice.class);
			startActivity(listen);
	}

}
