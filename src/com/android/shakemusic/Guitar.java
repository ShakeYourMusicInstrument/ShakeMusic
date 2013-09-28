package com.android.shakemusic;

public class Guitar implements Instrument{

	private int nHarm;
	private int bpm;
	private double duration;
	private double pluck_location;

	Guitar(int nHarm, int bpm, double duration, double pluck_location) {
		this.nHarm = nHarm;
		this.bpm = bpm;
		this.duration = duration;
		this.pluck_location = pluck_location;
	}

	public byte[] Note(int freq) {
		int Nt, jh, jhsqr;
		double T, An, dfn;
		T = duration / bpm;
		Nt = (int) (fs * T);
		double sj[] = new double[Nt];
		byte generatedSnd[] = new byte[(int) (fs*duration*2)];
		// Make Note
		int i, j;
		for (i = 0; i < Nt; i++) {
			sj[i] = 0;
			for (j = 0; j < nHarm; j++) {
				jh = j + 1;
				jhsqr = jh * jh;
				An = 2 / ((pisqr) * jhsqr * pluck_location * (1 - pluck_location))* Math.sin(jh * pi * pluck_location);
				dfn = Math.sqrt(1 + jhsqr * inharmonity * inharmonity);
				sj[i] += (An * Math.exp(-gam * jh * i / fs) * Math.sin(2 * pi * i * freq * dfn * jh / fs));
				System.out.println("***************************************************************************************+");
				System.out.println(An);
				System.out.println(dfn);
				System.out.println(sj[i]);

			}
		}
		
		
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
		return generatedSnd;
	}

}
