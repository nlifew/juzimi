package cn.nlifew.support.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.nlifew.support.R;

public class SeekBarPreference extends Preference
    implements DialogInterface.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "SeekBarPreference";

    private int mMinValue;
    private int mMaxValue;
    private int mCurrentValue;
    private String mUnit;
    private TextView mValueView;
    private SeekBar mSeekbar;
    private AlertDialog mDialog;

    public SeekBarPreference(Context context) {
        super(context);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, mMinValue);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        mCurrentValue = restorePersistedValue ?
                getPersistedInt(mMinValue) :
                (int) defaultValue;
    }

    @Override
    protected void onClick() {
        if (mDialog == null) {
            View view = View.inflate(getContext(), R.layout.dialog_seekbar, null);
            mValueView = view.findViewById(R.id.dialog_seekbar_value);
            ((TextView) view.findViewById(R.id.dialog_seekbar_unit)).setText(mUnit);
            mSeekbar = view.findViewById(R.id.dialog_seekbar_seek);
            mSeekbar.setMax(mMaxValue - mMinValue);
            mSeekbar.setProgress(mMaxValue - mMinValue);
            mSeekbar.setOnSeekBarChangeListener(this);

            mDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getTitle())
                    .setPositiveButton("确定", this)
                    .setNegativeButton("取消", this)
                    .setView(view)
                    .create();
        }
        mSeekbar.setProgress(mCurrentValue - mMinValue);
        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int process = mMinValue + mSeekbar.getProgress();
        if (which == DialogInterface.BUTTON_POSITIVE &&
                callChangeListener(process)) {
            mCurrentValue = process;
            persistInt(process);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mValueView.setText(String.valueOf(mMinValue + progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        this.mUnit = unit;
    }
}
