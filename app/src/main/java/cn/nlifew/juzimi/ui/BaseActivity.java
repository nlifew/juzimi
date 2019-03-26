package cn.nlifew.juzimi.ui;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.nlifew.juzimi.R;

public class BaseActivity extends cn.nlifew.support.BaseActivity {

    protected void useDefaultLayout(String title) {
        setContentView(R.layout.activity_base);

        Toolbar toolbar = findViewById(R.id.activity_base_toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.activity_base_arrow)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        TextView titleView = findViewById(R.id.activity_base_title);
        titleView.setText(title);
    }
}
