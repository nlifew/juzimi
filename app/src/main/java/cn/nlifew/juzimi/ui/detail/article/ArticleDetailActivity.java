package cn.nlifew.juzimi.ui.detail.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.support.fragment.LazyLoadFragment;

public class ArticleDetailActivity extends BaseActivity
    implements LazyLoadFragment.LazyLoadSwitch {
    private static final String TAG = "ArticleDetailActivity";
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        useDefaultLayout(title);

        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.mTitle = title;
        fragment.mUrl = intent.getStringExtra(EXTRA_URL);
        fragment.setLazyLoadSwitch(this);

        getFragmentManager().beginTransaction()
                .add(R.id.activity_base_view, fragment)
                .commit();
    }

    @Override
    public boolean willLazyLoad(LazyLoadFragment fragment) {
        return true;
    }
}
