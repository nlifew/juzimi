package cn.nlifew.support.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.TextUtils;

public class SingleChoicePreference extends Preference
    implements DialogInterface.OnClickListener {
    private static final String TAG = "SingleChoicePreference";

    private static final String DEFAULT_VALUE = "";

    private String[] mEntries;
    private int mCurrentValue;
    private String[] mEntryValues;
    private AlertDialog mDialog;

    public SingleChoicePreference(Context context) {
        super(context);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        String now = restorePersistedValue ?
                getPersistedString(DEFAULT_VALUE) :
                (String) defaultValue;
        for (int i = 0; i < mEntryValues.length; i++) {
            if (TextUtils.equals(now, mEntryValues[i])) {
                mCurrentValue = i;
                break;
            }
        }

    }

    @Override
    protected void onClick() {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getTitle())
                    .setSingleChoiceItems(mEntries, mCurrentValue, this)
                    .create();
        }
        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (callChangeListener(mEntryValues[which])) {
            mDialog.dismiss();
            mCurrentValue = which;
            persistString(mEntryValues[which]);
        }
    }

    public String[] getEntries() {
        return mEntries;
    }

    public void setEntries(String[] entries) {
        this.mEntries = entries;
    }

    public String[] getEntryValues() {
        return mEntryValues;
    }

    public void setEntryValues(String[] entryValues) {
        this.mEntryValues = entryValues;
    }
}
