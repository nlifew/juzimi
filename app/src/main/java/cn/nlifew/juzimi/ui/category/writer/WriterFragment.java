package cn.nlifew.juzimi.ui.category.writer;

import cn.nlifew.juzimi.fragment.writer.BaseWriterFragment;

public class WriterFragment extends BaseWriterFragment {

    String mUrl;

    @Override
    public String getRefreshUrl() {
        return mUrl;
    }
}
