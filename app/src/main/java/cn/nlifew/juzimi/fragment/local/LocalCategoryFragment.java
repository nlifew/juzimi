package cn.nlifew.juzimi.fragment.local;

import android.util.Log;

import java.util.List;

import cn.nlifew.juzimi.bean.Category;
import cn.nlifew.juzimi.fragment.content.BaseContentFragment;
import cn.nlifew.support.adapter.BaseAdapter;
import cn.nlifew.support.task.ESyncTaskFactory;

public abstract class LocalCategoryFragment<T extends Category>
        extends BaseContentFragment<T> {
    private static final String TAG = "LocalCategoryFragment";

    private LocalCategoryAdapter<T> mAdapter;

    protected abstract LocalCategoryTask<T> newESyncTask();

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: start");
        List<T> list = mAdapter.getList();
        if (list.size() == 0) {
            Log.d(TAG, "onRefresh: not found Writers in database, unzip assert ...");
            ESyncTaskFactory.with(this).execute(newESyncTask());
        } else {
            Log.d(TAG, "onRefresh: nothing to do");
            mSwipeLayout.setRefreshing(false);
        }
        Log.d(TAG, "onRefresh: end");
    }

    @Override
    protected void onBindAdapter(BaseAdapter<T> adapter) {
        super.onBindAdapter(adapter);
        mAdapter = (LocalCategoryAdapter<T>) adapter;
    }

    public void update(List<T> list) {
        mAdapter.update(list);
        mSwipeLayout.setRefreshing(false);
    }
}
