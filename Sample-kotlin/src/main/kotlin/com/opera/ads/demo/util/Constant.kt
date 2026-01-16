package com.opera.ads.demo.util

import com.opera.ads.AdFormat
import com.opera.ads.OperaAds

object Constant {
    private val useTestServer = OperaAds.usingTestServer()

    val APPLICATION_ID: String = if (useTestServer) {
        "pub13124398458816/ep13374306271488/app13336434553408"
    }
    else {
        "pub13423013211200/ep13423013211584/app13423536670400"
    }

    private val NATIVE_PLACEMENT_ID = if (useTestServer) "s13336452960512" else "s13429368154496"
    private val BANNER_PLACEMENT_ID = if (useTestServer) "s13336445508160" else "s13423621779136"
    private val BANNER_VIDEO_PLACEMENT_ID = if (useTestServer) "s13391091037312" else "s13429297184768"
    private val INTERSTITIAL_PLACEMENT_ID = if (useTestServer) "s13391104307072" else "s13423624619200"
    private val INTERSTITIAL_VIDEO_PLACEMENT_ID = if (useTestServer) "s13391097365952" else "s13424442482432"
    private val REWARDED_PLACEMENT_ID = if (useTestServer) "s13584962043136" else "s13938889680960"
    private val REWARDED_INTERSTITIAL_PLACEMENT_ID = if (useTestServer) "s14353628765376" else "s14352672069184"
    private val APP_OPEN_PLACEMENT_ID = if (useTestServer) "s14364818715776" else "s14352673721856"

    fun AdFormat.samplePlacementId(forceVideo: Boolean = false): String = when (this) {
        AdFormat.NATIVE -> NATIVE_PLACEMENT_ID
        AdFormat.BANNER -> if (forceVideo) BANNER_VIDEO_PLACEMENT_ID else BANNER_PLACEMENT_ID
        AdFormat.INTERSTITIAL -> if (forceVideo) INTERSTITIAL_VIDEO_PLACEMENT_ID else INTERSTITIAL_PLACEMENT_ID
        AdFormat.REWARDED -> REWARDED_PLACEMENT_ID
        AdFormat.REWARDED_INTERSTITIAL -> REWARDED_INTERSTITIAL_PLACEMENT_ID
        AdFormat.APP_OPEN -> APP_OPEN_PLACEMENT_ID
    }
}
