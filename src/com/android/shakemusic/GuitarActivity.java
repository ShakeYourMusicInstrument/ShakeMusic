package com.android.shakemusic;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class GuitarActivity extends Activity implements SensorEventListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guitarlayout);
		
		SensorManager mSensor;
		mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mSensor.registerListener(this, mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	                SensorManager.SENSOR_DELAY_UI);
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
		
		for(int i = 0; i < 10; i++){
		
			if(x != (int)values[0]){
				x = (int)values[0];
				System.out.println("x: " + x);
			}else if(y != (int)values[1]){
				y = (int)values[1];
				System.out.println("y: " +y);
			}else if(z != (int)values[2]){
				z = (int)values[2];
				//System.out.println("z: " + z);	
			}
		}
	}
	

}
