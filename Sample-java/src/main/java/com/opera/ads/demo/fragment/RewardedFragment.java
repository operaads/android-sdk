package com.opera.ads.demo.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opera.ads.AdError;
import com.opera.ads.AdFormat;
import com.opera.ads.demo.R;
import com.opera.ads.RewardItem;
import com.opera.ads.rewarded.RewardedAd;
import com.opera.ads.rewarded.RewardedAdInteractionListener;
import com.opera.ads.rewarded.RewardedAdLoadListener;

public class RewardedFragment extends BaseFragment {

    @Nullable
    private RewardedAd mRewardedAd;

    @Override
    protected boolean hasVideo() {
        return false;
    }

    @NonNull
    @Override
    protected AdFormat getAdFormat() {
        return AdFormat.REWARDED;
    }

    @Override
    protected int getAdFormatStringId() {
        return R.string.rewarded_ad_format;
    }

    @Override
    protected void loadAd() {
        assert mPlacementId != null;
        mLogView.print("Loading ...");
        RewardedAd.load(getContext(), mPlacementId, new RewardedAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                mRewardedAd = ad;
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
        if (mRewardedAd != null) {
            if (mRewardedAd.isAdInvalidated()) {
                mLogView.print("Ad is invalidated.");
                destroyAd();
                return;
            }
            mRewardedAd.show(requireActivity(), new RewardedAdInteractionListener() {
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
        mRewardedAd.destroy();
        mRewardedAd = null;
        disableDestroyAd();
        disableShowAd();
    }
}
