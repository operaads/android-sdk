package com.opera.ads.demo

import android.app.Application
import android.util.Log
import com.opera.ads.AdError
import com.opera.ads.OperaAds
import com.opera.ads.demo.util.Constant
import com.opera.ads.initialization.OnSdkInitCompleteListener
import com.opera.ads.initialization.SdkInitConfig

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        OperaAds.initialize(
            this,
            SdkInitConfig.Builder(Constant.APPLICATION_ID)
                .publisherName("Opera")
                .build(),
            object : OnSdkInitCompleteListener {
                override fun onSuccess() {
                    // OK.
                }

                override fun onError(error: AdError) {
                    Log.e("DemoApplication", "Failed to init ad sdk: " + error.message)
                }
            }
        )
    }
}
