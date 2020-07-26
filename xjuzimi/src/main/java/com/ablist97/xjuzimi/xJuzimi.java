package com.ablist97.xjuzimi;

import android.app.Application;

public class xJuzimi {
    private static final String TAG = "xJuzimi";

    public static Application sContext;

    public static void initialize(Application application) {
        sContext = application;
    }

}
