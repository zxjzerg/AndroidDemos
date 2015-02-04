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
    private Button mBtnAudio1;
    private Button mBtnAudio2;
    private Button mBtnAudio3;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

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

    private void initViews() {
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnAudio1 = (Button) findViewById(R.id.btn_audio_1);
        mBtnAudio2 = (Button) findViewById(R.id.btn_audio_2);
        mBtnAudio3 = (Button) findViewById(R.id.btn_audio_3);

        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnAudio1.setOnClickListener(this);
        mBtnAudio2.setOnClickListener(this);
        mBtnAudio3.setOnClickListener(this);
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
            case R.id.btn_audio_1:
                playAudio(R.raw.audio1);
                break;
            case R.id.btn_audio_2:
                playAudio(R.raw.audio2);
                break;
            case R.id.btn_audio_3:
                playAudio(R.raw.audio3);
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

    private void playAudio(int rawFile) {
        mPlayer = MediaPlayer.create(getApplicationContext(), rawFile);
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.reset();
                mPlayer.release();
                mPlayer = null;
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "onError" + what + extra);
                return false;
            }
        });
        mPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
