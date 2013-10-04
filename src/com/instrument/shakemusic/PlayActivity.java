package com.instrument.shakemusic;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PlayActivity extends ListActivity{
	
	private Cursor cursor;
	private ListAdapter adapter;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlayout);
		DBManager dbManager = new DBManager(this);
		db = dbManager.getWritableDatabase();
		cursor = db.rawQuery("SELECT * FROM " + DBManager.TABLE_NAME, null);
		adapter = new SimpleCursorAdapter(this, R.layout.list, cursor,
				new String[] {IO.NAME},
				new int[] { R.id.fileName }, 0);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		cursor = db.rawQuery("SELECT * FROM " + DBManager.TABLE_NAME, null);
		cursor.moveToPosition(position);
		MediaPlayer mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(cursor.getString(2));
			mPlayer.prepare();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        mPlayer.setOnCompletionListener(new OnCompletionListener() {

           @Override
           public void onCompletion(MediaPlayer mp) {
               // TODO Auto-generated method stub
               mp.release();
           }
        });   
        mPlayer.start();
	}
}
