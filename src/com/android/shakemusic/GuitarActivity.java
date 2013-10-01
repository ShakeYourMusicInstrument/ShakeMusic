package com.android.shakemusic;

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
import android.widget.Button;

public class GuitarActivity extends Activity implements SensorEventListener{
	
	float gravity [] = new float [3];
	int linear_acceleration [] = new int [3];
	private final float alpha = 0.8f;
	
	private long last_movement = 0;
	
    private int prevX = 0;
    private int prevY = 0; 
    private int prevZ = 0;
    
    private int curX = 0;
    private int curY = 0;
    private int curZ = 0;
    
    private boolean recording;
    private IO record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guitarlayout);

		SensorManager mSensor;
		mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor.registerListener(this,
				mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		recording = false;
	}

	protected void onResume() {
		super.onResume();
		sound();
		
	}
	
	public void sound(){
		int length = 100;
		Guitar guitar = new Guitar(25, Instrument.NORM_BPM, length, 0.26);
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				Instrument.fs, AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, Instrument.fs * length / 2,
				AudioTrack.MODE_STATIC);
		audioTrack.write(guitar.Note(100), 0, (Instrument.fs) * length / 2);
		audioTrack.play();
	}

	public void onRecordClick(){
		if (!recording) {
			//start recorder
			recording = true;
			record = new IO();
			Button recButton = (Button) findViewById(R.id.recButton);
			recButton.setText(R.string.recButtonStop);
			recButton.setBackgroundColor(0xffff0000);
		}else{
			
			Intent play = new Intent(GuitarActivity.this, PlayActivity.class);
			startActivity(play);
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	int i = 0;

	@Override
	public void onSensorChanged(final SensorEvent event) {		
		// alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate
		synchronized(this){
    	   if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
   	        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
   	        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
   	        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
   	
   	        linear_acceleration[0] = (int)(event.values[0] - gravity[0]);
   	        linear_acceleration[1] = (int)(event.values[1] - gravity[1]);
   	        linear_acceleration[2] = (int)(event.values[2] - gravity[2]);		
   	        
   	        long current_time = event.timestamp;
   	        
   	        curX = linear_acceleration[0];
   	        curY = linear_acceleration[1];
   	        curZ = linear_acceleration[2];
           
           if (prevX == 0 && prevY == 0 && prevZ == 0) {
               last_movement = current_time;
               prevX = curX;
               prevY = curY;
               prevZ = curZ;
           }


           
//               System.out.println("******************************************************************************************");
//               System.out.println(current_time);
//               System.out.println(last_update);
           
           int time_difference = Math.abs(((int) ((event.timestamp) - last_update))/10);
//           System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//           System.out.println("Diferencia de tiempo: " + time_difference);
           if (time_difference > 0) {
               float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
               //float movementX = Math.abs((curX - prevX)/time_difference);
               
               float min_movement = (float) 1E-8;
               
               if (movement > min_movement) {
            	   System.out.println("******************************************************************************************");
                   System.out.println("Movement: " + movement);
                   //if (current_time - last_movement >= limit) {                    	
                      // Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
                       sound();
                   //}
                   last_movement = current_time;
               }
               prevX = curX;
               prevY = curY;
               prevZ = curZ;
           }
		synchronized (this) {
			// alpha is calculated as t / (t + dT)
			// with t, the low-pass filter's time-constant
			// and dT, the event delivery rate
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			linear_acceleration[0] = (int) (event.values[0] - gravity[0]);
			linear_acceleration[1] = (int) (event.values[1] - gravity[1]);
			linear_acceleration[2] = (int) (event.values[2] - gravity[2]);

			long current_time = event.timestamp;

			curX = linear_acceleration[0];
			curY = linear_acceleration[1];
			curZ = linear_acceleration[2];

			int time_difference = ((int) ((current_time) - last_movement)) / 1000000;
			System.out
					.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("Diferencia de tiempo: " + time_difference);
			float movement = Math.abs((curX + curY + curZ)
					- (prevX - prevY - prevZ))
					/ time_difference;
			int limit = 1500;
			float min_movement = (float) 1E-3;
			if (movement > min_movement) {
				// if (current_time - last_movement >= limit) {
				// Toast.makeText(getApplicationContext(),
				// "Hay movimiento de " + movement,
				// Toast.LENGTH_SHORT).show();
				onResume();
				// }
				last_movement = current_time;
			}
			prevX = curX;
			prevY = curY;
			prevZ = curZ;
		}

	}


}
