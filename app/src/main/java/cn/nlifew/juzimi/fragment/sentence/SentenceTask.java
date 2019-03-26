package cn.nlifew.juzimi.fragment.sentence;

import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.BaseSentenceTask;
import cn.nlifew.support.ToastUtils;

public class SentenceTask extends BaseSentenceTask {

    SentenceTask(int id, String url) {
        super(url);
        mRequest.id(id);
    }

    @Override
    public void onUIThread(Object target) {
        BaseSentenceFragment fragment = (BaseSentenceFragment)
                target;
        if (mRequest.id() == BaseLoadMoreFragment.ID_REFRESH) {
            fragment.update(mSentences);
        } else {
            fragment.addAll(mSentences);
        }
        fragment.setNextUrl(mNextUrl);
        if (mErrInfo != null) {
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
    }
}
