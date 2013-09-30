package com.android.shakemusic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.Environment;

public class IO {
	
	private File path;
	private File sndFile;
    private OutputStream outStream;
	private int dataLength;
	
	private final byte[] header = {'R','I','F','F',0,0,0,0,'W','A','V','E','f','m','t',' ',16,0,0,0,1,0,2,0,40,0x1f,0,0,80,0x3e,0,0,4,0,/*35*/16,0,'d','a','t','a'};
	
	public IO(){
		path = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_MUSIC);
	    sndFile = new File(path.getAbsolutePath(), "temp");
	    try {
	    	outStream = new FileOutputStream(sndFile);
	    	outStream.write(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	public boolean write(byte []data, int length){
		this.dataLength += length;
		try {
	    	outStream.write(data);
	    	return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	
	public boolean save(String name){
		try {
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sndFile.renameTo(new File(path.getAbsolutePath(), name+".wav"));
		return false;		
	}
	
}
