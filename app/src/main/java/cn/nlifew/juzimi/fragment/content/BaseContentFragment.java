package cn.nlifew.juzimi.fragment.content;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.support.adapter.BaseAdapter;
import cn.nlifew.support.fragment.LazyLoadFragment;

public abstract class BaseContentFragment<T> extends LazyLoadFragment
    implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BaseContentFragment";

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeLayout;

    public abstract BaseAdapter<T> newRecyclerAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content,
                        container, false);
    }

    public LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Override
    protected void onLazyLoad() {
        Log.d(TAG, "onLazyLoad: start");
        onBindView(getView());
        onBindAdapter(newRecyclerAdapter());
        Log.d(TAG, "onLazyLoad: end");
        mSwipeLayout.setRefreshing(true);
        onRefresh();
    }

    protected void onBindView(View v) {
        mRecyclerView = v.findViewById(R.id.fragment_content_recycler);
        mSwipeLayout = v.findViewById(R.id.fragment_content_swipe);
    }

    protected void onBindAdapter(BaseAdapter<T> adapter) {
        mSwipeLayout.setColorSchemeColors(Color.BLUE, Color.GRAY, Color.BLACK);
        mSwipeLayout.setOnRefreshListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(getLayoutManager(getContext()));
    }

    public SwipeRefreshLayout getSwipeLayout() {
        return mSwipeLayout;
    }
}
