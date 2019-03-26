package cn.nlifew.juzimi.ui.space;

import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;

public class SpaceFragment extends BaseSentenceFragment {

    String mUrl;

    @Override
    public String getRefreshUrl() {
        return mUrl;
    }
}
