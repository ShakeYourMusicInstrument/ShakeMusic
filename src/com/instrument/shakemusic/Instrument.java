package com.instrument.shakemusic;

public interface Instrument {

	// General waveform parameters
	
	final int fs = 44100;
	final double inharmonity = 0.01;
	final double gam = 1.7;
	final double duration = 0.125;

	final int NORM_BPM = 80;
	final int HIGH_BPM = 120;
	final int LOW_BPM = 60;

	// Instrument definition
	
	final String INSTRUMENT = "INSTRUMENT";
	
	final int GUITAR = 0;
	final int PIANO = 1;
	
	// Note definition
	
	final int DO4 = 0;
	final int RE4 = 1;
	final int MI4 = 2;
	final int FA4 = 3;
	final int SOL4 = 4;
	final int LA4 = 5;
	final int SI4 = 6;
	final int DO5 = 7;
	
	final int [] pitch = {262, 294, 330, 349, 392, 440, 494, 523};
	
	// Math helpers
	
	final double pi = Math.PI;
	final double pisqr = pi * pi;

	public byte[] Note(int note);
	public void CreateInstrument();

}
