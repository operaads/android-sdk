package com.opera.ads.demo.util;

import com.opera.ads.AdFormat;
import com.opera.ads.OperaAds;

public final class Constant {
    private static final boolean useTestServer = OperaAds.usingTestServer();

    public static final String APPLICATION_ID = useTestServer
            ? "pub13124398458816/ep13374306271488/app13336434553408"
            : "pub13423013211200/ep13423013211584/app13423536670400";

    private static final String NATIVE_PLACEMENT_ID =
            useTestServer ? "s13336452960512" : "s13429368154496";
    private static final String BANNER_PLACEMENT_ID =
            useTestServer ? "s13336445508160" : "s13423621779136";
    private static final String BANNER_VIDEO_PLACEMENT_ID =
            useTestServer ? "s13391091037312" : "s13429297184768";
    private static final String INTERSTITIAL_PLACEMENT_ID =
            useTestServer ? "s13391104307072" : "s13423624619200";
    private static final String INTERSTITIAL_VIDEO_PLACEMENT_ID =
            useTestServer ? "s13391097365952" : "s13424442482432";
    private static final String REWARDED_PLACEMENT_ID =
            useTestServer ? "s13584962043136" : "s13938889680960";
    private static final String REWARDED_INTERSTITIAL_PLACEMENT_ID =
            useTestServer ? "s14353628765376" : "s14352672069184";
    private static final String APP_OPEN_PLACEMENT_ID =
            useTestServer ? "s14364818715776" : "s14352673721856";

    public static String getPlacementId(AdFormat adFormat, boolean forceVideo) {
        switch (adFormat) {
            case NATIVE:
                return NATIVE_PLACEMENT_ID;
            case BANNER:
                return forceVideo ? BANNER_VIDEO_PLACEMENT_ID : BANNER_PLACEMENT_ID;
            case INTERSTITIAL:
                return forceVideo ? INTERSTITIAL_VIDEO_PLACEMENT_ID : INTERSTITIAL_PLACEMENT_ID;
            case REWARDED:
                return REWARDED_PLACEMENT_ID;
            case REWARDED_INTERSTITIAL:
                return REWARDED_INTERSTITIAL_PLACEMENT_ID;
            case APP_OPEN:
                return APP_OPEN_PLACEMENT_ID;
        }
        throw new RuntimeException("Unknown ad format: " + adFormat);
    }
}
