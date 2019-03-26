package cn.nlifew.juzimi.ui.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.Utils;
import cn.nlifew.juzimi.ui.login.LoginActivity;
import cn.nlifew.juzimi.ui.search.SearchActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.juzimi.ui.settings.SettingsActivity;
import cn.nlifew.juzimi.ui.space.SpaceActivity;
import cn.nlifew.juzimi.ui.update.CheckUpdateTask;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.task.ESyncTaskFactory;
import cn.nlifew.support.widget.CircleImageView;

public class MainActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener  {
    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private CircleImageView mHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        mHeadView = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_image);
        mHeadView.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tab);
        ViewPager pager = findViewById(R.id.view_pager);
        MainAdapter adapter = new MainAdapter(getFragmentManager());
        adapter.attach(tabLayout, pager, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = Settings.getInstance(this).getUser();
        if (user != null && user.image != null) {
            RequestOptions options = RequestOptions
                    .errorOf(R.drawable.ic_head_default);
            Glide.with(this).asBitmap()
                    .load("https:" + user.image)
                    .apply(options)
                    .into(mHeadView);
        } else {
            Glide.with(this).asBitmap()
                    .load(R.drawable.ic_head_default)
                    .into(mHeadView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_image:
                startLoginActivity();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_space:
                if (! Utils.showLoginDialog(this)) {
                    startActivity(new Intent(this, SpaceActivity.class));
                }
                break;
            case R.id.nav_feedback:
                if (! Utils.joinQQGroup(this, "lV5AQeLulJ8Dwy9PhXp0CneIYfnRp7ig")) {
                    ToastUtils.with(this).show("您没有安装 QQ，群号将复制到剪贴板");
                    Utils.copyToClipboard(this, "548591863");
                }
                break;
            case R.id.nav_night_mode:
                Settings settings = Settings.getInstance(this);
                settings.setNightMode(! settings.isNightMode());
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_update:
                ESyncTaskFactory.with(this).execute(new CheckUpdateTask(this));
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }

    public static final int CODE_LOGIN = 1;

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_LOGIN:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
