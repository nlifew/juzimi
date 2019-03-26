package cn.nlifew.juzimi.fragment.writer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.juzimi.fragment.loadmore.LoadMoreAdapter;
import cn.nlifew.juzimi.ui.detail.writer.WriterDetailActivity;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.adapter.BaseViewHolder;

public class WriterAdapter extends LoadMoreAdapter<Writer>
    implements View.OnClickListener {

    WriterAdapter(BaseWriterFragment fragment, List<Writer> list) {
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
    public void handleItemView(BaseViewHolder holder, Writer writer) {
        holder.setText(R.id.fragment_category_item_title, writer.title);
        holder.setText(R.id.fragment_category_item_summary, writer.summary);
        holder.itemView.setTag(writer);

        ImageView iv = holder.getView(R.id.fragment_category_item_image);
        Glide.with(mFragment).load("https://m.juzimi.com" + writer.image)
                .into(iv);
    }

    @Override
    public void onClick(View v) {
        Writer writer = (Writer) v.getTag();
        WriterDetailActivity.start(getContext(),
                writer.title,
                writer.url);
    }
}
