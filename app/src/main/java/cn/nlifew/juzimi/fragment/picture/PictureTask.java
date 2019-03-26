package cn.nlifew.juzimi.fragment.picture;

import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.BasePictureTask;
import cn.nlifew.support.ToastUtils;

public class PictureTask extends BasePictureTask {

    PictureTask(int id, String url) {
        super(url);
        mRequest.id(id);
    }

    @Override
    public void onUIThread(Object target) {
        BasePictureFragment fragment = (BasePictureFragment)
                target;
        if (mRequest.id() == BaseLoadMoreFragment.ID_REFRESH) {
            fragment.update(mPictures);
        } else {
            fragment.addAll(mPictures);
        }
        fragment.setNextUrl(mNextUrl);
        if (mErrInfo != null) {
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
    }
}
