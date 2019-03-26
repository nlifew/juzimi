package cn.nlifew.juzimi.network;

import android.util.Log;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.support.html.HtmlParser;
import cn.nlifew.support.html.HtmlParserImpl;
import cn.nlifew.support.network.NetRequest;

public abstract class BaseWriterTask extends NetworkTask {
    private static final String TAG = "BaseWriterTask";

    protected NetRequest mRequest;

    protected String mErrInfo;
    protected String mNextUrl;
    protected List<Writer> mWriters;

    private Writer mWriter;

    public BaseWriterTask(String url) {
        super(url);
        mWriters = new ArrayList<>(20);
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
        parser.skip((int)(11.0f*1024));
        int type;
        String s;
        while ((type = parser.next()) != HtmlParser.TYPE_END_DOCUMENT) {
            if (type != HtmlParser.TYPE_START_TAG) continue;

            if (mWriter == null) {
                s = parser.name();
                if ("li".equals(s)) {
                    // 准备解析下一页网址
                    if ("pager-next".equals(parser.value("class"))) {
                        parser.next();
                        mNextUrl = parser.value("href");
                        return;
                    }
                } else if ("img".equals(s)) {
                    // 准备解析下一条数据
                    mWriter = new Writer();
                    mWriter.image = parser.value("src");
                }
                continue;
            }
            // 解析剩余词条
            s = parser.value("class");
            if (mWriter.title == null && "field-content".equals(s)) {
                parser.next();
                mWriter.url = parser.value("href");
                mWriter.title = parser.text();
                continue;
            }
            if ("xqagepawirdesc".equals(s)) {
                mWriter.summary = parser.text();
                mWriters.add(mWriter);
                mWriter = null;
            }
        }
    }
}
