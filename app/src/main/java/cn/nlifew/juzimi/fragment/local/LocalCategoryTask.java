package cn.nlifew.juzimi.fragment.local;

import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import org.litepal.LitePal;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.nlifew.juzimi.application.Juzimi;
import cn.nlifew.juzimi.bean.Category;
import cn.nlifew.support.TextUtils;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.task.ESyncTaskFactory;

public abstract class LocalCategoryTask<T extends Category>
        implements ESyncTaskFactory.ESyncInterface {
    private static final String TAG = "LocalCategoryTask";

    private List<T> mList;
    private String mErrInfo;

    protected abstract T newInstance();
    protected abstract String getXmlLabel();

    /* 缓存 */
    private AssetManager am;
    private byte[] mBuff;

    private void unzipAsset(String in, File out) throws IOException {
        InputStream is = am.open(in);
        FileOutputStream fos = new FileOutputStream(out);
        int count;
        while ((count = is.read(mBuff)) != -1) {
            fos.write(mBuff, 0, count);
        }
        fos.flush();
        fos.close();
        is.close();
    }

    private void startReadXml(XmlPullParser parser, List<T> target)
        throws IOException, XmlPullParserException {

        File dir = Juzimi.getContext().getFilesDir();
        File group = dir;

        String groupUrl = null, groupPath = null;
        boolean isRightTag = false;

        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                type = parser.next();
                continue;
            }
            String name = parser.getName();
            if ("label".equals(name)) {
                boolean b = getXmlLabel().equals(parser.
                        getAttributeValue(null, "name"));
                if (isRightTag && !b) return;
                isRightTag = b;
            }
            if (! isRightTag) {
                type = parser.next();
                continue;
            }
            switch (name) {
                case "group":
                    groupUrl = parser.getAttributeValue(null, "url");
                    groupPath = parser.getAttributeValue(null, "path");
                    group = new File(dir, groupPath);
                    if (! group.exists() && ! group.mkdirs()) {
                        throw new IOException("Can\'t create dir for:" + groupPath
                            + " in " + dir.getAbsolutePath());
                    }
                    break;
                case "item":
                    String id = parser.getAttributeValue(null, "id");
                    File out = new File(group, id);
                    unzipAsset(TextUtils.append(groupPath, id), out);

                    T t = newInstance();
                    t.image = out.toURI().toString();
                    t.title = parser.getAttributeValue(null, "title");
                    t.url = TextUtils.append(groupUrl,
                            parser.getAttributeValue(null, "url"));
                    t.summary = parser.nextText().trim();

                    mList.add(t);
                    break;
            }
            type = parser.next();
        }
    }


    @Override
    public boolean onIOThread() {
        am = Juzimi.getContext().getAssets();
        mBuff = new byte[1024];
        mList = new ArrayList<>(20);
        try {
            InputStream is = am.open("category.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            startReadXml(parser, mList);
            is.close();
        } catch (IOException|XmlPullParserException exp) {
            Log.d(TAG, "onIOThread: " + exp);
            mErrInfo = getXmlLabel() + exp;
        }

        LitePal.saveAll(mList);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onUIThread(Object target) {
        LocalCategoryFragment<T> fragment = (LocalCategoryFragment<T>)
                target;
        if (mErrInfo != null) {
            ToastUtils.with(fragment.getContext()).show(mErrInfo);
        }
        fragment.update(mList);
    }
}
