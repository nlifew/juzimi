package cn.nlifew.support.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.task.ESyncTaskFactory.ESyncInterface;

public class SaveViewBitmapTask implements ESyncInterface {
    private static final String TAG = "SaveViewBitmapTask";

    private String mName;
    private String mErrInfo;
    private Context mContext;
    private WeakReference<Bitmap> mBitmap;

    public SaveViewBitmapTask(View view, String name) {
        mName = name;
        view.buildDrawingCache();
        mContext = view.getContext().getApplicationContext();
        mBitmap = new WeakReference<>(view.getDrawingCache());
        ToastUtils.with(view.getContext()).show("正在保存 ...");
    }

    @Override
    public boolean onIOThread() {
        File dir = new File(Environment.getExternalStorageDirectory(),
                "/Pictures/juzimi");
        if (! dir.exists() && ! dir.mkdirs()) {
            mErrInfo = "无法访问内置存储";
            return true;
        }
        File file = new File(dir, mName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.get().compress(Bitmap.CompressFormat.PNG,
                    100, fos);
            fos.flush();
            fos.close();

            MediaStore.Images.Media.insertImage(
                    mContext.getContentResolver(),
                    file.getAbsolutePath(),
                    mName,
                    "from cn.nlifew.support");
            mErrInfo = "图片保存到:" + file.getAbsolutePath();
        } catch (Exception exp) {
            mErrInfo = mName + "\n" + exp;
            Log.d(TAG, "onIOThread: " + mErrInfo);
        }
        return true;
    }

    @Override
    public void onUIThread(Object target) {
        Context context = (Context) target;
        ToastUtils.with(context).show(mErrInfo);
    }
}
