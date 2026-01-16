package com.opera.ads.demo.fragment;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.opera.ads.AdError;
import com.opera.ads.AdSize;
import com.opera.ads.banner.BannerAd;
import com.opera.ads.banner.BannerAdListener;
import com.opera.ads.banner.BannerAdView;
import com.opera.ads.demo.R;

public class BannerFragment extends BaseFragment {

    private BannerAdView mBannerAdView;

    @Override
    protected boolean hasVideo() {
        return true;
    }

    @Override
    protected int getAdFormatStringId() {
        return R.string.banner_ad_format;
    }

    @Override
    protected void loadAd() {
        destroyAd();
        if (mBannerAdView == null) {
            mBannerAdView = new BannerAdView(getContext());
        }
        mLogView.print("Loading ...");
        final BannerAdListener adListener = new BannerAdListener() {
            @Override
            public void onAdLoaded(@NonNull BannerAd bannerAd) {
                mLogView.print("Loaded " + bannerAd.adSize + ", refreshCount: " + bannerAd.refreshCount);
                enableShowAd();
                enableDestroyAd();
            }

            @Override
            public void onAdFailedToLoad(@NonNull AdError error) {
                mLogView.print("Error: " + error.getMessage());
            }

            @Override
            public void onAdImpression() {
                mLogView.print("Impression!");
            }

            @Override
            public void onAdClicked() {
                mLogView.print("Clicked!");
            }
        };
        mBannerAdView.setPlacementId(mPlacementId);
        mBannerAdView.setAdSize(AdSize.BANNER_MREC);
        mBannerAdView.loadAd(adListener);
    }

    @Override
    protected void showAd() {
        if (mBannerAdView != null) {
            if (mBannerAdView.isAdInvalidated()) {
                mLogView.print("Ad is invalidated");
                destroyAd();
                return;
            }
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            mAdContainer.addView(mBannerAdView, layoutParams);
            enableDestroyAd();
            disableShowAd();
        }
    }

    @Override
    protected void destroyAd() {
        super.destroyAd();
        if (mBannerAdView != null) mBannerAdView.destroy();
        mBannerAdView = null;
        mAdContainer.removeAllViews();
        disableShowAd();
        disableDestroyAd();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerAdView != null) mBannerAdView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBannerAdView != null) mBannerAdView.resume();
    }

    @Override
    public void onDestroy() {
        if (mBannerAdView != null) mBannerAdView.destroy();
        mBannerAdView = null;
        super.onDestroy();
    }
}
