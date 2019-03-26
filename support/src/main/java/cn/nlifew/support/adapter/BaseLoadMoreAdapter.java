package cn.nlifew.support.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nlifew on 2018/8/11.
 */

public abstract class BaseLoadMoreAdapter<T> extends BaseAdapter<T> {
    private boolean loading = false;
    private OnLoadMoreListener listener;

    public interface OnLoadMoreListener {
        void onLoadMore(BaseLoadMoreAdapter adapter);
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        private int findMax(int[] ints) {
            int max = ints[0];
            for (int i = 1; i < ints.length; i++) {
                if (max < ints[i]) max = ints[i];
            }
            return max;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) return;

            int lastVisibleItemPosition = 0;
            int totalItemCount = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager
                        = (StaggeredGridLayoutManager) layoutManager;
                int[] lastPositions = new int[manager.getSpanCount()];
                manager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                totalItemCount = manager.getItemCount();
            } else if (layoutManager instanceof LinearLayoutManager ) {
                LinearLayoutManager manager
                        = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                totalItemCount = manager.getItemCount();
            }
            if (! loading && listener != null
                    && lastVisibleItemPosition == totalItemCount - 1) {
                //此时是刷新状态
                setLoading(true);
                listener.onLoadMore(BaseLoadMoreAdapter.this);
            }
        }
    }

    public BaseLoadMoreAdapter(List<T> list) {
        super(list);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new ScrollListener());
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }
}
