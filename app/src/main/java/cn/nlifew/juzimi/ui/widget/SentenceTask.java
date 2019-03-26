package cn.nlifew.juzimi.ui.widget;

import android.content.Context;

import cn.nlifew.juzimi.network.BaseSentenceTask;

public class SentenceTask extends BaseSentenceTask {

    SentenceTask(String url) {
        super(url);
    }

    @Override
    public void onUIThread(Object target) {
        // 把更新句子缓存的操作放到主线程
        // 因此不用担心线程同步的问题吼吼吼
        SentenceHelper.cache((Context) target, mSentences, mNextUrl);
    }
}
