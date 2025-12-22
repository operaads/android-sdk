package com.opera.ads.demo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opera.ads.AdError;
import com.opera.ads.AdFormat;
import com.opera.ads.demo.R;
import com.opera.ads.demo.databinding.NativeAdLayoutBinding;
import com.opera.ads.demo.util.Constant;
import com.opera.ads.nativead.NativeAd;
import com.opera.ads.nativead.NativeAdListener;
import com.opera.ads.nativead.NativeAdLoader;

public class NativeFragment extends BaseFragment {
    @Nullable
    private NativeAd mNativeAd;

    @NonNull
    @Override
    protected AdFormat getAdFormat() {
        return AdFormat.NATIVE;
    }

    @Override
    protected int getAdFormatStringId() {
        return R.string.native_ad_format;
    }

    @Override
    protected void loadAd() {
        if (mLogView != null) mLogView.print("Loading...");
        String placementId = mPlacementId == null ? Constant.getPlacementId(AdFormat.NATIVE, false) : mPlacementId;
        NativeAdLoader.loadAd(requireContext(), placementId, new NativeAdListener()  {
            @Override
            public void onAdLoaded(@NonNull NativeAd nativeAd) {
                mNativeAd = nativeAd;
                enableShowAd();
                enableDestroyAd();
                if (mLogView != null) {
                    mLogView.print("Loaded, ad: " + mNativeAd.title());
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull AdError error) {
                if (mLogView != null) {
                    mLogView.print("Failed, error: " + error.getMessage());
                }
            }

            @Override
            public void onAdImpression() {
                if (mLogView != null) {
                    mLogView.print("Impression, ad: " + mNativeAd.title());
                }
            }

            @Override
            public void onAdClicked() {
                if (mLogView != null) {
                    mLogView.print("Clicked, ad: " + mNativeAd.title());
                }
            }
        });
    }

    @Override
    protected void showAd() {
        if (mNativeAd != null && mAdContainer != null) {
            if (mNativeAd.isAdInvalidated()) {
                mLogView.print("Ad is invalidated.");
                destroyAd();
                return;
            }
            mNativeAd.setAdChoicePosition(NativeAd.AdChoicePosition.TOP_RIGHT);
            NativeAdLayoutBinding itemBinding = NativeAdLayoutBinding.inflate(LayoutInflater.from(requireContext()));
            itemBinding.nativeAdTitle.setText(mNativeAd.title());
            itemBinding.nativeAdBody.setText(mNativeAd.description());
            final Double starRating = mNativeAd.starRating();
            itemBinding.starRating.setText(starRating != null ? "Rating: " + starRating : "");
            itemBinding.starRating.setVisibility(starRating != null ? View.VISIBLE : View.GONE);
            itemBinding.nativeAdCallToAction.setText(mNativeAd.callToAction());
            itemBinding.nativeAdMedia.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            NativeAd.InteractionViews interactionViews = new NativeAd.InteractionViews.Builder(
                    itemBinding.nativeAdMedia
            ).setTitleView(itemBinding.nativeAdTitle)
                    .setBodyView(itemBinding.nativeAdBody)
                    .setCallToActionView(itemBinding.nativeAdCallToAction)
                    .setIconView(itemBinding.nativeAdIcon)
                    .build();

            FrameLayout nativeAdRootView = itemBinding.getRoot();
            mNativeAd.registerInteractionViews(nativeAdRootView, interactionViews);
            if (mAdContainer != null) mAdContainer.addView(nativeAdRootView);
            disableShowAd();
            enableDestroyAd();
        }
    }

    @Override
    protected void destroyAd() {
        if (mNativeAd != null) {
            mNativeAd.destroy();
            if (mAdContainer != null) mAdContainer.removeAllViews();
            if (mLogView != null) mLogView.print("Destroyed...");
            mNativeAd = null;
            disableShowAd();
            disableDestroyAd();
        }
    }
}
