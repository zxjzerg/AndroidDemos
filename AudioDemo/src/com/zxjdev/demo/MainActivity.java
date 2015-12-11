package com.zxjdev.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zxjdev.demo.MediaPlayerHelper.RecordListener;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "AudioDemo";
    private static final String AUDIO_DIR = Environment.getExternalStorageDirectory()
            + "/AudioDemo/";

    /** MediaRecorder操作按钮 */
    private Button mBtnMrStart, mBtnMrStop, mBtnMrPlay;
    /** AudioRecord操作按钮 */
    private Button mBtnArStart, mBtnArStop, mBtnArPlay, mBtnArSuppressNoise;
    /** 示例语音播放 */
    private Button mBtnAudio1, mBtnAudio2, mBtnAudio3;
    private String mFileName;
    private MediaPlayer mPlayer;
    private MediaPlayerHelper mMrHelper;
    private AudioRecordHelper mArHelper;

    private boolean mSuppressNoise;

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

        File audioDir = new File(AUDIO_DIR);
        if (!audioDir.exists()) {
            if (!audioDir.mkdirs()) {
                Log.e("createCacheDirs", "Directory not created");
            }
        }
        mArHelper = new AudioRecordHelper();
    }

    private void initViews() {
        mBtnMrStart = (Button) findViewById(R.id.btn_mr_start);
        mBtnMrStop = (Button) findViewById(R.id.btn_mr_stop);
        mBtnMrPlay = (Button) findViewById(R.id.btn_play);
        mBtnAudio1 = (Button) findViewById(R.id.btn_audio_1);
        mBtnAudio2 = (Button) findViewById(R.id.btn_audio_2);
        mBtnAudio3 = (Button) findViewById(R.id.btn_audio_3);
        mBtnArStart = (Button) findViewById(R.id.btn_ar_start);
        mBtnArStop = (Button) findViewById(R.id.btn_ar_stop);
        mBtnArPlay = (Button) findViewById(R.id.btn_ar_play);
        mBtnArSuppressNoise = (Button) findViewById(R.id.btn_ar_suppress_noise);

        mBtnMrStart.setOnClickListener(this);
        mBtnMrStop.setOnClickListener(this);
        mBtnMrPlay.setOnClickListener(this);
        mBtnAudio1.setOnClickListener(this);
        mBtnAudio2.setOnClickListener(this);
        mBtnAudio3.setOnClickListener(this);
        mBtnArStart.setOnClickListener(this);
        mBtnArStop.setOnClickListener(this);
        mBtnArPlay.setOnClickListener(this);
        mBtnArSuppressNoise.setOnClickListener(this);

        mBtnArSuppressNoise.setText(mSuppressNoise ? "降噪开" : "降噪关");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mr_start:
                startMrRecording();
                break;
            case R.id.btn_mr_stop:
                stopMrRecording();
                break;
            case R.id.btn_play:
                startMrPlaying(mFileName);
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
            case R.id.btn_ar_start:
                startArRecording();
                break;
            case R.id.btn_ar_stop:
                stopArRecording();
                break;
            case R.id.btn_ar_play:
                startArPlaying();
                break;
            case R.id.btn_ar_suppress_noise:
                mSuppressNoise = !mSuppressNoise;
                mBtnArSuppressNoise.setText(mSuppressNoise ? "降噪开" : "降噪关");
                break;
            default:
                break;
        }
    }

    /** 使用MediaRecorder开始录制 */
    @SuppressLint("InlinedApi")
    private void startMrRecording() {
        mBtnMrStart.setEnabled(false);
        mMrHelper = new MediaPlayerHelper(mFileName);
        mMrHelper.setRecordListener(new RecordListener() {

            @Override
            public void onStop() {
                Log.d(TAG, "onStop()");
                mBtnMrStart.setEnabled(true);
            }

            @Override
            public void onStart() {
                Log.d(TAG, "onStart()");
            }

            @Override
            public void onMaxDuration() {
                Log.d(TAG, "onMaxDuration()");
                mBtnMrStart.setEnabled(true);
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError()");
                mBtnMrStart.setEnabled(true);
            }
        });
        mMrHelper.start();
    }

    /** 停止MediaRecorder录制 */
    private void stopMrRecording() {
        mMrHelper.stop();
    }

    /** 播放MediaRecorder音频 */
    private void startMrPlaying(String path) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(path);
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

    private void startArRecording() {
        mBtnArStart.setEnabled(false);
        mArHelper.start(AUDIO_DIR + "voice8K16bitmono.pcm", mSuppressNoise);
    }

    private void stopArRecording() {
        mArHelper.stop(AUDIO_DIR + "voice8K16bitmono.amr");
        mBtnArStart.setEnabled(true);
    }

    private void startArPlaying() {
        startMrPlaying(AUDIO_DIR + "voice8K16bitmono.amr");
    }

    private void playAudio(int rawFile) {
        final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), rawFile);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "onError" + what + extra);
                return false;
            }
        });
        mediaPlayer.start();
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
