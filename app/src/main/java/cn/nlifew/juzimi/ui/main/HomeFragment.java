package cn.nlifew.juzimi.ui.main;

import android.app.FragmentManager;

import cn.nlifew.juzimi.fragment.container.BaseContainerAdapter;
import cn.nlifew.juzimi.fragment.container.BaseContainerFragment;
import cn.nlifew.juzimi.fragment.content.BaseContentFragment;
import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;

public class HomeFragment extends BaseContainerFragment {

    @Override
    public BaseContainerAdapter newPagerAdapter(FragmentManager fm) {
        return new HomeAdapter(fm);
    }

    @Override
    protected void onBindAdapter(BaseContainerAdapter adapter) {
        super.onBindAdapter(adapter);
        getViewPager().setCurrentItem(1);
    }

    public static final class RecommendFragment extends BaseSentenceFragment {

        @Override
        public String getRefreshUrl() {
            return "https://m.juzimi.com/recommend";
        }
    }

    public static final class NewFragment extends BaseSentenceFragment {
        @Override
        public String getRefreshUrl() {
            return "https://m.juzimi.com/new";
        }
    }

    public static final class HotFragment extends BaseSentenceFragment {
        @Override
        public String getRefreshUrl() {
            return "https://m.juzimi.com/todayhot";
        }
    }
}

class HomeAdapter extends BaseContainerAdapter {

    HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseContentFragment getFragmentItem(int position) {
        switch (position) {
            case 0: return new HomeFragment.NewFragment();
            case 1: return new HomeFragment.RecommendFragment();
            case 2: return new HomeFragment.HotFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "最新";
            case 1: return "推荐";
            case 2: return "热门";
            default: return "";
        }
    }
}