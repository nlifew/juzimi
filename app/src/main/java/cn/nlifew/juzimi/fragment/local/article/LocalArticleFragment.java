package cn.nlifew.juzimi.fragment.local.article;

import org.litepal.LitePal;

import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.fragment.local.LocalCategoryFragment;
import cn.nlifew.juzimi.fragment.local.LocalCategoryTask;
import cn.nlifew.support.adapter.BaseAdapter;

public class LocalArticleFragment extends LocalCategoryFragment<Article> {

    @Override
    protected LocalCategoryTask<Article> newESyncTask() {
        return new LocalArticleTask();
    }

    @Override
    public BaseAdapter<Article> newRecyclerAdapter() {
        return new LocalArticleAdapter(this,
                LitePal.findAll(Article.class));
    }
}
