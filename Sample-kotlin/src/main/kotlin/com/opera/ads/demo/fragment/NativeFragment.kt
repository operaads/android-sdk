package com.opera.ads.demo.fragment

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.opera.ads.AdError
import com.opera.ads.AdFormat
import com.opera.ads.demo.R
import com.opera.ads.demo.databinding.NativeAdLayoutBinding
import com.opera.ads.nativead.NativeAd
import com.opera.ads.nativead.NativeAdListener
import com.opera.ads.nativead.NativeAdLoader

class NativeFragment : BaseFragment(AdFormat.NATIVE, false, false, R.string.native_ad_format) {
    private var nativeAd: NativeAd? = null

    override fun loadAd() {
        logger.print("Loading...")
        NativeAdLoader.loadAd(
            requireContext(),
            placementId!!,
            object : NativeAdListener {
                override fun onAdLoaded(ad: NativeAd) {
                    nativeAd = ad
                    enableShowAd()
                    enableDestroyAd()
                    logger.print("Loaded, ad: ${nativeAd!!.title()}")
                }

                override fun onAdFailedToLoad(error: AdError) {
                    logger.print("Failed, error: ${error.message}")
                }

                override fun onAdImpression() {
                    logger.print("Impression, ad: ${nativeAd!!.title()}")
                }

                override fun onAdClicked() {
                    logger.print("Clicked, ad: ${nativeAd!!.title()}")
                }
            }
        )
    }

    override fun showAd() {
        nativeAd?.let {
            if (it.isAdInvalidated()) {
                logger.print("Ad is invalidated.")
                destroyAd()
                return
            }
            it.setAdChoicePosition(NativeAd.AdChoicePosition.TOP_RIGHT)
            NativeAdLayoutBinding.inflate(LayoutInflater.from(requireContext())).apply {
                nativeAdTitle.text = it.title()
                nativeAdBody.text = it.description()
                it.starRating()?.let { rating ->
                    starRating.text = "Rating: $rating"
                    starRating.visibility = View.VISIBLE
                } ?: run {
                    starRating.visibility = View.GONE
                }
                nativeAdCallToAction.text = it.callToAction()
                nativeAdMedia.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

                val nativeAdRootView: FrameLayout = getRoot()
                adContainer().addView(nativeAdRootView)
                val interactionViews = NativeAd.InteractionViews.Builder(nativeAdMedia)
                    .setTitleView(nativeAdTitle)
                    .setBodyView(nativeAdBody)
                    .setCallToActionView(nativeAdCallToAction)
                    .setIconView(nativeAdIcon)
                    .build()
                it.registerInteractionViews(nativeAdRootView, interactionViews)
            }
            disableShowAd()
            enableDestroyAd()
        }
    }

    override fun destroyAd() {
        nativeAd?.destroy()
        nativeAd = null
        adContainer().removeAllViews()
        logger.print("Destroyed...")
        disableShowAd()
        disableDestroyAd()
    }
}
