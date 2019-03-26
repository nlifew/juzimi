package cn.nlifew.juzimi.ui.login;

import android.util.Log;

import cn.nlifew.juzimi.network.BaseUserTask;
import cn.nlifew.support.ToastUtils;

public class LoginTask extends BaseUserTask {
    private static final String TAG = "LoginTask";

    LoginTask(String url, String cookie) {
        super(url, cookie);
    }

    @Override
    public void onUIThread(Object target) {
        LoginActivity activity = (LoginActivity) target;
        activity.dismissProgress();

        if (mErrInfo != null) {
            activity.setResult(LoginActivity.RESULT_CANCELED);
            ToastUtils.with(activity).show(mErrInfo);
        } else {
            activity.setResult(LoginActivity.RESULT_OK);
            ToastUtils.with(activity).show(mUser.name);
            Log.d(TAG, "onUIThread: " + mUser);
        }
        activity.finish();
    }
}
