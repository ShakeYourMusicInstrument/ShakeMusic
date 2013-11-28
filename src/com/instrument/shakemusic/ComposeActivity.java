package com.instrument.shakemusic;

import android.app.Activity;
import android.content.Intent;
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

public class ComposeActivity extends Activity implements SensorEventListener {

	private SensorManager mSensor;
	
	// Parameters to filter and eliminate gravity
	 	
	private float gravity[] = new float[2];
	private int linear_acceleration[] = new int[2];
	private final float alpha = 0.8f;

	// Parameters to control accelerometer data
	
	private long last_update = 0;

	private int curX = 0;
	private int curZ = 0;

	// Music parameters
	
	private int instrument;
	private Instrument []instruments = new Instrument[2];
	
	private AudioTrack audioTrack;

	// IO parameters
	
	static boolean recording;
	static IO record;

	/*
	 * Activity related methods
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.composelayout);
		
		// Sets information about how to play

		instrument = this.getIntent().getIntExtra("INSTRUMENT", -1);
		if (instrument == Instrument.GUITAR) {
			instruments[instrument] = new Guitar(0.26);
		}else{
			instruments[instrument] = new Piano();
		}		
		instruments[instrument].CreateInstrument();
		
		//set Sensor
		
		mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		//set recording state
		
		recording = false;

		//start
		
		onResume();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//start listener addition 
		
		mSensor.registerListener(this,
				mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensor.unregisterListener(this);
	}

	/*
	 * Sound related methods
	 */
	
	private void sound(int note) {
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Instrument.fs,
				AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT,
				(int) (Instrument.fs * Instrument.duration * 10 / 2), AudioTrack.MODE_STATIC);
		audioTrack.write(instruments[instrument].Note(note), 0, (int) (Instrument.fs * Instrument.duration * 2));
		audioTrack.play();

	}

	private void stop() {
		audioTrack.stop();
	}

	/*
	 * onClick methods
	 */
	
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

	public void onSaveClick(View v) {
		record.save(this, ((TextView) findViewById(R.id.wavName)).getText()
				.toString());
		Intent intent = new Intent(ComposeActivity.this, PlayActivity.class);
		startActivity(intent);
	}

	/*
	 * Sensor related methods
	 */
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(final SensorEvent event) {
		synchronized (this) {

			// Start calculation

			long current_time = event.timestamp;

			int time_difference = Math.abs((int) ((current_time) - last_update));

			if (time_difference > 125) {

				// Low pass filter

				gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
				gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[2];

				linear_acceleration[0] = (int) (event.values[0] - gravity[0]);
				linear_acceleration[1] = (int) (event.values[2] - gravity[1]);

				curX = linear_acceleration[0];
				curZ = linear_acceleration[1];
				
				last_update = current_time;

				if (Math.abs(event.values[1]) < Math.abs(curX + curZ)) {

					if ((curX <= 0) && (curZ > 0)) {
						if (-curX <= curZ) {
							sound(Instrument.DO4);
						} else {
							sound(Instrument.RE4);
						}
					} else if ((curX > 0) && (curZ >= 0)) {
						if (curX <= curZ) {
							sound(Instrument.MI4);
						} else {
							sound(Instrument.FA4);
						}
					} else if ((curX < 0) && (curZ <= 0)) {
						if (-curX > -curZ) {
							sound(Instrument.SOL4);
						} else {
							sound(Instrument.LA4);
						}
					} else if ((curX >= 0) && (curZ < 0)) {
						if (curX < -curZ) {
							sound(Instrument.SI4);
						} else {
							sound(Instrument.DO5);
						}
					}

				} else {
					stop();
				}

			}
		}
	}
}
