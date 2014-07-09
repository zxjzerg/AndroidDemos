/**
 * http://www.cnblogs.com/jun-it/articles/2881826.html
 */

package com.example.cropimagedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String TEMP_FILE = "temp/photo.jpg";
    private Uri mImageUri;
    private static final int TAKE_BIG_PICTURE = 0x01;
    private static final int CROP_BIG_PICTURE = 0x03;
    private ImageView ivShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        ivShow = (ImageView) findViewById(R.id.iv_show);

        File rootCache = getExternalCacheDir();
        if (rootCache == null) {
            Log.v("Volley", "Can't find External Cache Dir, " + "switching to application specific cache directory");
            rootCache = getCacheDir();
        }

        File cacheDir = new File(rootCache, TEMP_FILE);
        cacheDir.getParentFile().mkdirs();
        try {
            cacheDir.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageUri = Uri.fromFile(cacheDir);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, TAKE_BIG_PICTURE);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_BIG_PICTURE:
                Log.d(TAG, "TAKE_BIG_PICTURE: data = " + data);// it seems to be null
                // ��ͼƬ�ü�Ϊ640*640
                cropImageUri(mImageUri, 640, 640, CROP_BIG_PICTURE);
                break;
            case CROP_BIG_PICTURE:// from crop_big_picture
                Log.d(TAG, "CROP_BIG_PICTURE: data = " + data);// it seems to be null
                if (mImageUri != null) {
                    Bitmap bitmap = decodeUriAsBitmap(mImageUri);
                    ivShow.setImageBitmap(bitmap);
                }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true"); // �Ƿ�ü�
        intent.putExtra("aspectX", 1); // �������
        intent.putExtra("aspectY", 1); // �������
        intent.putExtra("outputX", outputX); // ����ļ��Ŀ��
        intent.putExtra("outputY", outputY); // ����ļ��ĸ߶�
        intent.putExtra("scale", true); // �Ƿ�����
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // �������ΪUri������File
        intent.putExtra("return-data", false); // �Ƿ񷵻�Bitmap
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // ����ļ�����
        intent.putExtra("noFaceDetection", true); // ����ʶ��
        startActivityForResult(intent, requestCode);
    }
}
