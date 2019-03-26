package cn.nlifew.juzimi.fragment.sentence;

import android.util.Log;

import java.io.Reader;

import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.network.NetworkTask;
import cn.nlifew.support.TextUtils;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.network.NetRequest;

public class SentenceLikeTask extends NetworkTask {
    private static final String TAG = "SentenceLikeTask";

    private String mUrl;
    private String mErrInfo;
    private Sentence mSentence;

    SentenceLikeTask(Sentence sentence) {
        mSentence = sentence;
        mUrl = sentence.likeUrl;
        sentence.likeUrl = null;
    }

    @Override
    public boolean onIOThread() {
        // 拼接 url
        mUrl = mUrl.replace("&amp;", "&");
        NetRequest request = post(mUrl);
        try {
            Reader reader = request.post("js=true").reader();
            StringBuilder builder = new StringBuilder(650);
            TextUtils.readAll(builder, reader);
            reader.close();

            TextUtils.subBuilder(builder, "/flag/", "\\\"");
            TextUtils.replaceFirst(builder, "\\x26amp;", "&");

            mUrl = builder.toString();
        } catch (Exception exp) {
            Log.e(TAG, "onIOThread: " + mUrl, exp);
            mErrInfo = "数据解析失败\n" + exp;
        } finally {
            request.close();
        }
        return true;
    }

    @Override
    public void onUIThread(Object target) {
        if (mErrInfo != null) {
            mSentence.liked = ! mSentence.liked;
            mSentence.likeUrl = mUrl;
            BaseSentenceFragment fragment = (BaseSentenceFragment)
                    target;
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
    }
}
