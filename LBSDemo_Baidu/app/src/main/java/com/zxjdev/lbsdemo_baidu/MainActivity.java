package com.zxjdev.lbsdemo_baidu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MainActivity extends Activity {

    public LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d("Location", "Error Code: " + bdLocation.getLocType());
                Log.d("Location",
                        "Latitude: " + bdLocation.getLatitude() + "; Longitude: " + bdLocation.getLongitude());
            }
        });
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }
}
