package cn.nlifew.juzimi.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.ToastUtils;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private WebView mWebview;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebview = buildWebView();
        setContentView(mWebview);

        showAlertDialog();
        removeCookies();
    }

    private void showAlertDialog() {
        DialogInterface.OnClickListener callback =
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mWebview.loadUrl("https://m.juzimi.com/user/login");
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    LoginActivity.this.finish();
                }
            }
        };
        new AlertDialog.Builder(this)
                .setTitle(Settings.getInstance(this).getAppName())
                .setMessage("您即将跳转到\"句子迷\"的官网登录\n" +
                        "请注意：当前版本暂不支持第三方登录，请直接用账号登录\n" +
                        "在您成功登录后，会自动抓取您的登录信息并保存到本地\n")
                .setPositiveButton("我知道了", callback)
                .setNegativeButton("取消", callback)
                .setCancelable(false)
                .show();
    }

    private WebView buildWebView() {
        WebView webView = new WebView(getApplicationContext());
        WebSettings webSettings = webView.getSettings();
        Settings.getInstance(this).setUserAgent("juzimiapp2v29v " +
                webSettings.getUserAgentString());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new LoginClient(this));
        return webView;
    }

    private void removeCookies() {
        // 移除所有的 Cookie
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookies(null);
        cm.flush();
    }

    private void destroyWebView(WebView webView) {
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent != null) {
            parent.removeView(webView);
        }
        webView.stopLoading();
        webView.clearHistory();
        webView.removeAllViews();
        webView.destroy();
    }

    @Override
    protected void onPause() {
        mWebview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mWebview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: start");
        destroyWebView(mWebview);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void showProgress(String msg) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
            mProgress.setCancelable(false);
            mProgress.setTitle(Settings.getInstance(this).getAppName());
        }
        mProgress.setMessage(msg);
        mProgress.show();
    }

    void dismissProgress() {
        if (mProgress != null) {
            mProgress.dismiss();
        }
    }
}
