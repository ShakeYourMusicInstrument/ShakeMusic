package com.instrument.shakemusic;

public interface Instrument {

	final int fs = 44100;
	final double inharmonity = 0.01;
	final double gam = 1.7;
	final double pi = Math.PI;
	final double pisqr = pi * pi;
	
	final int NORM_BPM = 80;
	final int HIGH_BPM = 120;
	final int LOW_BPM = 60;
	
	final String INSTRUMENT = "INSTRUMENT";
	final int GUITAR = 3;
	final int PIANO = 9;
	
	public byte[] Note(int freq);
	
}
