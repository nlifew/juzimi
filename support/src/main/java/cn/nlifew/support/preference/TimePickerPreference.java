package cn.nlifew.support.preference;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerPreference extends Preference
    implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerPreference";

    private final float mDefValue = 8.30f;

    private int mHour;
    private int mMinute;
    private TimePickerDialog mPickerDialog;

    public TimePickerPreference(Context context) {
        super(context);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getFloat(index, mDefValue);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        float f = restorePersistedValue ? getPersistedFloat(mDefValue)
                : (float) defaultValue;
        mHour = (int) f;
        mMinute = (int) (100*(f-mHour));
    }

    @Override
    protected void onClick() {
        if (mPickerDialog == null) {
            mPickerDialog = new TimePickerDialog(getContext(),
                    this, mHour, mMinute, true);
        }
        mPickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        float f = hourOfDay + minute / 100.0f;
        if (callChangeListener(f)) {
            persistFloat(f);
        }
    }
}
