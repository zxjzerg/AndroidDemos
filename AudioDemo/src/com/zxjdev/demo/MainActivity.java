
package com.zxjdev.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zxjdev.demo.SoundMeter.RecordListener;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "AudioDemo";
    private Button mBtnStart;
    private Button mBtnStop;
    private Button mBtnPlay;
    private String mFileName;
    private MediaPlayer mPlayer;
    private SoundMeter mSoundMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnPlay = (Button) findViewById(R.id.btn_play);

        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);

        File rootCache = getExternalCacheDir();
        if (rootCache == null) {
            rootCache = getCacheDir();
        }

        File cacheDir = new File(rootCache, "audio.amr");
        cacheDir.getParentFile().mkdirs();
        try {
            cacheDir.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        mFileName = cacheDir.getAbsolutePath();
        Log.d(TAG, "cache file: " + mFileName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startRecording();
                break;
            case R.id.btn_stop:
                stopRecording();
                break;
            case R.id.btn_play:
                startPlaying();
                break;
            default:
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private void startRecording() {
        mSoundMeter = new SoundMeter(mFileName);
        mSoundMeter.setRecordListener(new RecordListener() {

            @Override
            public void onStop() {
                Log.d(TAG, "onStop()");
            }

            @Override
            public void onStart() {
                Log.d(TAG, "onStart()");
            }

            @Override
            public void onMaxDuration() {
                Log.d(TAG, "onMaxDuration()");
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError()");
            }
        });
        mSoundMeter.start();
    }

    private void stopRecording() {
        mSoundMeter.stop();
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            Log.d(TAG, "audio time: " + mPlayer.getDuration());
            mPlayer.start();
            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

}
