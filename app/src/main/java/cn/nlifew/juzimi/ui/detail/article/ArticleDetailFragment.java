package cn.nlifew.juzimi.ui.detail.article;

import java.util.ArrayList;

import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.ui.detail.DetailFragment;
import cn.nlifew.support.adapter.BaseAdapter;

public class ArticleDetailFragment extends DetailFragment {

    @Override
    public BaseAdapter<Sentence> newRecyclerAdapter() {
        return new ArticleAdapter(this,
                new ArrayList<Sentence>(128));
    }
}
