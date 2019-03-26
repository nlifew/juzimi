package cn.nlifew.juzimi.ui.search;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;

public class SearchResultFragment extends BaseSentenceFragment {
    private static final String TAG = "SearchResultFragment";

    private String mUrl;

    @Override
    public String getRefreshUrl() {
        return mUrl;
    }

    void setKeyword(CharSequence text) {
        String s = text.toString();
        try {
            s = URLEncoder.encode(URLEncoder.encode(s, "utf-8"), "utf-8");
        } catch (UnsupportedEncodingException exp) {
            Log.e(TAG, "setKeyword: ", exp);
        }
        mUrl = "https://m.juzimi.com/search/node/"
                + s + "%20type:sentence";
    }

    void refresh(CharSequence text) {
        setKeyword(text);
        getSwipeLayout().setRefreshing(true);
        onRefresh();
    }
}
