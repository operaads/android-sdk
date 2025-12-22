package com.opera.ads.demo.util;

import android.content.Context;

public class Dips {

    public static float dipsToFloatPixels(final float dips, final Context context) {
        return dips * getDensity(context);
    }

    public static int dipsToIntPixels(final float dips, final Context context) {
        return (int) (dipsToFloatPixels(dips, context) + 0.5f);
    }

    private static float getDensity(final Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
