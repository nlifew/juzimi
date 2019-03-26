package cn.nlifew.juzimi.ui.detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Category;
import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;
import cn.nlifew.juzimi.fragment.sentence.SentenceAdapter;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.adapter.BaseViewHolder;

public class DetailAdapter extends SentenceAdapter {
    private static final String TAG = "DetailAdapter";

    private Category mCategory;

    private boolean mShowing = false;
    private boolean mExtended = false;
    private int mLongViewHeight = 0;

    private TextView mShortView;
    private TextView mLongView;
    private ImageView mArrowView;
    private ImageView mHeadView;

    public DetailAdapter(BaseSentenceFragment fragment, List<Sentence> list) {
        super(fragment, list);
    }

    void setParams(Category category) {
        mCategory = category;
    }

    @Override
    public boolean isHeadViewEnabled() {
        return mCategory != null
                && mCategory.image != null
                && mCategory.summary != null;
    }

    @Override
    public void handleHeadView(BaseViewHolder holder) {
        holder.setText(R.id.fragment_category_item_title, mCategory.title);
        mLongView = holder.getView(R.id.fragment_category_detail_desc);

        mArrowView = holder.getView(R.id.fragment_category_item_arrow);
        mArrowView.setImageResource(R.drawable.ic_arrow_down);
        mArrowView.setOnClickListener(this);

        mShortView = holder.getView(R.id.fragment_category_item_summary);
        mShortView.setText(mCategory.summary);

        mHeadView = holder.getView(R.id.fragment_category_item_image);
        Glide.with(getContext()).load(mCategory.image)
                .into(mHeadView);
    }

    @Override
    public void onClick(View v) {
        if (v == mArrowView) {
            onArrowClick(v);
        } else {
            super.onClick(v);
        }
    }

    private void onArrowClick(View v) {
        ToastUtils.with(getContext())
                .show("Toast !");
    }
}
