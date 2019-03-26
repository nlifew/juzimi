package cn.nlifew.juzimi.network;

import android.util.Log;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.nlifew.juzimi.bean.Picture;
import cn.nlifew.support.html.HtmlParser;
import cn.nlifew.support.html.HtmlParserImpl;
import cn.nlifew.support.network.NetRequest;

public abstract class BasePictureTask extends NetworkTask {
    private static final String TAG = "BasePictureTask";

    protected NetRequest mRequest;

    protected String mNextUrl;
    protected String mErrInfo;
    protected List<Picture> mPictures;

    public BasePictureTask(String url) {
        super(url);
        mPictures = new ArrayList<>(15);
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

    private void parseHtml(HtmlParser parser) throws Exception {
        parser.skip((int)(11.7f*1024));
        int type;
        String s;
        while ((type = parser.next()) != HtmlParser.TYPE_END_DOCUMENT) {
            if (type == HtmlParser.TYPE_START_TAG) {
                s = parser.value("class");
                if ("pager-next".equals(s)) {
                    parser.next();
                    mNextUrl = parser.value("href");
                    return;
                } else if ("chromeimg".equals(s)) {
                    Picture picture = new Picture();
                    picture.url = parser.value("src");
                    mPictures.add(picture);
                }
            }
        }
    }
}
