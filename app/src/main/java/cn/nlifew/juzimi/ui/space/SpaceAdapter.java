package cn.nlifew.juzimi.ui.space;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;

import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.adapter.FragmentPagerAdapter;
import cn.nlifew.support.fragment.LazyLoadFragment;

public class SpaceAdapter extends FragmentPagerAdapter
    implements LazyLoadFragment.LazyLoadSwitch {

    SpaceAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        User user = Settings.getInstance(null).getUser();
        SpaceFragment fragment = new SpaceFragment();
        switch (position) {
            case 0:
                fragment.mUrl = user.likeUrl;
                break;
            case 1:
                fragment.mUrl = user.originUrl;
                break;
        }
        fragment.setLazyLoadSwitch(this);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "收藏";
            case 1: return "原创";
        }
        return super.getPageTitle(position);
    }

    @Override
    public boolean willLazyLoad(LazyLoadFragment fragment) {
        return true;
    }
}
