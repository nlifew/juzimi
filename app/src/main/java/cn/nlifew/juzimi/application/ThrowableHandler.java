package cn.nlifew.juzimi.application;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import cn.nlifew.juzimi.ui.EmptyActivity;
import cn.nlifew.juzimi.ui.dead.DeadActivity;

public class ThrowableHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ThrowableHandler";

    private static Context mContext;

    ThrowableHandler(Context context) {
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG, "uncaughtException: " + t.getName(), e);
        DeadActivity.start(mContext, e);
    }
}
