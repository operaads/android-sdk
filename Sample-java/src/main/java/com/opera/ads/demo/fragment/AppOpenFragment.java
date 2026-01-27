package com.opera.ads.demo.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opera.ads.AdError;
import com.opera.ads.AdFormat;
import com.opera.ads.appopen.AppOpenAd;
import com.opera.ads.appopen.AppOpenAdInteractionListener;
import com.opera.ads.appopen.AppOpenAdLoadListener;
import com.opera.ads.demo.R;

public class AppOpenFragment extends BaseFragment {

    @Nullable
    private AppOpenAd mAppOpenAd;

    @Override
    protected boolean supportsMutedControl() {
        return true;
    }

    @NonNull
    @Override
    protected AdFormat getAdFormat() {
        return AdFormat.APP_OPEN;
    }

    @Override
    protected int getAdFormatStringId() {
        return R.string.app_open_ad_format;
    }

    @Override
    protected void loadAd() {
        assert mPlacementId != null;
        mLogView.print("Loading ...");
        AppOpenAd.load(getContext(), mPlacementId, new AppOpenAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                mAppOpenAd = ad;
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
        if (mAppOpenAd != null) {
            if (mAppOpenAd.isAdInvalidated()) {
                mLogView.print("Ad is invalidated.");
                destroyAd();
                return;
            }
            mAppOpenAd.show(requireActivity(), new AppOpenAdInteractionListener() {
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
        mAppOpenAd.destroy();
        mAppOpenAd = null;
        disableDestroyAd();
        disableShowAd();
    }
}
