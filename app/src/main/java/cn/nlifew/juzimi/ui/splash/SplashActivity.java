package cn.nlifew.juzimi.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.main.MainActivity;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.TextUtils;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;

public class SplashActivity extends BaseActivity
    implements Runnable {
    private static final String TAG = "SplashActivity";

    private Handler mHandler;
    private TextView mTextView;
    private int mLeftTimeMs = 1500;
    private StringBuilder mBuilder = new StringBuilder("这是一个启动页\n(～￣▽￣)～\n ");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextView = new TextView(this);
        addTextView(mTextView);

        mHandler = new Handler();
        mHandler.post(this);
    }

    private void addTextView(TextView textView) {
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.color.colorPrimary);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(textView, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "run: " + mLeftTimeMs);
        mBuilder.deleteCharAt(mBuilder.length() - 1);
        mBuilder.append(mLeftTimeMs / 1000 + 1);
        mTextView.setText(mBuilder);
        switch (mLeftTimeMs) {
            case 500:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case 1000:
                initCookie();
                mLeftTimeMs -= 500;
                mHandler.postDelayed(this, 500);
                break;
            case 1500:
                initNightMode();
                mLeftTimeMs -= 500;
                mHandler.postDelayed(this, 500);
                break;
        }
    }

    private void initNightMode() {
        Settings settings = Settings.getInstance(this);
        boolean auto = settings.isNightAuto();
        if (auto) {
            Calendar calendar = Calendar.getInstance();
            float now = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 100.0f;
            float on = settings.getNightAutoOn();
            float off = settings.getNightAutoOff();
            AppCompatDelegate.setDefaultNightMode(
                    now >= on || now <= off ?
                    MODE_NIGHT_YES : MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    settings.isNightMode() ?
                    MODE_NIGHT_YES : MODE_NIGHT_NO);
        }
    }

    private void initCookie() {
        User user = Settings.getInstance(this).getUser();
        if (user == null) return;

        StringBuilder builder = new StringBuilder(user.cookie);
        int time = (int) (System.currentTimeMillis() / 1000);
        TextUtils.replaceLastAfter(builder,
                "Hm_lpvt_0684e5255bde597704c827d5819167ba=",
                10, String.valueOf(time));
        user.setCookie(builder.toString());
    }
}
