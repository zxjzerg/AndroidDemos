package com.zxjdev.demo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Created by 赵骁俊 on 2015/8/26.
 */
public class AudioRecordHelper {

    private AudioRecord mRecord;

    private void init() {
        int min = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, min);
    }

    public void start() {
        mRecord.startRecording();
    }

    public void stop() {
        mRecord.stop();
        mRecord.release();
    }
}
