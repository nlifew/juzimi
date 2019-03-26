package cn.nlifew.juzimi.network;

import android.util.Log;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.nlifew.juzimi.bean.Category;
import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.support.html.HtmlParser;
import cn.nlifew.support.html.HtmlParserImpl;
import cn.nlifew.support.network.NetRequest;

public abstract class BaseDetailTask extends NetworkTask {
    private static final String TAG = "BaseDetailTask";

    protected String mErrInfo;
    protected String mNextUrl;
    protected Category mCategory;
    protected NetRequest mRequest;
    protected List<Sentence> mSentences;

    private Sentence mSentence;
    private StringBuilder mBuilder;


    public BaseDetailTask(String url) {
        mCategory = new Category();
        mBuilder = new StringBuilder(64);
        mSentences = new ArrayList<>(10);
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
            Log.d(TAG, "onIOThread: cost: " + (t1 - t0) + " ms.");

            reader.close();
        } catch (Exception exp) {
            Log.e(TAG, "onIOThread: " + mRequest.url(), exp);
            mErrInfo = exp.toString();
        } finally {
            mRequest.close();
        }
        Log.d(TAG, "onIOThread: category: " + mCategory);
        Log.d(TAG, "onIOThread: nextUrl: " + mNextUrl);
        for (Sentence sentence : mSentences) {
            Log.d(TAG, "onIOThread: sentence: " + sentence);
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

    private void parseHtml(HtmlParser parser) throws IOException {
        String s;
        int type;
        parser.skip((int)(11.0f*1024));
        while ((type = parser.next()) != HtmlParser.TYPE_END_DOCUMENT) {
            if (type != HtmlParser.TYPE_START_TAG) continue;
            if (mSentence == null) {
                s = parser.value("class");
                if ("pager-next".equals(s)) {
                    parser.next();
                    mNextUrl = parser.value("href");
                    return;
                } else if ("xlistju".equals(s)) {
                    mSentence = new Sentence();
                    mSentence.url = parser.value("href");
                    mSentence.content = readWithBr(parser);
                } else if (mSentences.size() != 0) {
                    continue;
                } else if ("xqwriterpicimg".equals(s)) {
                    parser.next();
                    mCategory.image = parser.value("src");
                } else if ("xqwriterpicdesc".equals(s)) {
                    mCategory.summary = readWithBr(parser);
                }
                continue;
            }
            s = parser.value("class");
            if (s == null) {
                continue;
            }
            if ("views-field-field-oriwriter-value".equals(s)) {
                mSentence.writerUrl = parser.value("href");
                mSentence.writer = parser.text();
                continue;
            }
            if ("active".equals(s)) {
                mSentence.articleUrl = parser.value("href");
                mSentence.article = parser.text();
                continue;
            }
            if (s.startsWith("flag ") || "flag-action".equals(s)) {
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
