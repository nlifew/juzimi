package cn.nlifew.juzimi.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import cn.nlifew.juzimi.ui.login.LoginActivity;
import cn.nlifew.juzimi.ui.main.MainActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.ToastUtils;

public class Utils {

    private Utils() {
        throw new RuntimeException("unnecessary to instance it.");
    }

    public static boolean showLoginDialog(final BaseActivity activity) {
        Settings settings = Settings.getInstance(activity);
        if (settings.getUser() != null) {
            return false;
        }

        DialogInterface.OnClickListener callback = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivityForResult(intent, MainActivity.CODE_LOGIN);
                }
            }
        };
        new AlertDialog.Builder(activity)
                .setTitle(settings.getAppName())
                .setMessage("您需要先登录")
                .setPositiveButton("登录", callback)
                .setNegativeButton("取消", callback)
                .show();
        return true;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String id, String name, int impartance) {
        NotificationChannel channel = new NotificationChannel(id, name, impartance);
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager manager = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager == null) {
            ToastUtils.with(context).show("无法访问剪贴板服务");
        } else {
            manager.setPrimaryClip(ClipData.newPlainText(
                    Settings.getInstance(context).getAppName(),
                    text
            ));
        }
    }

    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
