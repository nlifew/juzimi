package cn.nlifew.juzimi.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatDelegate;

import org.litepal.LitePal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.StringWrapper;
import cn.nlifew.juzimi.bean.User;

public final class Settings {

    private static Settings sInstance;

    public static Settings getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Settings.class) {
                if (sInstance == null) {
                    sInstance = new Settings(context);
                }
            }
        }
        return sInstance;
    }

    private User mUser;
    private final Context mContext;
    private final SharedPreferences mPreference;

    static final String PREFERENCE = "settings";

    static final String KEY_NIGHT_ON = "night_on";
    static final String KEY_NIGHT_OFF = "night_off";
    static final String KEY_NIGHT_MODE = "night_mode";
    static final String KEY_NIGHT_AUTO = "night_auto";

    static final String KEY_WIDGET_URL = "widget_url";
    static final String KEY_WIDGET_TIME = "widget_time";
    static final String KEY_WIDGET_SIZE = "widget_size";
    static final String KEY_WIDGET_COLOR = "widget_color";
    static final String KEY_WIDGET_CLICK = "widget_click";
    static final String KEY_WIDGET_DOUBLE = "widget_double";

    public static final String VALUE_WIDGET_CLICK_NULL = "null";
    public static final String VALUE_WIDGET_CLICK_UPDATE = "update";
    public static final String VALUE_WIDGET_DOUBLE_NULL = "null";
    public static final String VALUE_WIDGET_DOUBLE_COPY = "copy";
    public static final String VALUE_WIDGET_DOUBLE_SHARE = "share";
    public static final String VALUE_WIDGET_DOUBLE_UPDATE = "update";

    private static final String KEY_USER_HISTORY = "user_agent";

    private static final String KEY_SEARCH_HISTORY = "search_history";


    private Settings(Context context) {
        mContext = context.getApplicationContext();
        mPreference = mContext.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        mUser = LitePal.findFirst(User.class);
    }

    public String getAppName() {
        return mContext.getString(R.string.app_name);
    }

    public int getVersionCode() {
        int version;
        try {
            String pkg = mContext.getPackageName();
            version = mContext.getPackageManager()
                    .getPackageInfo(pkg, 0).versionCode;
        } catch (PackageManager.NameNotFoundException exp) {
            exp.printStackTrace();
            version = 1;
        }
        return version;
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }

    public boolean isNightMode() {
        return mPreference.getBoolean(KEY_NIGHT_MODE, false);
    }

    public void setNightMode(boolean night) {
        mPreference.edit()
                .putBoolean(KEY_NIGHT_MODE, night)
                .apply();
        AppCompatDelegate.setDefaultNightMode(night ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    public boolean isNightAuto() {
        return mPreference.getBoolean(KEY_NIGHT_AUTO, false);
    }

    public float getNightAutoOn() {
        return mPreference.getFloat(KEY_NIGHT_ON, 6.30f);
    }

    public float getNightAutoOff() {
        return mPreference.getFloat(KEY_NIGHT_OFF, 22.30f);
    }

    public int getWidgetTime() {
        return mPreference.getInt(KEY_WIDGET_TIME, 5);
    }

    public int getWidgetSize() {
        return mPreference.getInt(KEY_WIDGET_SIZE, 14);
    }

    public int getWidgetColor() {
        return mPreference.getInt(KEY_WIDGET_COLOR, 0xffffff);
    }

    public Set<String> getWidgetUrl() {
        Set<String> set = mPreference.getStringSet(
                KEY_WIDGET_URL,null);
        if (set == null) {
            set = new HashSet<>();
            set.add("https://m.juzimi.com/recommend");
            set.add("https://m.juzimi.com/totallike");
        }
        return set;
    }

    String[] getWidgetUrlEntry() {
        return new String[] {
                "推荐",
                "最受欢迎",
                "今日最热",
                "最新发布",
        };
    }

    String[] getWidgetUrlValue() {
        return new String[] {
                "https://m.juzimi.com/recommend",
                "https://m.juzimi.com/totallike",
                "https://m.juzimi.com/todayhot",
                "https://m.juzimi.com/new",
        };
    }

    public String getWidgetClick() {
        return mPreference.getString(KEY_WIDGET_CLICK,
                VALUE_WIDGET_CLICK_UPDATE);
    }

    String[] getWidgetClickEntry() {
        return new String[] {
                "无事件",
                "更新句子"
        };
    }

    String[] getWidgetClickValue() {
        return new String[] {
                VALUE_WIDGET_CLICK_NULL,
                VALUE_WIDGET_CLICK_UPDATE
        };
    }

    public String getWidgetDouble() {
        return mPreference.getString(KEY_WIDGET_DOUBLE,
                VALUE_WIDGET_DOUBLE_NULL);
    }

    String[] getWidgetDoubleEntry() {
        return new String[] {
                "无事件",
                "复制到剪贴板",
                "打开编辑界面",
                "更新句子"
        };
    }

    String[] getWidgetDoubleValue() {
        return new String[] {
                VALUE_WIDGET_DOUBLE_NULL,
                VALUE_WIDGET_DOUBLE_COPY,
                VALUE_WIDGET_DOUBLE_SHARE,
                VALUE_WIDGET_DOUBLE_UPDATE
        };
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        if (mUser != null) {
            mUser.delete();
        }
        user.save();
        mUser = user;
    }

    public String getUserAgent() {
        return mPreference.getString(KEY_USER_HISTORY,
                "juzimiapp2v29v Mozilla/5.0 " +
                        "(Linux; Android 8; Sony xz2 Build/PPR2.180905.005; wv) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Version/4.0 Chrome/66.0.3359.158 " +
                        "Mobile Safari/537.36");
    }

    public void setUserAgent(String userAgent) {
        mPreference.edit()
                .putString(KEY_USER_HISTORY, userAgent)
                .apply();
    }

    public List<String> getSearchHistory() {
        List<StringWrapper> wrappers = LitePal
                .where("label = ?", KEY_SEARCH_HISTORY)
                .find(StringWrapper.class);
        return StringWrapper.toString(wrappers);
    }

    public void setSearchHistory(List<String> list) {
        LitePal.deleteAll(StringWrapper.class,
                "label = ?", KEY_SEARCH_HISTORY);
        List<StringWrapper> wrappers = StringWrapper
                .toWrapper(KEY_SEARCH_HISTORY, list);
        LitePal.saveAll(wrappers);
    }
}
