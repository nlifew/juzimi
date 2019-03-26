package cn.nlifew.juzimi.ui.main;

import android.app.FragmentManager;

import cn.nlifew.juzimi.fragment.container.BaseContainerAdapter;
import cn.nlifew.juzimi.fragment.container.BaseContainerFragment;
import cn.nlifew.juzimi.fragment.content.BaseContentFragment;
import cn.nlifew.juzimi.fragment.picture.BasePictureFragment;

public class PictureFragment extends BaseContainerFragment {

    @Override
    public BaseContainerAdapter newPagerAdapter(FragmentManager fm) {
        return new PictureAdapter(fm);
    }

    public static final class HandsFragment extends BasePictureFragment {
        @Override
        public String getRefreshUrl() {
            return "https://m.juzimi.com/meitumeiju/shouxiemeiju";
        }
    }

    public static final class TalksFragment extends BasePictureFragment {
        @Override
        public String getRefreshUrl() {
            return "https://m.juzimi.com/meitumeiju/jingdianduibai";
        }
    }
}


class PictureAdapter extends BaseContainerAdapter {

    PictureAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseContentFragment getFragmentItem(int position) {
        switch (position) {
            case 0: return new PictureFragment.HandsFragment();
            case 1: return new PictureFragment.TalksFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "手写每句";
            case 1: return "经典对白";
            default: return "";
        }
    }
}
