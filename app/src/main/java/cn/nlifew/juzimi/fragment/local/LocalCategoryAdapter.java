package cn.nlifew.juzimi.fragment.local;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Category;
import cn.nlifew.support.adapter.BaseAdapter;
import cn.nlifew.support.adapter.BaseViewHolder;

public abstract class LocalCategoryAdapter<T extends Category>
        extends BaseAdapter<T> {

    private LocalCategoryFragment<T> mFragment;

    protected LocalCategoryAdapter(LocalCategoryFragment<T> fragment, List<T> list) {
        super(list);
        mFragment = fragment;
    }

    public Context getContext() {
        return mFragment.getContext();
    }

    @Override
    public void handleItemView(BaseViewHolder holder, T t) {
        holder.setText(R.id.fragment_category_item_title, t.title);
        holder.setText(R.id.fragment_category_item_summary, t.summary);
        holder.itemView.setTag(t);

        ImageView iv = holder.getView(R.id.fragment_category_item_image);
        Glide.with(mFragment).load(t.image).into(iv);
    }
}
