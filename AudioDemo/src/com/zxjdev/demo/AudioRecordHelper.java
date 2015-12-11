package com.zxjdev.demo;

import android.annotation.TargetApi;
import android.media.AmrInputStream;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioRecordHelper {

    private AudioRecord mRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private int bufferSize = 0;
    int BufferElements2Rec = 1024;  // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2;        // 2 bytes in 16bit format
    String mFilePath;
    private Speex mSpeex;

    public AudioRecordHelper() {
        mSpeex = new Speex();
        mSpeex.init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void start(final String filePath, boolean isNoiseSuppress) {
        bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        if (isNoiseSuppress) {
            NoiseSuppressor.create(mRecord.getAudioSessionId());
        }

        mFilePath = filePath;
        mRecord.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile(filePath);
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    public void stop(String amrPath) {
        if (mRecord != null) {
            isRecording = false;
            mRecord.stop();
            mRecord.release();
            pcm2Amr(mFilePath, amrPath);
        }
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    /*private void writeAudioDataToFile(String outputPath) {
        // Write the output audio in byte

        // String filePath = AUDIO_DIR + "voice8K16bitmono.pcm";
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(outputPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            mRecord.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void pcm2Amr(String pcmPath, String amrPath) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(pcmPath);
            pcm2Amr(fis, amrPath);
            fis.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pcm2Amr(InputStream pcmStream, String amrPath) {
        try {
            AmrInputStream ais = new AmrInputStream(pcmStream);
            OutputStream out = new FileOutputStream(amrPath);
            byte[] buf = new byte[4096];
            int len = -1;
            /*
             * 下面的AMR的文件头,缺少这几个字节是不行的
             */
            out.write(0x23);
            out.write(0x21);
            out.write(0x41);
            out.write(0x4D);
            out.write(0x52);
            out.write(0x0A);
            while ((len = ais.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            ais.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAudioDataToFile(String filePath) {
        byte data[] = new byte[bufferSize];
        short sData[] = new short[bufferSize];
        // String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if (null != os) {
            while (isRecording) {
                // read = mRecord.read(data, 0, bufferSize);
                read = mRecord.read(sData, 0, bufferSize);
                mSpeex.encode(sData, 0, data, 1);
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
