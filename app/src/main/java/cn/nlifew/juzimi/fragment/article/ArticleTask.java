package cn.nlifew.juzimi.fragment.article;

import android.util.Log;

import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.BaseArticleTask;
import cn.nlifew.support.ToastUtils;

public class ArticleTask extends BaseArticleTask {
    private static final String TAG = "ArticleTask";

    ArticleTask(int id, String url) {
        super(url);
        mRequest.id(id);
    }

    @Override
    public void onUIThread(Object target) {
        BaseArticleFragment fragment = (BaseArticleFragment)
                target;
        if (mRequest.id() == BaseLoadMoreFragment.ID_REFRESH) {
            fragment.update(mArticles);
        } else {
            fragment.addAll(mArticles);
        }
        fragment.setNextUrl(mNextUrl);
        if (mErrInfo != null) {
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
        Log.d(TAG, "onUIThread: mNextUrl:" + mNextUrl);
        Log.d(TAG, "onUIThread: " + mArticles.size());
    }
}
