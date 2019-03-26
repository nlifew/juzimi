package cn.nlifew.juzimi.ui.category.writer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.support.fragment.LazyLoadFragment;

public class WriterActivity extends BaseActivity
    implements LazyLoadFragment.LazyLoadSwitch  {

    private static final String EXTRA_ARTICLE = "extra_writer";

    public static void start(Context context, Writer writer) {
        Intent intent = new Intent(context, WriterActivity.class);
        intent.putExtra(EXTRA_ARTICLE, writer);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Writer writer = getIntent().getParcelableExtra(EXTRA_ARTICLE);
        useDefaultLayout(writer.title);

        WriterFragment fragment = new WriterFragment();
        fragment.mUrl = writer.url;
        fragment.setLazyLoadSwitch(this);

        getFragmentManager().beginTransaction()
                .add(R.id.activity_base_view, fragment)
                .commit();
    }

    @Override
    public boolean willLazyLoad(LazyLoadFragment fragment) {
        return true;
    }
}
