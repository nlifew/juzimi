package cn.nlifew.juzimi.fragment.loadmore;

import android.util.Log;

import java.util.List;

import cn.nlifew.juzimi.fragment.content.BaseContentFragment;
import cn.nlifew.juzimi.network.NetworkTask;
import cn.nlifew.support.TextUtils;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.adapter.BaseAdapter;
import cn.nlifew.support.adapter.BaseLoadMoreAdapter;
import cn.nlifew.support.task.ESyncTaskFactory;

public abstract class BaseLoadMoreFragment<T> extends BaseContentFragment<T>
    implements BaseLoadMoreAdapter.OnLoadMoreListener {
    private static final String TAG = "BaseLoadMoreFragment";

    protected String mNextUrl;
    protected BaseLoadMoreAdapter<T> mAdapter;
    protected ESyncTaskFactory mFactory;

    public static final int ID_REFRESH = 1;
    public static final int ID_LOADMORE = 2;

    public abstract String getRefreshUrl();

    public abstract NetworkTask newNetworkTask(int id, String url);

    @Override
    protected void onBindAdapter(BaseAdapter<T> adapter) {
        super.onBindAdapter(adapter);
        mFactory = ESyncTaskFactory.with(this);
        mAdapter = (BaseLoadMoreAdapter<T>) adapter;
        mAdapter.setOnLoadMoreListener(this);
    }

    @Override
    public void onRefresh() {
        String url = getRefreshUrl();
        Log.d(TAG, "onRefresh: start " + url);
        if (url == null) {
            ToastUtils.with(getContext()).show("没有数据");
            mAdapter.setLoading(false);
        } else {
            NetworkTask task = newNetworkTask(ID_REFRESH, url);
            mFactory.execute(task);
        }
        Log.d(TAG, "onRefresh: end " + url);
    }

    @Override
    public void onLoadMore(BaseLoadMoreAdapter adapter) {
        Log.d(TAG, "onLoadMore: start " + mNextUrl);
        if (mNextUrl == null) {
            ToastUtils.with(getContext()).show("没有更多数据");
            mAdapter.setLoading(false);
        } else {
            NetworkTask task = newNetworkTask(ID_LOADMORE, mNextUrl);
            mFactory.execute(task);
        }
    }

    public void update(List<T> list) {
        mAdapter.update(list);
        mAdapter.setLoading(false);
    }

    public void addAll(List<T> list) {
        mAdapter.addAll(list);
        mAdapter.setLoading(false);
        ToastUtils.with(getContext()).show("新加载" + list.size() + "条数据");
    }

    public ESyncTaskFactory getESyncFactory() {
        return mFactory;
    }

    public void setNextUrl(String url) {
        mNextUrl = url;
    }
}
