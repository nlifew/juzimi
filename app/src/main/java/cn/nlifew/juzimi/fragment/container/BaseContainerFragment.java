package cn.nlifew.juzimi.fragment.container;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.nlifew.juzimi.R;
import cn.nlifew.support.fragment.LazyLoadFragment;

public abstract class BaseContainerFragment extends LazyLoadFragment {
    private static final String TAG = "BaseContainerFragment";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public abstract BaseContainerAdapter newPagerAdapter(FragmentManager fm);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container,
                        container, false);
    }

    @Override
    protected void onLazyLoad() {
        Log.d(TAG, "onLazyLoad: start");
        onBindView(getView());
        BaseContainerAdapter adapter =
                newPagerAdapter(getChildFragmentManager());
        onBindAdapter(adapter);
        adapter.mFirstItem = mViewPager.getCurrentItem();
        Log.d(TAG, "onLazyLoad: end");
    }

    protected void onBindView(View v) {
        mTabLayout = v.findViewById(R.id.fragment_container_tab);
        mViewPager = v.findViewById(R.id.fragment_container_pager);
    }

    protected void onBindAdapter(BaseContainerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}
