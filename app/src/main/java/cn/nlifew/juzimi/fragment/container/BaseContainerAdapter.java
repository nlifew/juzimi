package cn.nlifew.juzimi.fragment.container;

import android.app.Fragment;
import android.app.FragmentManager;

import cn.nlifew.juzimi.fragment.content.BaseContentFragment;
import cn.nlifew.support.adapter.FragmentPagerAdapter;
import cn.nlifew.support.fragment.LazyLoadFragment;

public abstract class BaseContainerAdapter extends FragmentPagerAdapter
    implements LazyLoadFragment.LazyLoadSwitch {

    int mFirstItem;
    private boolean mLazyLoad;

    public BaseContainerAdapter(FragmentManager fm) {
        super(fm);
    }

    public abstract BaseContentFragment getFragmentItem(int position);

    @Override
    public Fragment getItem(int position) {
        BaseContentFragment fragment = getFragmentItem(position);
        fragment.setLazyLoadSwitch(this);

        if (position == mFirstItem) {
            mLazyLoad = true;
        }
        return fragment;
    }

    @Override
    public boolean willLazyLoad(LazyLoadFragment fragment) {
        return mLazyLoad;
    }
}
