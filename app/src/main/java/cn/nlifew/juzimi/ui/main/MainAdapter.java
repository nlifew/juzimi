package cn.nlifew.juzimi.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.fragment.container.BaseContainerFragment;
import cn.nlifew.support.adapter.FragmentPagerAdapter;
import cn.nlifew.support.fragment.LazyLoadFragment;

public class MainAdapter extends FragmentPagerAdapter
    implements TabLayout.OnTabSelectedListener,
        LazyLoadFragment.LazyLoadSwitch {

    private int mFirstItem;
    private boolean mLazyLoad = false;
    private TabLayout mTabLayout;

    MainAdapter(FragmentManager fm) {
        super(fm);
    }

    void attach(TabLayout tab, ViewPager pager, int first) {
        mTabLayout = tab;
        mFirstItem = first;

        pager.setAdapter(this);
        pager.setCurrentItem(first);

        mTabLayout.setupWithViewPager(pager);
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public Fragment getItem(int position) {
        BaseContainerFragment fragment;
        int selectedIcon, unSelectedIcon;
        switch (position) {
            case 0: fragment = new PictureFragment();
                selectedIcon = R.drawable.ic_image;
                unSelectedIcon = R.drawable.ic_image_unselected;
                break;
            case 1: fragment = new HomeFragment();
                selectedIcon = R.drawable.ic_home;
                unSelectedIcon = R.drawable.ic_home_unselected;
                break;
            case 2: fragment = new CategoryFragment();
                selectedIcon = R.drawable.ic_category;
                unSelectedIcon = R.drawable.ic_category_unselected;
                break;
            default:
                return null;
        }
        fragment.setLazyLoadSwitch(this);

        TabLayout.Tab tab = mTabLayout.getTabAt(position);
        if (position == mFirstItem) {
            tab.setIcon(selectedIcon);
            tab.setTag(unSelectedIcon);
            mLazyLoad = true;
        } else {
            tab.setIcon(unSelectedIcon);
            tab.setTag(selectedIcon);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Drawable icon = tab.getIcon();
        Object tag = tab.getTag();
        if (tag instanceof Integer) {
            tab.setIcon((int) tag);
        } else {
            tab.setIcon((Drawable) tag);
        }
        tab.setTag(icon);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Drawable icon = tab.getIcon();
        Object tag = tab.getTag();
        if (tag instanceof Integer) {
            tab.setIcon((int) tag);
        } else {
            tab.setIcon((Drawable) tag);
        }
        tab.setTag(icon);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean willLazyLoad(LazyLoadFragment fragment) {
        return mLazyLoad;
    }
}
