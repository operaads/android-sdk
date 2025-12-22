package com.opera.ads.demo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.opera.ads.AdError;
import com.opera.ads.OperaAds;
import com.opera.ads.demo.util.Constant;
import com.opera.ads.initialization.OnSdkInitCompleteListener;
import com.opera.ads.initialization.SdkInitConfig;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OperaAds.initialize(this,
                new SdkInitConfig.Builder(Constant.APPLICATION_ID)
                        .publisherName("Opera")
                        .build(),
                new OnSdkInitCompleteListener() {
                    @Override
                    public void onSuccess() {
                        // OK.
                    }

                    @Override
                    public void onError(@NonNull AdError error) {
                        Log.e("DemoApplication", "Failed to init ad sdk: " + error.getMessage());
                    }
                });
    }
}
