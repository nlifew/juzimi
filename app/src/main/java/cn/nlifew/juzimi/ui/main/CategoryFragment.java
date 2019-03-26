package cn.nlifew.juzimi.ui.main;

import android.app.FragmentManager;

import cn.nlifew.juzimi.fragment.container.BaseContainerAdapter;
import cn.nlifew.juzimi.fragment.container.BaseContainerFragment;
import cn.nlifew.juzimi.fragment.content.BaseContentFragment;
import cn.nlifew.juzimi.fragment.local.article.LocalArticleFragment;
import cn.nlifew.juzimi.fragment.local.writer.LocalWriterFragment;

public class CategoryFragment extends BaseContainerFragment {

    @Override
    public BaseContainerAdapter newPagerAdapter(FragmentManager fm) {
        return new CategoryAdapter(fm);
    }
}

class CategoryAdapter extends BaseContainerAdapter {

    CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseContentFragment getFragmentItem(int position) {
        switch (position) {
            case 0: return new LocalWriterFragment();
            case 1: return new LocalArticleFragment();
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
            case 0: return "名人";
            case 1: return "名句";
            default: return "";
        }
    }
}
