package com.zxjdev.github.demo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andrew on 1/10/16.
 */
public class FirstActivity extends FragmentActivity implements View.OnClickListener {

    public static final String TAG = FirstActivity.class.getSimpleName();

    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                showFragment(Fragment1.class, "Fragment1");
                break;
            case R.id.btn_2:
                showFragment(Fragment2.class, "Fragment2");
                break;
            case R.id.btn_3:
                showFragment(Fragment3.class, "Fragment3");
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showFragment(Class<? extends Fragment> fragmentClass, String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            try {
                isFragmentExist = false;
                fragment = fragmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                Log.e(TAG, "", e);
                return;
            }
        }
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isFragmentExist) {
            ft.replace(R.id.flyt_container, fragment);
        } else {
            ft.replace(R.id.flyt_container, fragment, tag);
        }

        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    public static class Fragment1 extends Fragment {

        private String mTest;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "Fragment1 onCreate");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d(TAG, "Fragment1 onCreateView");
            Log.d(TAG, "mTest is null?" + (mTest == null));
            if (mTest == null) mTest = "Fragment";
            return inflater.inflate(R.layout.fragment_1, container, false);
        }
    }

    public static class Fragment2 extends Fragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "Fragment2 onCreate");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d(TAG, "Fragment2 onCreateView");
            return inflater.inflate(R.layout.fragment_2, container, false);
        }
    }

    public static class Fragment3 extends Fragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "Fragment3 onCreate");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d(TAG, "Fragment3 onCreateView");
            return inflater.inflate(R.layout.fragment_3, container, false);
        }
    }
}
