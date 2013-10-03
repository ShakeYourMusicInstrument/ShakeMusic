package com.android.shakemusic;

public class Guitar implements Instrument{

	private int nHarm;
	private int bpm;
	private int duration;
	private double pluck_location;
	private byte generatedSnd[];

	Guitar(int bpm, int duration, double pluck_location) {
		this.bpm = bpm;
		this.duration = duration;
		this.pluck_location = pluck_location;
	}

	public byte[] Note(int freq) {
		nHarm = (int) Math.round(fs/(2.0*freq));
		int Nt, jh;
		double An[] = new double [nHarm];
		double dfn[] = new double[nHarm];
		Nt = (int) (fs * duration / bpm);
		double sj[] = new double[Nt];
		generatedSnd= new byte[(int) (fs*duration*2)];
		// Make Note
		int i, j, jsqr;
		for (i = 0; i < nHarm; i++) {
			j = i+1;
			jsqr = j*j;
			An[i] = 2 / ((pisqr) * jsqr * pluck_location * (1 - pluck_location))* Math.sin(j * pi * pluck_location);
			dfn[i] = Math.sqrt(1 + jsqr * inharmonity * inharmonity);
		}
		for (i = 0; i < Nt; i++) {
			sj[i] = 0;
			for (j = 0; j < nHarm; j++) {
				jh = j + 1;
				sj[i] += (An[j] * Math.exp(-gam * jh * i / fs) * Math.sin(2 * pi * i * freq * dfn[j] * jh / fs));
//				System.out.println("***************************************************************************************+");
//				System.out.println(An);
//				System.out.println(dfn);
//				System.out.println(sj[i]);

			}
		}
//		
//		
//		for (i = 0; i < Nt; ++i) {
//			sj[i] = Math.sin(2 * Math.PI * i / (8000 / freq));
//		}
		
		
		int idx = 0;
        for (final double dVal : sj) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
        if(GuitarActivity.recording){
			new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					GuitarActivity.record.write(generatedSnd, duration);
				}
			});
		}
		return generatedSnd;
	}

}
