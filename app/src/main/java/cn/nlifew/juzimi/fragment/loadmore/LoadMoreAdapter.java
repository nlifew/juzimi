package cn.nlifew.juzimi.fragment.loadmore;


import android.content.Context;

import java.util.List;

import cn.nlifew.support.adapter.BaseLoadMoreAdapter;

public abstract class LoadMoreAdapter<T> extends BaseLoadMoreAdapter<T> {

    protected BaseLoadMoreFragment<T> mFragment;

    public Context getContext() {
        return mFragment.getContext();
    }

    public LoadMoreAdapter(BaseLoadMoreFragment<T> fragment, List<T> list) {
        super(list);
        mFragment = fragment;
    }

    @Override
    public void setLoading(boolean loading) {
        super.setLoading(loading);
        mFragment.getSwipeLayout().setRefreshing(loading);
    }
}
