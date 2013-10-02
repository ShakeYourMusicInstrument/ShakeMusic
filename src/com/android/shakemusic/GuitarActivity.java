package com.android.shakemusic;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuitarActivity extends Activity implements SensorEventListener {


	private SensorManager mSensor;
	
	float gravity[] = new float[2];
	int linear_acceleration[] = new int[2];
	private final float alpha = 0.8f;

	private long last_update = 0;

	private int prevX = 0;
	private int prevZ = 0;

	private int curX = 0;
	private int curZ = 0;

	static boolean recording;
	static IO record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guitarlayout);
		findViewById(R.id.saveButton).setVisibility(View.GONE);
		findViewById(R.id.wavInfo).setVisibility(View.GONE);
		findViewById(R.id.wavName).setVisibility(View.GONE); 
		
		mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor.registerListener(this,
				mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		
		recording = false;
	}

	protected void onResume() {
		super.onResume();
		sound(100, 0);

	}

	public void sound(int freq, int length) {
		Guitar guitar = new Guitar(25, Instrument.NORM_BPM, length, 0.26);
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				Instrument.fs, AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, Instrument.fs * length / 2,
				AudioTrack.MODE_STATIC);
		audioTrack.write(guitar.Note(freq), 0, (Instrument.fs) * length / 2);
		audioTrack.play();
	}

	public void onRecordClick(View v) {
		Button recButton = (Button) findViewById(R.id.recButton);
		if (!recording) {
			// start recorder
			recording = true;
			record = new IO();
			recButton.setText(R.string.recButtonStop);
			recButton.setBackgroundColor(0xffff0000);
		} else {
			findViewById(R.id.saveButton).setVisibility(View.VISIBLE);
			findViewById(R.id.wavInfo).setVisibility(View.VISIBLE);
			findViewById(R.id.wavName).setVisibility(View.VISIBLE); 
			recButton.setVisibility(View.GONE);
			mSensor.unregisterListener(this);
		}
	}
	
	public void onSaveClick(View v){
		record.save(this,((TextView)findViewById(R.id.wavName)).getText().toString());
		Intent intent = new Intent(GuitarActivity.this, PlayActivity.class);
		startActivity(intent);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(final SensorEvent event) {
		// alpha is calculated as t / (t + dT)
		// with t, the low-pass filter's time-constant
		// and dT, the event delivery rate
		synchronized (this) {
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[2];

			linear_acceleration[0] = (int) (event.values[0] - gravity[0]);
			linear_acceleration[1] = (int) (event.values[2] - gravity[1]);

			long current_time = event.timestamp;

			curX = linear_acceleration[0];
			curZ = linear_acceleration[1];

			if (prevX == 0 && prevZ == 0) {
				last_update = current_time;
				prevX = curX;
				prevZ = curZ;
			}

			// System.out.println("******************************************************************************************");
			// System.out.println(current_time);
			// System.out.println(last_update);

			int time_difference = Math
					.abs(((int) ((event.timestamp) - last_update)) / 100000);
			// System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			// System.out.println("Diferencia de tiempo: " +
			// time_difference);
			if (time_difference > 0) {
				float movement = Math.abs((curX + curZ) - (prevX - prevZ))
						/ time_difference;
				// float movementX = Math.abs((curX -
				// prevX)/time_difference);

				float min_movement = (float) 1E-16;

				if (movement > min_movement) {
					if ((curX > curZ) && (curX > 1)) {
						sound(261, time_difference);
					} else if ((curZ > curX) && curZ > 1) {
						sound(1660, time_difference);
					} else if ((curX > curZ) && (curX < 1)) {
						sound(50, time_difference);
					} else if ((curZ > curX) && curZ < 1) {
						sound(700, time_difference);
					}

				}
				prevX = curX;
				prevZ = curZ;
			}

		}
	}
}
