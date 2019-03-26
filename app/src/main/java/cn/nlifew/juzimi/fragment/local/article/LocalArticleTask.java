package cn.nlifew.juzimi.fragment.local.article;

import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.fragment.local.LocalCategoryTask;

public class LocalArticleTask extends LocalCategoryTask<Article> {

    @Override
    protected Article newInstance() {
        return new Article();
    }

    @Override
    protected String getXmlLabel() {
        return "article";
    }
}
