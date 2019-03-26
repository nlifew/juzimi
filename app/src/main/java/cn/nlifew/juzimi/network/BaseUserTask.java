package cn.nlifew.juzimi.network;

import android.util.Log;

import java.io.Reader;

import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.html.HtmlParser;
import cn.nlifew.support.html.HtmlParserImpl;
import cn.nlifew.support.network.NetRequest;

public abstract class BaseUserTask extends NetworkTask {
    private static final String TAG = "BaseUserTask";

    private NetRequest mRequest;

    protected User mUser;
    protected String mErrInfo;

    public BaseUserTask(String url, String cookie) {
        mRequest = get(url)
                .head("Cookie", cookie)
                .tag(cookie);
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
            Log.e(TAG, "onIOThread: " + mRequest.url(), exp);
            mErrInfo = mRequest.url() + exp;
        } finally {
            mRequest.close();
        }
        if (mUser == null) {
            if (mErrInfo == null) {
                mErrInfo =  "解析错误: User{null};";
            }
            return true;
        }
        mUser.cookie = (String) mRequest.tag();
        Settings.getInstance(null).setUser(mUser);
        Log.d(TAG, "onIOThread: user: " + mUser);
        return true;
    }

    private void parseHtml(HtmlParser parser) throws Exception {
        String s;
        int type;
        parser.skip((int)(9.5f*1024));

        while ((type = parser.next()) != HtmlParser.TYPE_END_DOCUMENT) {
            if (type != HtmlParser.TYPE_START_TAG) continue;
            if (mUser == null) {
                if ("picture".equals(parser.value("class"))) {
                    parser.next();
                    mUser = new User();
                    s = parser.value("href");
                    mUser.edit = s;

                    parser.next();
                    mUser.image = parser.value("src");
                }
                continue;
            }
            s = parser.value("class");
            if ("views-field-name".equals(s)) {
                parser.next();
                parser.next();
                mUser.url = parser.value("href");
                mUser.name = parser.text();
                continue;
            }
            if ("views-field-signature".equals(s)) {
                parser.next();
                mUser.sign = parser.text();
                continue;
            }
            if ("views-field-phpcode-5".equals(s)) {
                parser.next();
                parser.next();
                mUser.likeUrl = parser.value("href");
                continue;
            }
            if ("views-field-phpcode-6".equals(s)) {
                parser.next();
                parser.next();
                mUser.originUrl = parser.value("href");
                return;
            }
        }
    }
}
