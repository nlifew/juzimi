package cn.nlifew.juzimi.fragment.writer;

import java.util.ArrayList;

import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.NetworkTask;
import cn.nlifew.support.adapter.BaseAdapter;

public abstract class BaseWriterFragment extends BaseLoadMoreFragment<Writer> {


    @Override
    public BaseAdapter<Writer> newRecyclerAdapter() {
        return new WriterAdapter(this,
                new ArrayList<Writer>(64));
    }

    @Override
    public NetworkTask newNetworkTask(int id, String url) {
        return new WriterTask(id, url);
    }
}
