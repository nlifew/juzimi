package cn.nlifew.juzimi.ui.update;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.network.NetRequest;
import cn.nlifew.support.network.NetRequestImpl;
import cn.nlifew.support.task.ESyncTaskFactory;

public class CheckUpdateTask implements ESyncTaskFactory.ESyncInterface {
    private static final String TAG = "CheckUpdateTask";

    private String mErrInfo;
    private UpdateInfo mUpdateInfo;

    public CheckUpdateTask(Context context) {
        ToastUtils.with(context).show("正在检查更新 ...", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onIOThread() {
        try {
            mUpdateInfo = UpdateInfo.fromJSON(obtainUpdateJSON());
            /*
            mUpdateInfo = new UpdateInfo();
            mUpdateInfo.link = "https://github.com/nlifew/juzimi/release/xxx.apk";
            mUpdateInfo.force = false;
            mUpdateInfo.version = 5;
            mUpdateInfo.changelog = "changelog";
            Thread.sleep(5000);
            */
            Log.d(TAG, "onIOThread: " + mUpdateInfo);
        } catch (Exception exp) {
            mErrInfo = exp.toString();
            Log.e(TAG, "onIOThread: ", exp);
        }
        return true;
    }

    private String obtainUpdateJSON() throws IOException {
        NetRequest request = new NetRequestImpl();
        request.url("https://raw.githubusercontent.com/nlifew/juzimi/master/update.json");
        try {
            return request.get().string();
        } catch (IOException exp) {
            throw exp;
        } finally {
            request.close();
        }
    }

    @Override
    public void onUIThread(Object target) {
        Context context = (Context) target;
        if (mErrInfo != null) {
            ToastUtils.with(context).show(mErrInfo);
            return;
        }
        int now = Settings.getInstance(context).getVersionCode();
        if (mUpdateInfo.version > now) {
            UpdateResultActivity.start(context, mUpdateInfo);
        } else {
            ToastUtils.with(context).show("未发现新版本");
        }
    }
}
