package cn.nlifew.support.fragment;


import android.os.Handler;

import cn.nlifew.support.task.ESyncTaskFactory;

public class LazyLoadFragment extends BaseFragment {

    public interface LazyLoadSwitch {
        boolean willLazyLoad(LazyLoadFragment fragment);
    }

    private boolean mLazyLoaded = false;
    private LazyLoadSwitch mLazyLoadSwitch = null;

    @Override
    public void onResume() {
        super.onResume();
        lazyLoadIfReady();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoadIfReady();
    }

    public void lazyLoadIfReady() {
        if (! mLazyLoaded && isResumed()
                && getUserVisibleHint()
                && mLazyLoadSwitch != null
                && mLazyLoadSwitch.willLazyLoad(this)) {
            mLazyLoaded = true;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    onLazyLoad();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        mLazyLoaded = false;
        super.onDestroyView();
    }

    protected void onLazyLoad() {}

    public void setLazyLoadSwitch(LazyLoadSwitch s) {
        mLazyLoadSwitch = s;
    }

}
