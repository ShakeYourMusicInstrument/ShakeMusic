package com.instrument.shakemusic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class IO {

	private File path;
	private File sndFile;

	private OutputStream outStream;
	private int dataLength;

	private final byte[] header = { 'R', 'I', 'F', 'X', 0, 0, 0, 0, 'W', 'A',
			'V', 'E', 'f', 'm', 't', ' ', 16, 0, 0, 0, 1, 0, 2, 0, 40, 0x1f, 0,
			0, 80, 0x3e, 0, 0, 4, 0,/* 35 */16, 0, 'd', 'a', 't', 'a' };
	private final byte[] riff = { 'R', 'I', 'F', 'X' };

	public static final String NAME = "name";
	public static final String PATH = "path";

	public IO() {
		path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		sndFile = new File(path.getAbsolutePath(), "temp");
		try {
			outStream = new FileOutputStream(sndFile);
			outStream.write(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean write(byte[] data, int length) {
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

	public boolean save(Context ctx, String name) {
		try {
			outStream.close();
			outStream = new FileOutputStream(sndFile);
			outStream.write(riff);
			byte[] bytes = new byte[4];
			for (int i = 0; i < 4; i++) {
				bytes[i] = (byte) (dataLength >>> (i * 8));
			}
			outStream.write(bytes);
			outStream.write(header, 8, 32);
			outStream.write(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		name = name + ".wav";
		sndFile.renameTo(new File(path.getAbsolutePath(), name));
		DBManager dbManager = new DBManager(ctx);
		final SQLiteDatabase db = dbManager.getWritableDatabase();
		dbManager.onCreate(db);
		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(PATH, sndFile.getAbsolutePath());
		db.insert(DBManager.TABLE_NAME, null, values);
		return true;
	}

	public File getSndFile() {
		return sndFile;
	}

}
