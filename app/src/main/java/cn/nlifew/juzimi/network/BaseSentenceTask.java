package cn.nlifew.juzimi.network;

import android.util.Log;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.support.html.HtmlParser;
import cn.nlifew.support.html.HtmlParserImpl;
import cn.nlifew.support.network.NetRequest;

public abstract class BaseSentenceTask extends NetworkTask {
    private static final String TAG = "BaseSentenceTask";

    protected NetRequest mRequest;

    protected String mNextUrl;
    protected List<Sentence> mSentences;

    private Sentence mSentence;
    private StringBuilder mBuilder;

    protected String mErrInfo;

    public BaseSentenceTask(String url) {
        mBuilder = new StringBuilder(64);
        mSentences = new ArrayList<>(15);
        mRequest = get(url);
    }

    @Override
    public boolean onIOThread() {
        try {
            Reader reader = mRequest.get().reader();
            HtmlParser parser = new HtmlParserImpl(reader);

            long t0 = System.currentTimeMillis();
            parseHtml(parser);
            long t1 = System.currentTimeMillis();
            Log.d(TAG, "onIOThread: cost time: " + (t1 - t0) + " ms.");

            reader.close();
        } catch (Exception exp) {
            mErrInfo = exp.toString();
            Log.e(TAG, "onIOThread: " + mRequest.url(), exp);
        } finally {
            mRequest.close();
        }
        return true;
    }

    private String readWithBr(HtmlParser parser) throws IOException {
        mBuilder.setLength(0);
        String name = parser.name();
        mBuilder.append(parser.text()).append(' ');
        while (parser.next() != HtmlParser.TYPE_END_DOCUMENT
                && ! name.equals(parser.name())) {
            mBuilder.append(parser.text()).append(' ');
        }
        return mBuilder.toString();
    }

    private void parseHtml(HtmlParser parser) throws Exception {
        int type;
        String s;
        parser.skip((int)(10.5f*1024));
        while ((type = parser.next()) != HtmlParser.TYPE_END_DOCUMENT) {
            if (type != HtmlParser.TYPE_START_TAG) continue;
            if (mSentence == null) {
                // 准备解析下一个句子或解析下一页网址
                s = parser.value("class");
                if ("pager-next".equals(s)) {
                    parser.next();
                    mNextUrl = parser.value("href");
                    return;
                } else if ("xlistju".equals(s)) {
                    mSentence = new Sentence();
                    mSentence.url = parser.value("href");
                    mSentence.content = readWithBr(parser);
                }
                continue;
            }
            // 解析句子剩余的部分
            s = parser.value("class");
            if (s == null) {
                continue;
            }
            if ("views-field-field-oriwriter-value".equals(s)) {
                mSentence.writerUrl = parser.value("href");
                mSentence.writer = parser.text();
            } else if ("active".equals(s)) {
                mSentence.articleUrl = parser.value("href");
                mSentence.article = parser.text();
            } else if (s.startsWith("flag ") || "flag-action".equals(s)) {
                mSentence.likeUrl = parser.value("href");
                s = parser.text();
                if (s.startsWith("已喜欢(")) {
                    mSentence.liked = true;
                    mSentence.like = s.substring(4, s.length() - 1);
                } else if (s.startsWith("喜欢(")) {
                    mSentence.liked = false;
                    mSentence.like = s.substring(3, s.length() - 1);
                } else {
                    mSentence.liked = false;
                    mSentence.like = "0";
                }
                mSentences.add(mSentence);
                mSentence = null;
                parser.skip(mBuilder.length() * 3);
            }
        }
    }
}
