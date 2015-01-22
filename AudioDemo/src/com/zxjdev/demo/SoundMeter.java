
package com.zxjdev.demo;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class SoundMeter {
    private static final String TAG = SoundMeter.class.getSimpleName();
    private static final double EMA_FILTER = 0.6;
    private static final int DEFAULT_MAX_DURATION = 60 * 1000;

    private String mPath;
    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;
    private RecordListener mListener;

    /** user SoundMeter(String path) instead **/
    @Deprecated
    public SoundMeter() {

    }

    public SoundMeter(String path) {
        mPath = path;
        mRecorder = new MediaRecorder();
        if (TextUtils.isEmpty(mPath)) {
            Log.e(TAG, "file path is empty!");
        }
    }

    private void init() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mPath);
        mRecorder.setMaxDuration(DEFAULT_MAX_DURATION);
        mRecorder.setOnInfoListener(new OnInfoListener() {
            public void onInfo(MediaRecorder arg0, int what, int arg2) {
                switch (what) {
                    case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                        Log.e(TAG, "Max duration reached");
                        if (mListener != null)
                            mListener.onMaxDuration();
                        mRecorder.release();
                        break;
                }
            }
        });
    }

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }

        mRecorder.reset();
        init();
        try {
            mRecorder.prepare();
            mRecorder.start();
            mEMA = 0.0;
            if (mListener != null)
                mListener.onStart();
        } catch (IllegalStateException e) {
            Log.e(TAG, "error in start()", e);
            if (mListener != null)
                mListener.onError();
        } catch (IOException e) {
            Log.e(TAG, "error in start()", e);
            if (mListener != null)
                mListener.onError();
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        if (mListener != null)
            mListener.onStop();
    }

    public void pause() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    interface RecordListener {
        void onStart();

        void onStop();

        void onMaxDuration();

        void onError();
    }

    public void setRecordListener(RecordListener listener) {
        mListener = listener;
    }

    /** user start() instead **/
    @Deprecated
    public void start(String name) {
        if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return;
        }
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(android.os.Environment.getExternalStorageDirectory() + "/plato/" + name);
            mRecorder.setMaxDuration(60 * 1000);
            mRecorder.setOnInfoListener(new OnInfoListener() {

                public void onInfo(MediaRecorder arg0, int what, int arg2) {
                    switch (what) {
                        case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                            Log.e("SoundMeter", "Max duration reached");
                            break;
                    }
                }
            });
            try {
                mRecorder.prepare();
                mRecorder.start();

                mEMA = 0.0;
            } catch (IllegalStateException e) {
                System.out.print(e.getMessage());
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }
        }
    }
}
