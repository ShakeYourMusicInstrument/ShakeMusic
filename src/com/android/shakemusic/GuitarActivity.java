package com.android.shakemusic;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;

public class GuitarActivity extends Activity implements SensorEventListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guitarlayout);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111111111");
				onResume();
				System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222");
			}
		}).start();
		
		SensorManager mSensor;
		mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mSensor.registerListener(this, mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	                SensorManager.SENSOR_DELAY_UI);
	    
	}
	
	protected void onResume(){
		super.onResume();
		int length = 100;
		Guitar guitar = new Guitar(25, Instrument.NORM_BPM, length, 0.26);
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
				Instrument.fs, AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, Instrument.fs*length/2,
				AudioTrack.MODE_STATIC);
		audioTrack.write(guitar.Note(100), 0, (Instrument.fs)*length/2);
		audioTrack.play();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            refreshValues(event);
        }
	}
	
	private void refreshValues(SensorEvent event) {
		float values[] = event.values;
		int x = 0;
		int y = 0;
		int z = 0;
		
		
		if(x != (int)values[0]){
			x = (int)values[0];
			//System.out.println("x: " + x);
		}else if(y != (int)values[1]){
			y = (int)values[1];
			//System.out.println("y: " +y);
		}else if(z != (int)values[2]){
			z = (int)values[2];
			//System.out.println("z: " + z);	
		}
		//onResume(Math.abs(x*y));
	}
	

}
