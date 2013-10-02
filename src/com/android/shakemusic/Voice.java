package com.android.shakemusic;

import java.util.Locale;
import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;

public class Voice extends Activity implements OnClickListener {

	static final String[] texts = {
		"Listen to the instruction", "Shake your phone", "Test"
	};
	TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		tts = new TextToSpeech(Voice.this, new TextToSpeech.OnInitListener() {
			
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if (status !=TextToSpeech.ERROR){
					tts.setLanguage(Locale.US);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(tts !=null){
			tts.stop();
			tts.shutdown();
		}
		super.onPause();
	}

	public void onClick(View v){
		Random r = new Random();
		String random = texts[r.nextInt()];
		tts.speak(random, TextToSpeech.QUEUE_FLUSH, null);
		
	}

}
