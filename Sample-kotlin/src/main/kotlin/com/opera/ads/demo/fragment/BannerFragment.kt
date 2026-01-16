package com.opera.ads.demo.fragment

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.opera.ads.AdError
import com.opera.ads.AdFormat
import com.opera.ads.AdSize
import com.opera.ads.banner.BannerAd
import com.opera.ads.banner.BannerAdListener
import com.opera.ads.banner.BannerAdView
import com.opera.ads.demo.R

class BannerFragment : BaseFragment(AdFormat.BANNER, true, true, R.string.banner_ad_format) {
    private var bannerAdView: BannerAdView? = null

    override fun loadAd() {
        destroyAd()
        if (bannerAdView == null) {
            bannerAdView = BannerAdView(requireContext())
        }
        bannerAdView?.run {
            logger.print("Loading ...")
            placementId = this@BannerFragment.placementId
            adSize = AdSize.BANNER_MREC
            loadAd(
                object : BannerAdListener {
                    override fun onAdLoaded(ad: BannerAd) {
                        logger.print("Loaded ${ad.adSize}, refreshCount: ${ad.refreshCount}")
                        enableShowAd()
                        enableDestroyAd()
                    }

                    override fun onAdFailedToLoad(error: AdError) {
                        logger.print("Error: ${error.message}")
                    }

                    override fun onAdImpression() {
                        logger.print("Impression!")
                    }

                    override fun onAdClicked() {
                        logger.print("Clicked!")
                    }
                }
            )
        }
    }

    override fun showAd() {
        bannerAdView?.run {
            if (isAdInvalidated()) {
                logger.print("Ad is invalidated")
                destroyAd()
                return
            }
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            adContainer().addView(this, layoutParams)
            enableDestroyAd()
            disableShowAd()
        }
    }

    override fun destroyAd() {
        bannerAdView?.destroy()
        bannerAdView = null
        adContainer().removeAllViews()
        logger.print("Destroyed...")
        disableShowAd()
        disableDestroyAd()
    }

    override fun onPause() {
        super.onPause()
        bannerAdView?.pause()
    }

    override fun onResume() {
        super.onResume()
        bannerAdView?.resume()
    }

    override fun onDestroy() {
        bannerAdView?.destroy()
        bannerAdView = null
        super.onDestroy()
    }
}
