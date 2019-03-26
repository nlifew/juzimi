package cn.nlifew.juzimi.ui.update;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.ToastUtils;

public class UpdateResultActivity extends BaseActivity
    implements DialogInterface.OnClickListener {
    private static final String TAG = "UpdateResultActivity";
    private static final String EXTRA_UPDATE_INFO = "EXTRA_UPDATE_INFO";

    public static void start(Context context, UpdateInfo info) {
        Intent intent = new Intent(context, UpdateResultActivity.class);
        intent.putExtra(EXTRA_UPDATE_INFO, info);
        context.startActivity(intent);
    }

    private UpdateInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInfo = getIntent().getParcelableExtra(EXTRA_UPDATE_INFO);

        new AlertDialog.Builder(this)
                .setTitle("检测到新版本")
                .setMessage(mInfo.changelog)
                .setCancelable(! mInfo.force)
                .setNeutralButton("取消", this)
                .setPositiveButton("直接下载", this)
                .setNegativeButton("应用市场下载", this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                DownloadManager manager = (DownloadManager)
                        getSystemService(DOWNLOAD_SERVICE);
                if (manager == null) {
                    ToastUtils.with(this).show("无法访问下载服务");
                } else {
                    manager.enqueue(new DownloadManager.Request(
                            Uri.parse(mInfo.link)));
                    finish();
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (Exception exp) {
                    ToastUtils.with(this).show(exp.toString());
                }
                finish();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                finish();
                break;
        }
    }
}
