package cn.nlifew.juzimi.network;

import android.util.Log;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.support.html.HtmlParser;
import cn.nlifew.support.html.HtmlParserImpl;
import cn.nlifew.support.network.NetRequest;

public abstract class BaseArticleTask extends NetworkTask {
    private static final String TAG = "BaseArticleTask";

    protected NetRequest mRequest;

    protected String mErrInfo;
    protected String mNextUrl;
    protected List<Article> mArticles;

    private Article mArticle;
    private boolean ignoreBr;
    private StringBuilder mBuilder;

    public BaseArticleTask(String url) {
        mBuilder = new StringBuilder(32);
        mArticles = new ArrayList<>(20);
        mRequest = get(url);
    }

    @Override
    public boolean onIOThread() {
        try {
            Reader reader = mRequest.get().reader();
            HtmlParser parser = new HtmlParserImpl(dumpReader(reader));

            long t0 = System.currentTimeMillis();
            parseHtml(parser);
            long t1 = System.currentTimeMillis();
            Log.d(TAG, "onIOThread: " + mArticle);
            Log.d(TAG, "onIOThread: cost time: " + (t1 - t0) + " ms.");

            reader.close();
            System.gc();
        } catch (Exception exp) {
            mErrInfo = mRequest.url() + exp;
            Log.e(TAG, "onIOThread: " + mRequest.url(), exp);
        } finally {
            mRequest.close();
        }
        return true;
    }

    private void parseHtml(HtmlParser parser) throws Exception {
        parser.skip((int)(11.5f*1024));
        int type;
        String s;
        while ((type = parser.next()) != HtmlParser.TYPE_END_DOCUMENT) {
            if (type == HtmlParser.TYPE_START_TAG) {
                if (mArticle == null) {
                    s = parser.name();
                    if ("li".equals(s)) {
                        if ("pager-next".equals(parser.value("class"))) {
                            parser.next();
                            mNextUrl = parser.value("href");
                            return;
                        }
                    } else if ("img".equals(s)) {
                        // 准备解析下一条数据
                        mBuilder.setLength(0);
                        mArticle = new Article();
                        mArticle.image = parser.value("src");
                    }
                    continue;
                }
                s = parser.value("class");
                if ("xqallarticletilelink".equals(s)) {
                    mArticle.url = parser.value("href");
                    mArticle.title = parser.text();
                    continue;
                }
                if ("xqagepawirdesc".equals(s)) {
                    ignoreBr = true;
                    mBuilder.append(parser.text());
                    continue;
                }
                if ("xqagepawirdesclink".equals(s)) {
                    ignoreBr = false;
                    mArticle.summary = mBuilder.toString();
                    mArticles.add(mArticle);
                    mArticle = null;
                }
            } else if (ignoreBr && "br".equals(parser.name())) {
                mBuilder.append(parser.text());
            }
        }
    }
}
