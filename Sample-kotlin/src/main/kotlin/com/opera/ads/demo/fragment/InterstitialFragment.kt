package com.opera.ads.demo.fragment

import com.opera.ads.AdError
import com.opera.ads.AdFormat
import com.opera.ads.demo.R
import com.opera.ads.interstitial.InterstitialAd
import com.opera.ads.interstitial.InterstitialAd.Companion.load
import com.opera.ads.interstitial.InterstitialAdInteractionListener
import com.opera.ads.interstitial.InterstitialAdLoadListener

class InterstitialFragment : BaseFragment(
    AdFormat.INTERSTITIAL,
    true,
    true,
    R.string.interstitial_ad_format
) {
    private var mInterstitialAd: InterstitialAd? = null

    override fun loadAd() {
        checkNotNull(placementId)
        logger.print("Loading ...")
        load(requireContext(), placementId!!, object : InterstitialAdLoadListener {
            override fun onAdLoaded(ad: InterstitialAd) {
                mInterstitialAd = ad
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
        mInterstitialAd?.run {
            if (isAdInvalidated()) {
                logger.print("Ad is invalidated.")
                destroyAd()
                return
            }
            show(requireActivity(), object : InterstitialAdInteractionListener {
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
            })
            enableDestroyAd()
            disableShowAd()
        }
    }

    override fun destroyAd() {
        mInterstitialAd?.destroy()
        mInterstitialAd = null
        logger.print("Destroyed...")
        disableDestroyAd()
        disableShowAd()
    }
}
