package cn.nlifew.juzimi.fragment.local.article;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.fragment.local.LocalCategoryAdapter;
import cn.nlifew.juzimi.ui.category.article.ArticleActivity;

public class LocalArticleAdapter extends LocalCategoryAdapter<Article>
    implements View.OnClickListener {
    private static final String TAG = "LocalArticleAdapter";

    LocalArticleAdapter(LocalArticleFragment fragment, List<Article> list) {
        super(fragment, list);
    }

    @Override
    public View getItemView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_article_item,
                        parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Article article = (Article) v.getTag();
        Log.d(TAG, "onClick: " + article.url);
        ArticleActivity.start(getContext(), article);
    }
}
