package com.opera.ads.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.opera.ads.AdFormat;
import com.opera.ads.demo.databinding.FragmentBaseBinding;
import com.opera.ads.demo.util.Constant;
import com.opera.ads.demo.util.LogView;

public class BaseFragment extends Fragment {
    private FragmentBaseBinding mBinding;
    @Nullable
    protected String mPlacementId;
    @Nullable
    protected LogView mLogView;
    @Nullable
    protected ViewGroup mAdContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentBaseBinding.inflate(inflater, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) mPlacementId = arguments.getString("placementId");
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogView = new LogView();
        mLogView.setUp(mBinding.log, mBinding.logScroller);
        mAdContainer = mBinding.adContainer;
        mBinding.adFormat.setText(getAdFormatStringId());
        mBinding.placementId.setText(mPlacementId);
        mBinding.loadAd.setOnClickListener(v -> loadAd());
        mBinding.showAd.setOnClickListener(v -> showAd());
        mBinding.destroyAd.setOnClickListener(v -> destroyAd());
        disableShowAd();
        disableDestroyAd();
        if (hasVideo()) {
            mBinding.isVideo.setVisibility(View.VISIBLE);
            mBinding.isVideo.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> {
                        mPlacementId = Constant.getPlacementId(getAdFormat(), isChecked);
                        mBinding.placementId.setText(mPlacementId);
                    });
        }
    }

    protected boolean hasVideo() {
        return false;
    }

    @NonNull
    protected AdFormat getAdFormat() {
        return AdFormat.BANNER;
    }

    protected int getAdFormatStringId() {
        return 0;
    }

    protected void loadAd() {

    }

    protected void showAd() {

    }

    protected void destroyAd() {

    }

    protected void enableShowAd() {
        mBinding.showAd.setEnabled(true);
    }

    protected void disableShowAd() {
        mBinding.showAd.setEnabled(false);
    }

    protected void enableDestroyAd() {
        mBinding.destroyAd.setEnabled(true);
    }

    protected void disableDestroyAd() {
        mBinding.destroyAd.setEnabled(false);
    }
}
