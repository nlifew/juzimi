package cn.nlifew.juzimi.ui.settings;


import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import cn.nlifew.support.preference.SeekBarPreference;
import cn.nlifew.support.preference.SingleChoicePreference;
import cn.nlifew.support.preference.TextColorPreference;
import cn.nlifew.support.preference.TextSizePreference;
import cn.nlifew.support.preference.TimePickerPreference;

/**
 * 使用 addPreferencesFromResource() 会有明显的卡顿
 * 考虑到设置项不怎么多，本喵直接 Java 布局，口亨
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager pm = getPreferenceManager();
        pm.setSharedPreferencesName(Settings.PREFERENCE);
        PreferenceScreen screen = pm.createPreferenceScreen(this);
        setPreferenceScreen(screen);
        screen.setOrderingAsAdded(true);

        /* 下面 Preference 组件添加的顺序需要注意
         * PreferenceGroup 需要先被 add，之后才能添加控件
         * 原因是 PreferenceGroup.addPreference(Preference pref) 内会将自己的 PreferenceManager 赋值给 pref
         * pref 的 PreferenceManager 是空的话直接空指针异常
         * 不过 pref 也不能在设置 key 之前被 add，否则和 SharedPreference 失去联系
         */
        Settings settings = Settings.getInstance(this);
        addNightPreference(screen, settings);
        addWidgetPreference(screen, settings);
    }

    private PreferenceCategory addPreferenceCategory(PreferenceScreen screen, String title) {
        PreferenceCategory category = new PreferenceCategory(this);
        category.setOrderingAsAdded(true);
        category.setTitle(title);
        screen.addPreference(category);
        return category;
    }

    private void addNightPreference(PreferenceScreen screen, Settings settings) {
        PreferenceCategory category = addPreferenceCategory(screen, "夜间模式");

        SwitchPreference night = new SwitchPreference(this);
        night.setKey(Settings.KEY_NIGHT_AUTO);
        night.setTitle("自动切换夜间模式");
        night.setDefaultValue(settings.isNightAuto());
        category.addPreference(night);

        TimePickerPreference on = new TimePickerPreference(this);
        on.setKey(Settings.KEY_NIGHT_ON);
        on.setTitle("自动开启时间");
        on.setDefaultValue(settings.getNightAutoOn());
        /* 下面这两个顺序不能倒过来，否则不会建立依赖关系 */
        category.addPreference(on);
        on.setDependency(Settings.KEY_NIGHT_AUTO);

        TimePickerPreference off = new TimePickerPreference(this);
        off.setKey(Settings.KEY_NIGHT_OFF);
        off.setTitle("自动关闭时间");
        off.setDefaultValue(settings.getNightAutoOff());
        category.addPreference(off);
        off.setDependency(Settings.KEY_NIGHT_AUTO);
    }

    private void addWidgetPreference(PreferenceScreen screen, Settings settings) {
        PreferenceCategory category = addPreferenceCategory(screen, "小部件设置");

        SeekBarPreference time = new SeekBarPreference(this);
        time.setKey(Settings.KEY_WIDGET_TIME);
        time.setTitle("自动更新间隔");
        time.setMinValue(5);
        time.setMaxValue(30);
        time.setUnit("分钟");
        time.setDefaultValue(settings.getWidgetTime());
        category.addPreference(time);

        MultiSelectListPreference url = new MultiSelectListPreference(this);
        url.setKey(Settings.KEY_WIDGET_URL);
        url.setTitle("句子来源");
        url.setSummary("选择推送的句子源");
        url.setEntries(settings.getWidgetUrlEntry());
        url.setEntryValues(settings.getWidgetUrlValue());
        url.setDefaultValue(settings.getWidgetUrl());
        category.addPreference(url);

        TextSizePreference size = new TextSizePreference(this);
        size.setKey(Settings.KEY_WIDGET_SIZE);
        size.setTitle("字号大小");
        size.setMin(10);
        size.setMax(20);
        size.setDefaultValue(settings.getWidgetSize());
        size.setUnit("dp");
        category.addPreference(size);

        TextColorPreference color = new TextColorPreference(this);
        color.setKey(Settings.KEY_WIDGET_COLOR);
        color.setTitle("字体颜色");
        color.setDefaultValue(settings.getWidgetColor());
        category.addPreference(color);

        SingleChoicePreference click = new SingleChoicePreference(this);
        click.setKey(Settings.KEY_WIDGET_CLICK);
        click.setTitle("点击事件");
        click.setEntries(settings.getWidgetClickEntry());
        click.setDefaultValue(settings.getWidgetClick());
        click.setEntryValues(settings.getWidgetClickValue());
        category.addPreference(click);

        SingleChoicePreference doubl = new SingleChoicePreference(this);
        doubl.setKey(Settings.KEY_WIDGET_DOUBLE);
        doubl.setTitle("双击事件");
        doubl.setSummary("高度实验性，可能会不稳定");
        doubl.setDefaultValue(settings.getWidgetDouble());
        doubl.setEntries(settings.getWidgetDoubleEntry());
        doubl.setEntryValues(settings.getWidgetDoubleValue());
        category.addPreference(doubl);
    }
}
