package com.zxjdev.demo;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.util.Log;

public class RecordAudioHelper {

	private static final String TAG = RecordAudioHelper.class.getSimpleName();
	private String mFileName;
	private MediaPlayer mPlayer;

	public RecordAudioHelper(File file) {
		mFileName = file.getAbsolutePath();
	}

	/*public int getAudioDuration() {
		if (mPlayer == null)
			mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			Log.d(TAG, "audio time: " + mPlayer.getDuration());
			mPlayer.start();
		} catch (IOException e) {
			Log.e(TAG, "prepare() failed");
		}
	}*/
}
