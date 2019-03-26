package cn.nlifew.juzimi.ui.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.juzimi.ui.share.ShareActivity;
import cn.nlifew.support.ToastUtils;

public class SentenceWidget extends AppWidgetProvider {
    private static final String TAG = "SentenceWidget";
    public static final String ACTION_WIDGET_CLICK =
            "cn.nlifew.juzimi.ui.widget.SentenceWidget.ACTION_WIDGET_CLICK";
    public static final String ACTION_WIDGET_TIMER =
            "cn.nlifew.juzimi.ui.widget.SentenceWidget.ACTION_WIDGET_TIMER";
    public static final String ACTION_WIDGET_UPDATE =
            "cn.nlifew.juzimi.ui.widget.SentenceWidget.ACTION_WIDGET_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled: start");
        loop(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled: start");
        cancel(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: action: " + intent.getAction());
        // 派发事件
        String action = intent.getAction();
        if (ACTION_WIDGET_UPDATE.equals(action)) {
            updateWidget(context);
        } else if (ACTION_WIDGET_TIMER.equals(action)) {
            updateWidget(context);
            loop(context);
        } else if (ACTION_WIDGET_CLICK.equals(action)) {
            if (SentenceHelper.isDoubleClick()) {
                dispatchDoubleClickAction(context);
            } else {
                dispatchClickAction(context);
            }
        }
    }

    private void dispatchClickAction(Context context) {
        String action = Settings.getInstance(context).getWidgetClick();
        Log.d(TAG, "dispatchClickAction: action from settings: " + action);
        switch (action) {
            case Settings.VALUE_WIDGET_CLICK_NULL:
                break;
            case Settings.VALUE_WIDGET_CLICK_UPDATE:
                updateWidget(context);
                break;
        }
    }

    private void dispatchDoubleClickAction(Context context) {
        String action = Settings.getInstance(context).getWidgetDouble();
        Log.d(TAG, "dispatchDoubleClickAction: action from settings: " + action);
        switch (action) {
            case Settings.VALUE_WIDGET_DOUBLE_NULL:
                break;
            case Settings.VALUE_WIDGET_DOUBLE_COPY:
                copyToClipboard(context, SentenceHelper.current());
                break;
            case Settings.VALUE_WIDGET_DOUBLE_SHARE:
                startShareActivity(context, SentenceHelper.current());
                break;
            case Settings.VALUE_WIDGET_DOUBLE_UPDATE:
                updateWidget(context);
                break;
        }
    }

    private void loop(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            Log.w(TAG, "loop: null AlarmManager");
            ToastUtils.with(context).show("无法访问 AlarmManager");
            return;
        }
        int loopTimeMs = Settings.getInstance(context)
                .getWidgetTime() * 60 * 1000;
        am.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + loopTimeMs,
                SentenceHelper.newTimerIntent(context));
    }

    private void cancel(Context context) {
        AlarmManager am = (AlarmManager) context.
                getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(SentenceHelper.newTimerIntent(context));
        }
    }

    private void updateWidget(Context context) {
        Log.d(TAG, "updateWidget: start");
        Sentence sentence = SentenceHelper.next(context);
        Log.d(TAG, "updateWidget: sentence: " + sentence);
        if (sentence == null) {
            return;
        }
        Settings settings = Settings.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_sentence);
        views.setOnClickPendingIntent(R.id.widget_sentence,
                SentenceHelper.newClickIntent(context));

        views.setTextViewTextSize(R.id.widget_sentence_content,
                TypedValue.COMPLEX_UNIT_DIP,
                settings.getWidgetSize());
        views.setTextColor(R.id.widget_sentence_content,
                0xff000000|settings.getWidgetColor());
        views.setTextViewText(R.id.widget_sentence_content, sentence.content);


        views.setTextViewTextSize(R.id.widget_sentence_from,
                TypedValue.COMPLEX_UNIT_DIP,
                settings.getWidgetSize());
        views.setTextColor(R.id.widget_sentence_from,
                0xff000000|settings.getWidgetColor());
        views.setTextViewText(R.id.widget_sentence_from, sentence.getFrom());

        SentenceHelper.update(context, views);
        Log.d(TAG, String.format("updateWidget: color:%#x, size:%d",
                0xff000000|settings.getWidgetColor(),
                settings.getWidgetSize()));
    }

    private void copyToClipboard(Context context, Sentence sentence) {
        if (sentence == null) {
            return;
        }
        ClipboardManager cm = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm == null) {
            Log.w(TAG, "copyToClipboard: ClipboardManager is null");
            ToastUtils.with(context).show("无法访问剪贴板服务");
            return;
        }
        String app = Settings.getInstance(context).getAppName();
        cm.setPrimaryClip(ClipData.newPlainText(app,
                sentence.content + "\n" + sentence.getFrom()));
        ToastUtils.with(context).show("已复制到剪贴板");
    }

    private void startShareActivity(Context context, Sentence sentence) {
        if (sentence == null) {
            return;
        }
        ShareActivity.start(context, sentence);
    }
}
