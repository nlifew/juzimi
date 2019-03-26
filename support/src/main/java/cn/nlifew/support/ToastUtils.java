package cn.nlifew.support;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtils {

    private static ToastUtils sInstance;
    public static ToastUtils with(Context context) {
        if (sInstance == null) {
            synchronized (ToastUtils.class) {
                if (sInstance == null) sInstance = new ToastUtils(
                        context.getApplicationContext());
            }
        }
        return sInstance;
    }

    private Toast mToast;
    private ToastUtils(Context context) {
        mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
    }

    public void show(CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    public void show(CharSequence text, int time) {
        mToast.setText(text);
        mToast.setDuration(time);
        mToast.show();
    }
}
