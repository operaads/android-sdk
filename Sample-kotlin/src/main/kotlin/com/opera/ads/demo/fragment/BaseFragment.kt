package com.opera.ads.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.opera.ads.AdFormat
import com.opera.ads.OperaAds
import com.opera.ads.demo.R
import com.opera.ads.demo.databinding.FragmentBaseBinding
import com.opera.ads.demo.util.Constant.samplePlacementId
import com.opera.ads.demo.util.LogView

abstract class BaseFragment(
    val format: AdFormat,
    val canForceVideoAsset: Boolean,
    val supportsMutedControl: Boolean,
    @StringRes val adFormatStringId: Int,
) : Fragment() {
    private lateinit var binding: FragmentBaseBinding
    protected lateinit var logger: LogView
    protected var placementId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
        val arguments = getArguments()
        if (arguments != null) placementId = arguments.getString("placementId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            logger = LogView(log, logScroller)
            adFormat.setText(adFormatStringId)
            placementId.text = this@BaseFragment.placementId
            loadAd.setOnClickListener { loadAd() }
            showAd.setOnClickListener { showAd() }
            destroyAd.setOnClickListener { destroyAd() }
            disableShowAd()
            disableDestroyAd()

            if (canForceVideoAsset) {
                isVideo.visibility = View.VISIBLE
                isVideo.setOnCheckedChangeListener { _, forceVideo ->
                    format.samplePlacementId(forceVideo).let {
                        this@BaseFragment.placementId = it
                        placementId.text = it
                    }
                }
            }

            if (supportsMutedControl) {
                isMutedRadioLayout.visibility = View.VISIBLE
                // Initialize UI based on global muted state.
                initializeMutedUI()
                // Set up listener for user interactions
                isMutedRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                    updateMutedState(checkedId)
                }
            }
        }
    }

    protected fun adContainer(): ViewGroup = binding.adContainer

    private fun initializeMutedUI() {
        // Set radio button selection based on global state.
        val checkedId = when (sGlobalMutedState) {
            null -> R.id.is_muted_radio_default
            true -> R.id.is_muted_radio_yes
            else -> R.id.is_muted_radio_no
        }
        binding.isMutedRadioGroup.check(checkedId)

        // Sync state to SDK.
        OperaAds.setMuted(sGlobalMutedState)
    }

    private fun updateMutedState(checkedId: Int) {
        var isMuted: Boolean? = null
        if (checkedId == R.id.is_muted_radio_yes) {
            isMuted = true
        } else if (checkedId == R.id.is_muted_radio_no) {
            isMuted = false
        }
        // Update global state.
        sGlobalMutedState = when (checkedId) {
            R.id.is_muted_radio_yes -> true
            R.id.is_muted_radio_no -> false
            else -> null
        }
        // Apply to SDK.
        OperaAds.setMuted(isMuted)
    }

    abstract fun loadAd()

    abstract fun showAd()

    abstract fun destroyAd()

    protected fun enableShowAd() {
        binding.showAd.setEnabled(true)
    }

    protected fun disableShowAd() {
        binding.showAd.setEnabled(false)
    }

    protected fun enableDestroyAd() {
        binding.destroyAd.setEnabled(true)
    }

    protected fun disableDestroyAd() {
        binding.destroyAd.setEnabled(false)
    }

    companion object {
        // Global muted state shared for all fragments.
        private var sGlobalMutedState: Boolean? = null
    }
}
