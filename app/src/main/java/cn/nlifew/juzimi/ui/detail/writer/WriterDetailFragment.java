package cn.nlifew.juzimi.ui.detail.writer;

import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;

public class WriterDetailFragment extends BaseSentenceFragment {

    String mUrl;

    @Override
    public String getRefreshUrl() {
        return mUrl;
    }
}
