package cn.nlifew.juzimi.ui.detail;

import cn.nlifew.juzimi.bean.Category;
import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;
import cn.nlifew.juzimi.network.NetworkTask;

public class DetailFragment extends BaseSentenceFragment {

    public String mUrl;
    public String mTitle;

    @Override
    public String getRefreshUrl() {
        return mUrl;
    }

    public void setParams(Category category) {
        category.title = mTitle;
        category.url = mUrl;
        DetailAdapter adapter = (DetailAdapter) mAdapter;
        adapter.setParams(category);
    }

    @Override
    public NetworkTask newNetworkTask(int id, String url) {
        return new DetailTask(id, url);
    }
}
