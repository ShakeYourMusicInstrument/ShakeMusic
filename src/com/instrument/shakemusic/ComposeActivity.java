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

import com.android.shakemusic.R;

public class ComposeActivity extends Activity implements SensorEventListener {

	private int instrument;
	
	private SensorManager mSensor;
	
	float gravity[] = new float[2];
	int linear_acceleration[] = new int[2];
	private final float alpha = 0.8f;

	private long last_update = 0;

	private int prevX = 0;
	private int prevZ = 0;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensor.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensor.registerListener(this,
				mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);

	}

	private int curX = 0;
	private int curZ = 0;

	static boolean recording;
	static IO record;
	
	private AudioTrack audioTrack;
	private int countMovement = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.composelayout);
		findViewById(R.id.saveButton).setVisibility(View.GONE);
		findViewById(R.id.wavInfo).setVisibility(View.GONE);
		findViewById(R.id.wavName).setVisibility(View.GONE); 
		instrument = this.getIntent().getIntExtra("INSTRUMENT", -1);
		
		mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
		recording = false;
		onResume();
	}

	public void sound(int freq, int length) {
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				Instrument.fs, AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, Instrument.fs * length*10 / 2,
				AudioTrack.MODE_STATIC);
		if (instrument == Instrument.GUITAR){
			Guitar guitar = new Guitar(Instrument.NORM_BPM, length*10, 0.26);
			audioTrack.write(guitar.Note(freq), 0, (Instrument.fs) * length*10 / 2);
		}else{
			Piano piano = new Piano(Instrument.NORM_BPM, length*10);
			audioTrack.write(piano.Note(freq), 0, (Instrument.fs) * length*10 / 2);			
		}
		audioTrack.play();
		
	}
	
	public void stop(){
		audioTrack.stop();
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
		Intent intent = new Intent(ComposeActivity.this, PlayActivity.class);
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

			int time_difference = Math
					.abs(((int) ((current_time) - last_update)) / 100000000);

			if (time_difference > 0) {
				float movement = Math.abs((curX + curZ) - (prevX + prevZ));

				float min_movement = (float) 10;

				if (movement > min_movement) {
					countMovement++;
					prevX = curX;
					prevZ = curZ;
					last_update = current_time;

					if (countMovement % 2 == 1) {
						
						if((curX <= 0) && (curZ > 0)){
							if(-curX <= curZ){
								sound(261, time_difference);
							}else{
								sound(293, time_difference);
							}
						}else if((curX > 0) && (curZ >= 0)){
							if(curX <= curZ ){
								sound(329, time_difference);
							}else{
								sound(349, time_difference);
							}
						}else if((curX < 0) && (curZ <= 0)){
							if(-curX > -curZ ){
								sound(392, time_difference);
							}else{
								sound(440, time_difference);
							}
						}else if((curX >= 0) && (curZ < 0)){
							if(curX < -curZ ){
								sound(494, time_difference);
							}else{
								sound(523, time_difference);
							}
						}
						

					} else {
						stop();
					}
				}
			}
		}
	}
}
