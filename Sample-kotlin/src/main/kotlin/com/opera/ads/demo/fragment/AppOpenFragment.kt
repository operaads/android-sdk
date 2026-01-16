package com.opera.ads.demo.fragment

import com.opera.ads.AdError
import com.opera.ads.AdFormat
import com.opera.ads.appopen.AppOpenAd
import com.opera.ads.appopen.AppOpenAd.Companion.load
import com.opera.ads.appopen.AppOpenAdInteractionListener
import com.opera.ads.appopen.AppOpenAdLoadListener
import com.opera.ads.demo.R

class AppOpenFragment : BaseFragment(AdFormat.APP_OPEN, true, true, R.string.app_open_ad_format) {
    private var appOpenAd: AppOpenAd? = null

    override fun loadAd() {
        checkNotNull(placementId)
        logger.print("Loading ...")
        load(
            requireContext(),
            placementId!!,
            object : AppOpenAdLoadListener {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    logger.print("Loaded")
                    enableShowAd()
                    enableDestroyAd()
                }

                override fun onAdFailedToLoad(error: AdError) {
                    logger.print(error.message)
                }
            }
        )
    }

    override fun showAd() {
        appOpenAd?.run {
            if (isAdInvalidated()) {
                logger.print("Ad is invalidated.")
                destroyAd()
                return
            }
            show(requireActivity(), object : AppOpenAdInteractionListener {
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
        appOpenAd?.destroy()
        appOpenAd = null
        logger.print("Destroyed...")
        disableDestroyAd()
        disableShowAd()
    }
}
