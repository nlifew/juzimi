package cn.nlifew.support.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Nlifew on 2018/8/11.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BaseAdapter";

    private static final int TYPE_HEAD_VIEW = 1;
    private static final int TYPE_ITEM_VIEW = 2;
    private static final int TYPE_TAIL_VIEW = 4;

    private List<T> mList;

    public View getHeadView(ViewGroup parent) { return null; }
    public View getTailView(ViewGroup parent) { return null; }
    public void handleHeadView(BaseViewHolder holder) {}
    public void handleTailView(BaseViewHolder holder) {}
    public abstract View getItemView(ViewGroup parent);
    public abstract void handleItemView(BaseViewHolder holder, T t);

    public BaseAdapter(List<T> list) {
        mList = list;
    }

    public void addAll(List<T> list) {
        int size = mList.size();
        mList.addAll(list);
        notifyItemRangeChanged(size, list.size());
    }

    public void update(List<T> list) {
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public boolean isHeadViewEnabled() {
        return false;
    }

    public boolean isTailViewEnabled() {
        return false;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 底部布局和顶部布局之间，优先后者.
        return position == getItemCount() - 1 && isTailViewEnabled() ?
                TYPE_TAIL_VIEW : (position == 0 && isHeadViewEnabled() ?
                TYPE_HEAD_VIEW : TYPE_ITEM_VIEW);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder;
        switch (viewType) {
            case TYPE_ITEM_VIEW:
                holder = new ItemViewHolder(getItemView(parent));
                break;
            case TYPE_HEAD_VIEW:
                holder = new HeadViewHolder(getHeadView(parent));
                break;
            case TYPE_TAIL_VIEW:
                holder = new TailViewHolder(getTailView(parent));
                break;
            default:
                throw new RuntimeException("Unknown ViewType: " + viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Class<?> clazz = holder.getClass();
        if (clazz == ItemViewHolder.class) {
            T t = mList.get(isHeadViewEnabled() ? position - 1 : position);
            handleItemView(holder, t);
        } else if (clazz == HeadViewHolder.class) {
            handleHeadView(holder);
        } else {
            handleTailView(holder);
        }
    }

    public static final class ItemViewHolder extends BaseViewHolder {
        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static final class HeadViewHolder extends BaseViewHolder {
        HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static final class TailViewHolder extends BaseViewHolder {
        TailViewHolder(View itemView) {
            super(itemView);
        }
    }

    public List<T> getList() {
        return mList;
    }
}
