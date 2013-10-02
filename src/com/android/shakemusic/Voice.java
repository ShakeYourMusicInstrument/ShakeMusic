package com.android.shakemusic;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class Voice {

	TextToSpeech tts;

	public Voice(Context ctx) {
		// TODO Auto-generated method stub
		final Context ctx2 = ctx;
		tts = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {

			public void onInit(int status) {
				// TODO Auto-generated method stub
				if (status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.US);
				}
				tts.speak(ctx2.getString(R.string.InstructionListen),
						TextToSpeech.QUEUE_FLUSH, null);
			}
		});
	}

}
