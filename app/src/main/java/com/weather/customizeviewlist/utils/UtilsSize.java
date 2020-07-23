package com.weather.customizeviewlist.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class UtilsSize {
    public static int dpToPx(Context context, float fDp) {
        if (null == context) {
            return 0;
        }

        return Math.round(fDp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(Context context, float fPx) {
        if (null == context) {
            return 0;
        }

        return Math.round(fPx / context.getResources().getDisplayMetrics().density);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
