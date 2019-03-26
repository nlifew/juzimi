package cn.nlifew.juzimi.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.application.Juzimi;
import cn.nlifew.juzimi.ui.BaseActivity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useDefaultLayout("关于我们");

        int dp20 = Juzimi.px2dp(20);
        int dp10 = Juzimi.px2dp(10);
        MarginLayoutParams params =
                new MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.setMargins(dp10, dp20, dp10, dp20);
        TextView tv = new TextView(this);
        ViewGroup layout = findViewById(R.id.activity_base_view);
        layout.addView(tv, params);
        /* ... */
    }
}
