package com.opera.ads.demo.fragment

import com.opera.ads.AdError
import com.opera.ads.AdFormat
import com.opera.ads.RewardItem
import com.opera.ads.RewardSsvOptions
import com.opera.ads.demo.R
import com.opera.ads.rewarded.RewardedAd
import com.opera.ads.rewarded.RewardedAd.Companion.load
import com.opera.ads.rewarded.RewardedAdInteractionListener
import com.opera.ads.rewarded.RewardedAdLoadListener

class RewardedFragment : BaseFragment(AdFormat.REWARDED, true, true, R.string.rewarded_ad_format) {
    private var mRewardedAd: RewardedAd? = null

    override fun loadAd() {
        checkNotNull(placementId)
        logger.print("Loading ...")
        load(requireContext(), placementId!!, object : RewardedAdLoadListener {
            override fun onAdLoaded(ad: RewardedAd) {
                mRewardedAd = ad
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
        mRewardedAd?.run {
            if (isAdInvalidated()) {
                logger.print("Ad is invalidated.")
                destroyAd()
                return
            }
            // Set scene ID and SSV options(if any) when S2S callback is set for Opera placement id.
            // scene id: max length 100 bytes after url encoded, or will be discarded.
            setSceneId("Demo scene #1")
            setRewardSsvOptions(
                RewardSsvOptions.Builder()
                    // user id: max length 100 bytes after url encoded, or will be discarded.
                    .userId("Demo user id %:{测试?}")
                    // custom data: max length 1KB after url encoded, or will be discarded.
                    .customData("Demo user custom data %:{测试?}#1")
                    .build()
            )
            show(requireActivity(), object : RewardedAdInteractionListener {
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
                    logger.print("rewarded: type=" + reward.type + ", amount=" + reward.amount)
                }
            })
            enableDestroyAd()
            disableShowAd()
        }
    }

    override fun destroyAd() {
        mRewardedAd?.destroy()
        mRewardedAd = null
        logger.print("Destroyed...")
        disableDestroyAd()
        disableShowAd()
    }
}
