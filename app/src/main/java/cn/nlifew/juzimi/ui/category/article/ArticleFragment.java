package cn.nlifew.juzimi.ui.category.article;

import cn.nlifew.juzimi.fragment.article.BaseArticleFragment;

public class ArticleFragment extends BaseArticleFragment {
    String mUrl;

    @Override
    public String getRefreshUrl() {
        return mUrl;
    }
}
