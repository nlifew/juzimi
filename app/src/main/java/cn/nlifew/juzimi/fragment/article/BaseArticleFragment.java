package cn.nlifew.juzimi.fragment.article;

import java.util.ArrayList;

import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.NetworkTask;
import cn.nlifew.support.adapter.BaseAdapter;

public abstract class BaseArticleFragment extends BaseLoadMoreFragment<Article> {
    private static final String TAG = "BaseArticleFragment";

    @Override
    public NetworkTask newNetworkTask(int id, String url) {
        return new ArticleTask(id, url);
    }

    @Override
    public BaseAdapter<Article> newRecyclerAdapter() {
        return new ArticleAdapter(this,
                new ArrayList<Article>(64));
    }
}
