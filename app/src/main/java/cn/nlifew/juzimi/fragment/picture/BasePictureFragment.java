package cn.nlifew.juzimi.fragment.picture;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cn.nlifew.juzimi.bean.Picture;
import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.network.NetworkTask;
import cn.nlifew.support.adapter.BaseAdapter;

public abstract class BasePictureFragment extends BaseLoadMoreFragment<Picture> {

    public static final int SPAN_COUNT = 2;

    @Override
    public BaseAdapter<Picture> newRecyclerAdapter() {
        return new PictureAdapter(this,
                new ArrayList<Picture>(64));
    }

    @Override
    public NetworkTask newNetworkTask(int id, String url) {
        return new PictureTask(id, url);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context, SPAN_COUNT);
    }
}
