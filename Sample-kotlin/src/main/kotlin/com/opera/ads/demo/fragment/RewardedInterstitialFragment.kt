package com.opera.ads.demo.fragment

import com.opera.ads.AdError
import com.opera.ads.AdFormat
import com.opera.ads.RewardItem
import com.opera.ads.RewardSsvOptions
import com.opera.ads.demo.R
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAd
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAd.Companion.load
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAdInteractionListener
import com.opera.ads.rewardedinterstitial.RewardedInterstitialAdLoadListener

class RewardedInterstitialFragment : BaseFragment(
    AdFormat.REWARDED_INTERSTITIAL,
    true,
    true,
    R.string.rewarded_interstitial_ad_format
) {
    private var mRewardedInterstitialAd: RewardedInterstitialAd? = null

    override fun loadAd() {
        checkNotNull(placementId)
        logger.print("Loading ...")
        load(requireContext(), placementId!!, object : RewardedInterstitialAdLoadListener {
            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                mRewardedInterstitialAd = ad
                logger.print("Loaded")
                enableShowAd()
                enableDestroyAd()
            }

            override fun onAdFailedToLoad(error: AdError) {
                logger.print(error.message)
            }
        })
    }

    override fun showAd() {
        mRewardedInterstitialAd?.run {
            if (isAdInvalidated()) {
                logger.print("Ad is invalidated.")
                destroyAd()
                return
            }
            // Set scene ID and SSV options(if any) when S2S callback is set for Opera placement id.
            // scene id: max length 100 bytes after url encoded, or will be discarded.
            setSceneId("Demo scene #2")
            setRewardSsvOptions(
                RewardSsvOptions.Builder()
                    // user id: max length 100 bytes after url encoded, or will be discarded.
                    .userId("Demo user id %:{测试?}")
                    // custom data: max length 1KB after url encoded, or will be discarded.
                    .customData("Demo user custom data %:{测试?}#2")
                    .build()
            )
            show(requireActivity(), object : RewardedInterstitialAdInteractionListener {
                override fun onAdClicked() {
                    logger.print("Clicked!")
                }

                override fun onAdDisplayed() {
                    logger.print("Displayed!")
                }

                override fun onAdDismissed() {
                    logger.print("Dismissed")
                    destroyAd()
                }

                override fun onAdFailedToShow(error: AdError) {
                    logger.print(error.message)
                }

                override fun onUserRewarded(reward: RewardItem) {
                    logger.print("rewarded: type=${reward.type}, amount=${reward.amount}")
                }
            })
            enableDestroyAd()
            disableShowAd()
        }
    }

    override fun destroyAd() {
        mRewardedInterstitialAd?.destroy()
        mRewardedInterstitialAd = null
        logger.print("Destroyed...")
        disableDestroyAd()
        disableShowAd()
    }
}
