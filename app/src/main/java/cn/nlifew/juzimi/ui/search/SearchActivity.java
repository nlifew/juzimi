package cn.nlifew.juzimi.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.application.Juzimi;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.fragment.LazyLoadFragment;
import cn.nlifew.support.widget.FlowLayout;

public class SearchActivity extends BaseActivity
    implements View.OnClickListener,
        LazyLoadFragment.LazyLoadSwitch {
    private static final String TAG = "SearchActivity";

    private View mScrollView;
    private EditText mEditView;
    private List<String> mHistory;
    private FlowLayout mHisoryView;
    private SearchResultFragment mFragment;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.activity_search_toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.activity_search_arrow).setOnClickListener(this);
        findViewById(R.id.activity_search_clear).setOnClickListener(this);

        mEditView = findViewById(R.id.activity_search_edit);
        mScrollView = findViewById(R.id.activity_search_scroll);
        mHisoryView = findViewById(R.id.activity_search_history);
        mHistory = Settings.getInstance(this).getSearchHistory();
        for (String s : mHistory) addHistoryView(s);

        // 设置输入框搜索事件
        mEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = v.getText().toString();
                if (! mHistory.contains(text)) {
                    mHistory.add(text);
                    addHistoryView(text);
                }
                mEditView.setCursorVisible(false);
                startSearch(text);
                return true;
            }
        });
        // 当用户点击编辑框时显示光标
        mEditView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEditView.setCursorVisible(true);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_search_arrow:
                onBackPressed();
                break;
            case R.id.activity_search_clear:
                clearHistory();
                break;
            case R.id.activity_search_item:
                String text = (String) ((TextView) v).getText();
                mEditView.setText(text);
                startSearch(text);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && ! mFragment.isHidden()) {
            mScrollView.setVisibility(View.VISIBLE);
            getFragmentManager().beginTransaction()
                    .hide(mFragment)
                    .commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Settings.getInstance(this).setSearchHistory(mHistory);
    }

    private void startSearch(String text) {
        Log.d(TAG, "startSearch: " + text);
        closeInputMethod();
        mScrollView.setVisibility(View.INVISIBLE);

        if (mFragment == null) {
            mFragment = new SearchResultFragment();
            mFragment.setLazyLoadSwitch(this);
            mFragment.setKeyword(text);
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_search_view, mFragment)
                    .commit();
        } else if (mFragment.isHidden()) {
            getFragmentManager().beginTransaction()
                    .show(mFragment)
                    .commit();
            mFragment.refresh(text);
        } else {
            mFragment.refresh(text);
        }
    }

    private void addHistoryView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setOnClickListener(this);
        tv.setId(R.id.activity_search_item);
        tv.setTextAppearance(R.style.SearchItemStyle);
        ViewGroup.MarginLayoutParams params
                = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Juzimi.dp2px(10), Juzimi.dp2px(5),
                Juzimi.dp2px(10), Juzimi.dp2px(5));
        mHisoryView.addView(tv, params);
    }


    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(getWindow()
                .getDecorView().getWindowToken(), 0);
    }


    private void clearHistory() {
        mHisoryView.removeAllViews();
        Snackbar.make(mScrollView, "已清空历史记录", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (String s : mHistory) addHistoryView(s);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            mHistory.clear();
                        }
                    }
                })
                .show();
    }

    @Override
    public boolean willLazyLoad(LazyLoadFragment fragment) {
        return true;
    }
}
