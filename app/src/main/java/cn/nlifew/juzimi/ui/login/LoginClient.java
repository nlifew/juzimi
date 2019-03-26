package cn.nlifew.juzimi.ui.login;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.task.ESyncTaskFactory;

public class LoginClient extends WebViewClient {

    private LoginActivity mActivity;
    private ESyncTaskFactory mFactory;

    LoginClient(LoginActivity activity) {
        mActivity = activity;
        mFactory = ESyncTaskFactory.with(activity);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mActivity.showProgress("加载中 ...");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (url.startsWith("https://m.juzimi.com/u/")) {
            mActivity.showProgress("抓取登录信息 ...");
            String cookie = CookieManager.getInstance().getCookie(url);
            mFactory.execute(new LoginTask(url, cookie));
        } else {
            mActivity.dismissProgress();
        }
    }
}
