package cn.nlifew.juzimi.ui.detail;

import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.BaseDetailTask;
import cn.nlifew.support.ToastUtils;

public class DetailTask extends BaseDetailTask {

    DetailTask(int id, String url) {
        super(url);
        mRequest.id(id);
    }

    @Override
    public void onUIThread(Object target) {
        DetailFragment fragment = (DetailFragment) target;
        fragment.setNextUrl(mNextUrl);
        fragment.setParams(mCategory);
        if (mRequest.id() == BaseLoadMoreFragment.ID_REFRESH) {
            fragment.update(mSentences);
        } else if (mRequest.id() == BaseLoadMoreFragment.ID_LOADMORE) {
            fragment.addAll(mSentences);
        }
        if (mErrInfo != null) {
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
    }
}
