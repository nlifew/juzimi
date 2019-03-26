package cn.nlifew.juzimi.fragment.local.writer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.juzimi.fragment.local.LocalCategoryAdapter;
import cn.nlifew.juzimi.ui.category.writer.WriterActivity;

public class LocalWriterAdapter extends LocalCategoryAdapter<Writer>
    implements View.OnClickListener {
    private static final String TAG = "LocalWriterAdapter";

    LocalWriterAdapter(LocalWriterFragment fragment, List<Writer> list) {
        super(fragment, list);
    }

    @Override
    public View getItemView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_writer_item,
                        parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Writer writer = (Writer) v.getTag();
        Log.d(TAG, "onClick: " + writer.url);
        WriterActivity.start(getContext(), writer);
    }
}
