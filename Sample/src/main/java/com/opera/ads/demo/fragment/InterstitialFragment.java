package com.opera.ads.demo.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opera.ads.AdError;
import com.opera.ads.AdFormat;
import com.opera.ads.demo.R;
import com.opera.ads.interstitial.InterstitialAd;
import com.opera.ads.interstitial.InterstitialAdInteractionListener;
import com.opera.ads.interstitial.InterstitialAdLoadListener;

public class InterstitialFragment extends BaseFragment {

    @Nullable
    private InterstitialAd mInterstitialAd;

    @Override
    protected boolean hasVideo() {
        return true;
    }

    @NonNull
    @Override
    protected AdFormat getAdFormat() {
        return AdFormat.INTERSTITIAL;
    }

    @Override
    protected int getAdFormatStringId() {
        return R.string.interstitial_ad_format;
    }

    @Override
    protected void loadAd() {
        assert mPlacementId != null;
        mLogView.print("Loading ...");
        InterstitialAd.load(getContext(), mPlacementId, new InterstitialAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                mInterstitialAd = ad;
                mLogView.print("Loaded");
                enableShowAd();
                enableDestroyAd();
            }

            @Override
            public void onAdFailedToLoad(@NonNull AdError error) {
                mLogView.print(error.getMessage());
            }
        });
    }

    @Override
    protected void showAd() {
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isAdInvalidated()) {
                mLogView.print("Ad is invalidated.");
                destroyAd();
                return;
            }
            mInterstitialAd.show(requireActivity(), new InterstitialAdInteractionListener() {
                @Override
                public void onAdClicked() {
                    mLogView.print("Clicked!");
                }

                @Override
                public void onAdDisplayed() {
                    mLogView.print("Displayed!");
                }

                @Override
                public void onAdDismissed() {
                    mLogView.print("Dismissed");
                    destroyAd();
                }

                @Override
                public void onAdFailedToShow(@NonNull AdError error) {
                    mLogView.print(error.getMessage());
                }
            });
            enableDestroyAd();
            disableShowAd();
        }
    }

    @Override
    protected void destroyAd() {
        super.destroyAd();
        mInterstitialAd.destroy();
        mInterstitialAd = null;
        disableDestroyAd();
        disableShowAd();
    }
}
