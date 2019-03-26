package cn.nlifew.juzimi.ui.category.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.support.fragment.LazyLoadFragment;

public class ArticleActivity extends BaseActivity
    implements LazyLoadFragment.LazyLoadSwitch {
    private static final String TAG = "ArticleActivity";
    private static final String EXTRA_ARTICLE = "extra_article";

    public static void start(Context context, Article article) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(EXTRA_ARTICLE, article);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Article article = getIntent().getParcelableExtra(EXTRA_ARTICLE);
        useDefaultLayout(article.title);

        ArticleFragment fragment = new ArticleFragment();
        fragment.mUrl = article.url;
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
