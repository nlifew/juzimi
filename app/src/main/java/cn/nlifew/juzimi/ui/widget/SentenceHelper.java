package cn.nlifew.juzimi.ui.widget;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.task.ESyncTaskFactory;

public class SentenceHelper {
    private static final String TAG = "SentenceHelper";

    private SentenceHelper() {}

    private static long mLastClickTime;
    static boolean isDoubleClick() {
        long now = System.currentTimeMillis();
        long last = mLastClickTime;
        Log.d(TAG, "isDoubleClick: last: " + mLastClickTime +
                    " now: " + now +
                    " delayMs: " + (now - last));

        mLastClickTime = now;
        return now - last < 200;
    }

    static Sentence current() {
        return mSentences.size() != 0 ? mSentences.get(0) : null;
    }

    private static ESyncTaskFactory mFactory;
    private static LinkedList<Sentence> mSentences = new LinkedList<>();
    static Sentence next(Context context) {
        if (mSentences.size() > 1) {
            mSentences.removeFirst();
            return mSentences.removeFirst();
        }
        if (mFactory == null) {
            mFactory = ESyncTaskFactory.with(context.getApplicationContext());
        }
        String url = nextUrl(context);
        if (url != null) {
            mFactory.execute(new SentenceTask(url));
        }
        return null;
    }

    static void cache(Context context, List<Sentence> sentences, String nextUrl) {
        mSentences.addAll(sentences);
        mUrls.addLast(nextUrl);
        if (mSentences.size() != 0) {
            Intent intent = new Intent(SentenceWidget.ACTION_WIDGET_UPDATE);
            intent.setClass(context, SentenceWidget.class);
            context.sendBroadcast(intent);
        }
    }

    static PendingIntent newClickIntent(Context context) {
        Intent intent = new Intent(SentenceWidget.ACTION_WIDGET_CLICK);
        intent.setClass(context, SentenceWidget.class);
        return PendingIntent.getBroadcast(context, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    static PendingIntent newTimerIntent(Context context) {
        Intent intent = new Intent(SentenceWidget.ACTION_WIDGET_TIMER);
        intent.setClass(context, SentenceWidget.class);
        return PendingIntent.getBroadcast(context, 2,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    static void update(Context context, RemoteViews views) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context, SentenceWidget.class);
        manager.updateAppWidget(name, views);
    }

    private static LinkedList<String> mUrls = new LinkedList<>();
    private static String nextUrl(Context context) {
        if (mUrls.size() != 0) {
            return mUrls.removeFirst();
        }
        Settings settings = Settings.getInstance(context);
        User user = settings.getUser();
        if (user != null && user.likeUrl != null) {
            mUrls.add(user.likeUrl);
        }
        mUrls.addAll(settings.getWidgetUrl());
        return mUrls.size() != 0 ? mUrls.removeFirst() : null;
    }
}
