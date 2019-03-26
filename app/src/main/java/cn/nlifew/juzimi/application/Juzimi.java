package cn.nlifew.juzimi.application;


import android.util.DisplayMetrics;
import android.util.Log;

import org.litepal.LitePalApplication;

import cn.nlifew.juzimi.ui.settings.Settings;

public class Juzimi extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        sDensity = metrics.density;
        sScaledDensity = metrics.scaledDensity;

        Thread.setDefaultUncaughtExceptionHandler(new ThrowableHandler(this));
    }

    private static float sDensity;
    private static float sScaledDensity;

    public static int px2sp(float px) {
        return (int) (px / sScaledDensity + 0.5f);
    }

    public static int px2dp(float px) {
        return (int) (px / sDensity + 0.5f);
    }

    public static int dp2px(float dp) {
        return (int) (dp * sDensity + 0.5f);
    }
}
