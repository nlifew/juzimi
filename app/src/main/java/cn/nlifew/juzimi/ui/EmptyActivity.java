package cn.nlifew.juzimi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.ui.detail.article.ArticleDetailActivity;
import cn.nlifew.juzimi.ui.main.MainActivity;
import cn.nlifew.juzimi.ui.update.CheckUpdateTask;
import cn.nlifew.support.task.ESyncTaskFactory;

public class EmptyActivity extends BaseActivity
    implements View.OnClickListener {
    private static final String TAG = "EmptyActivity";
    private static final String ACTION_CLICK =
            "cn.nlifew.juzimi.ui.EmptyActivity.ACTION_CLICK";
    private static final String ID_NOTIFICATION_CHANNEL = "channel_test";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        findViewById(R.id.activity_empty_btn)
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ArticleDetailActivity.start(this,
                "这个杀手不太冷",
                "/article/%E8%BF%99%E4%B8%AA%E6%9D%80%E6%89%8B%E4%B8%8D%E5%A4%AA%E5%86%B7");
    }
}
