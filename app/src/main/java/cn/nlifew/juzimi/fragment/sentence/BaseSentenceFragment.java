package cn.nlifew.juzimi.fragment.sentence;

import java.util.ArrayList;

import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.BaseSentenceTask;
import cn.nlifew.juzimi.network.NetworkTask;
import cn.nlifew.support.adapter.BaseAdapter;

public abstract class BaseSentenceFragment extends BaseLoadMoreFragment<Sentence> {

    @Override
    public BaseAdapter<Sentence> newRecyclerAdapter() {
        return new SentenceAdapter(this,
                new ArrayList<Sentence>(128));
    }

    @Override
    public NetworkTask newNetworkTask(int id, String url) {
        return new SentenceTask(id, url);
    }
}
