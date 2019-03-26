package cn.nlifew.juzimi.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

/**
 * 用
 * 去特喵的 xml, 本喵 Java 布局直接撸
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        PreferenceManager pm = getPreferenceManager();
        pm.setSharedPreferencesName(Settings.PREFERENCE);

        PreferenceScreen screen = pm.createPreferenceScreen(context);
        setPreferenceScreen(screen);
        screen.setOrderingAsAdded(true);

    }
}
