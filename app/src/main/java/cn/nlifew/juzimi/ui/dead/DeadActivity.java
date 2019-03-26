package cn.nlifew.juzimi.ui.dead;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cn.nlifew.juzimi.application.Juzimi;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.Utils;
import cn.nlifew.juzimi.ui.splash.SplashActivity;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.task.ESyncTaskFactory;

public class DeadActivity extends BaseActivity
    implements DialogInterface.OnClickListener {
    private static final String TAG = "DeadActivity";
    private static final String EXTRA_ERROR = "EXTRA_ERROR";

    public static void start(Context context, Throwable t) {
        StringBuilder builder = new StringBuilder(1024);
        builder.append(t.getLocalizedMessage()).append(": ")
                .append(t.getMessage()).append('\n');
        StackTraceElement[] elements = t.getStackTrace();
        for (StackTraceElement element : elements) {
            builder.append("\t@").append(element.getClassName())
                    .append(".").append(element.getMethodName())
                    .append("(").append(element.getFileName())
                    .append(":").append(element.getLineNumber())
                    .append(")\n");
        }

        Intent intent = new Intent(context, DeadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_ERROR, builder.toString());
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText(getIntent().getStringExtra(EXTRA_ERROR));
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        setContentView(tv);

        String msg = "哦上帝啊，老实说，我可不敢相信这是真的——\n" +
                "有一个调皮的萝卜头跑了出来，它让一个倒霉的线程挂掉了\n" +
                "但幸亏有你，我的老伙计。如果可以的话——我是说，你一定要这样做——" +
                "下面有个叫\"反馈\"的家伙，点击它，然后和我联系。\n";

        new AlertDialog.Builder(this)
                .setTitle("哦吼，完蛋")
                .setMessage(msg)
                .setPositiveButton("重新打开app", this)
                .setNegativeButton("反馈", this)
                .setNeutralButton("保存到本地", this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent = new Intent(this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Process.killProcess(Process.myPid());
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if (! Utils.joinQQGroup(this, "lV5AQeLulJ8Dwy9PhXp0CneIYfnRp7ig")) {
                    ToastUtils.with(this).show("您没有安装 QQ，群号将复制到剪贴板");
                    Utils.copyToClipboard(this, "548591863");
                }
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                String err = getIntent().getStringExtra(EXTRA_ERROR);
                File dir = Environment.getExternalStorageDirectory();
                File out = new File(dir, String.valueOf(System.currentTimeMillis()));
                ESyncTaskFactory.with(this).execute(new SaveLogTask(err, out));
                break;
        }
    }
}

class SaveLogTask implements ESyncTaskFactory.ESyncInterface {

    private String log;
    private File file;

    SaveLogTask(String err, File out) {
        this.log = err;
        this.file = out;
    }

    @Override
    public boolean onIOThread() {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(log);
            fw.flush();
            fw.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        return true;
    }

    @Override
    public void onUIThread(Object target) {
        Context context = (Context) target;
        ToastUtils.with(context).show("已保存到" + file.getAbsolutePath());
    }
}