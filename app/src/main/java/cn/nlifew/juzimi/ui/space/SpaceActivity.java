package cn.nlifew.juzimi.ui.space;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.widget.CircleImageView;

public class SpaceActivity extends BaseActivity
    implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = "SpaceActivity";

    private int mBarHeight;
    private int mTitleHeight;
    private TextView mTitleView;
    private CircleImageView mHeadView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        AppBarLayout appbar = findViewById(R.id.activity_space_appBar);
        appbar.addOnOffsetChangedListener(this);

        TabLayout tab = findViewById(R.id.activity_space_tab);
        ViewPager pager = findViewById(R.id.activity_space_pager);
        SpaceAdapter adapter = new SpaceAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);

        User user = Settings.getInstance(this).getUser();

        mHeadView = findViewById(R.id.activity_space_head);
        RequestOptions options = new RequestOptions();
        Glide.with(this).asBitmap()
                .load("https:" + user.image)
                .apply(options.error(R.drawable.ic_head_default))
                .into(mHeadView);

        mTitleView = findViewById(R.id.activity_space_title);
        SpannableString text = new SpannableString(
                user.name + "说：" + user.sign);
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        text.setSpan(boldStyle, 0, user.name.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTitleView.setText(text);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mBarHeight == 0) {
            mBarHeight = appBarLayout.getMeasuredHeight();
            mTitleHeight = mTitleView.getMeasuredHeight();
        }
        float multiple = 1.0f / mBarHeight * (mBarHeight + verticalOffset);
        ViewGroup.LayoutParams params = mTitleView.getLayoutParams();
        params.height = (int) (mTitleHeight * multiple);
        mTitleView.setLayoutParams(params);

        mHeadView.setScaleX(multiple);
        mHeadView.setScaleY(multiple);
        mHeadView.setVisibility(multiple > 0.4f ? View.VISIBLE : View.INVISIBLE);
    }
}
