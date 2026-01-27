package com.opera.ads.demo.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opera.ads.AdError;
import com.opera.ads.AdFormat;
import com.opera.ads.RewardItem;
import com.opera.ads.RewardSsvOptions;
import com.opera.ads.demo.R;
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAdInteractionListener;
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAdLoadListener;

public class RewardedInterstitialFragment extends BaseFragment {

    @Nullable
    private RewardedInterstitialAd mRewardedInterstitialAd;

    @Override
    protected boolean supportsMutedControl() {
        return true;
    }

    @NonNull
    @Override
    protected AdFormat getAdFormat() {
        return AdFormat.REWARDED_INTERSTITIAL;
    }

    @Override
    protected int getAdFormatStringId() {
        return R.string.rewarded_interstitial_ad_format;
    }

    @Override
    protected void loadAd() {
        assert mPlacementId != null;
        mLogView.print("Loading ...");
        RewardedInterstitialAd.load(getContext(), mPlacementId, new RewardedInterstitialAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                mRewardedInterstitialAd = ad;
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
        if (mRewardedInterstitialAd != null) {
            if (mRewardedInterstitialAd.isAdInvalidated()) {
                mLogView.print("Ad is invalidated.");
                destroyAd();
                return;
            }
            // Set scene ID and SSV options(if any) when S2S callback is set for Opera placement id.
            // scene id: max length 100 bytes after url encoded, or will be discarded.
            mRewardedInterstitialAd.setSceneId("Demo scene #2");
            mRewardedInterstitialAd.setRewardSsvOptions(new RewardSsvOptions.Builder()
                    // user id: max length 100 bytes after url encoded, or will be discarded.
                    .userId("Demo user id %:{测试?}")
                    // custom data: max length 1KB after url encoded, or will be discarded.
                    .customData("Demo user custom data %:{测试?}#2")
                    .build());
            mRewardedInterstitialAd.show(requireActivity(), new RewardedInterstitialAdInteractionListener() {
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

                @Override
                public void onUserRewarded(@NonNull RewardItem reward) {
                    mLogView.print("rewarded: type=" + reward.type + ", amount=" + reward.amount);
                }
            });
            enableDestroyAd();
            disableShowAd();
        }
    }

    @Override
    protected void destroyAd() {
        super.destroyAd();
        mRewardedInterstitialAd.destroy();
        mRewardedInterstitialAd = null;
        disableDestroyAd();
        disableShowAd();
    }
}
