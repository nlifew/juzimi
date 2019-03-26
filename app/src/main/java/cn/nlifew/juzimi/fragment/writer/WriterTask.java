package cn.nlifew.juzimi.fragment.writer;

import android.util.Log;

import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.BaseWriterTask;
import cn.nlifew.support.ToastUtils;

public class WriterTask extends BaseWriterTask {
    private static final String TAG = "WriterTask";

    WriterTask(int id, String url) {
        super(url);
        mRequest.id(id);
    }

    @Override
    public void onUIThread(Object target) {
        BaseWriterFragment fragment = (BaseWriterFragment)
                target;
        if (mRequest.id() == BaseLoadMoreFragment.ID_REFRESH) {
            fragment.update(mWriters);
        } else {
            fragment.addAll(mWriters);
        }
        fragment.setNextUrl(mNextUrl);
        if (mErrInfo != null) {
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
        Log.d(TAG, "onUIThread: mNextUrl:" + mNextUrl);
        Log.d(TAG, "onUIThread: " + mWriters.size());
    }
}
