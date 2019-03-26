package cn.nlifew.juzimi.fragment.local.writer;

import org.litepal.LitePal;

import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.juzimi.fragment.local.LocalCategoryFragment;
import cn.nlifew.juzimi.fragment.local.LocalCategoryTask;
import cn.nlifew.support.adapter.BaseAdapter;

public class LocalWriterFragment extends LocalCategoryFragment<Writer> {


    @Override
    public BaseAdapter<Writer> newRecyclerAdapter() {
        return new LocalWriterAdapter(this,
                LitePal.findAll(Writer.class));
    }

    @Override
    protected LocalCategoryTask<Writer> newESyncTask() {
        return new LocalWriterTask();
    }
}
