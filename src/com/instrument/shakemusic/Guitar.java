package com.instrument.shakemusic;

public class Guitar implements Instrument {

	private int nHarm;
	private double pluck_location;
	private byte notes[][];

	Guitar(double pluck_location) {
		this.pluck_location = pluck_location;
		notes = new byte[8][];
	}

	public void CreateNote(int freq, int note) {

		//Calculates the number of harmonics for that frequency
		nHarm = (int) Math.round(fs / (freq));

		int Nt, jh;
		double An[] = new double[nHarm];
		double dfn[] = new double[nHarm];
		Nt = (int) (fs * duration);
		double sj[] = new double[Nt];
		notes[note] = new byte[(int) (fs * duration * 2)];
		int i, j, jsqr;
		
		// Make Note
		
		for (i = 0; i < nHarm; i++) {
			j = i + 1;
			jsqr = j * j;
			An[i] = 2
					/ ((pisqr) * jsqr * pluck_location * (1 - pluck_location))
					* Math.sin(j * pi * pluck_location);
			dfn[i] = Math.sqrt(1 + jsqr * inharmonity * inharmonity);
		}

		for (i = 0; i < Nt; i++) {
			sj[i] = 0;
			for (j = 0; j < nHarm; j++) {
				jh = j + 1;
				sj[i] += (An[j] * Math.exp(-gam * jh * i / fs) * Math.sin(2
						* pi * i * freq * dfn[j] * jh / fs));
			}
		}

		i = 0;
		for (final double dVal : sj) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			notes[note][i++] = (byte) (val & 0x00ff);
			notes[note][i++] = (byte) ((val & 0xff00) >>> 8);

		}
//
//		if (ComposeActivity.recording) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					ComposeActivity.record.write(generatedSnd, duration);
//				}
//			});
//		}
	}
	
	public byte[] Note(int note){
		return notes[note];
	}

	@Override
	public void CreateInstrument() {
		// TODO Auto-generated method stub
		for (int i = 0;i<8;i++){
			CreateNote(pitch[i], i);
		}
	}

}
