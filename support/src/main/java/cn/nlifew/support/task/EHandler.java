package cn.nlifew.support.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import cn.nlifew.support.task.ESyncTaskFactory.ESyncInterface;

import java.util.LinkedList;

public class EHandler extends Handler {
    private static final String TAG = "EHandler";

    private volatile Object mTarget;
    private LinkedList<ESyncInterface> mTaskList;

    public void onResume(Object t) {
        if (mTaskList != null) {
            while (mTaskList.size() != 0) {
                Message msg = Message.obtain();
                msg.obj = mTaskList.removeFirst();
                sendMessage(msg);
            }
        }
        mTarget = t;
    }

    public void onPause() {
        mTarget = null;
    }

    public void onDestroy() {
        mTarget = null;
        removeCallbacksAndMessages(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        ESyncInterface task = (ESyncInterface) msg.obj;

        if (mTarget == null) {
            // 把所有任务放到队列里等待执行
            Log.d(TAG, "handleMessage: append task to waiting list");
            if (mTaskList == null) {
                mTaskList = new LinkedList<>();
            }
            mTaskList.addLast(task);
        } else {
            Log.d(TAG, "handleMessage: handle task directly");
            task.onUIThread(mTarget);
        }
    }
}
