package cn.nlifew.support.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.nlifew.support.R;

public class TextSizePreference extends Preference
    implements SeekBar.OnSeekBarChangeListener,
        DialogInterface.OnClickListener {
    private static final String TAG = "TextSizePreference";

    private final int DEFAULT_VALUE = 12;

    private String mUnit;
    private int mMin, mMax;
    private int mCurrentValue;

    TextView mValueView;

    private SeekBar mSeekBar;
    private AlertDialog mDialog;


    public TextSizePreference(Context context) {
        super(context);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        mCurrentValue = restorePersistedValue ?
                getPersistedInt(DEFAULT_VALUE) :
                (int) defaultValue;
    }

    @Override
    protected void onClick() {
        if (mDialog == null) {
            View view = View.inflate(getContext(), R.layout.dialog_textsize, null);
            mValueView = view.findViewById(R.id.dialog_textsize_value);

            mSeekBar = view.findViewById(R.id.dialog_textsize_seek);
            Log.d(TAG, "onClick: max: " + mMax + " min: " + mMin);
            mSeekBar.setMax(mMax - mMin);
            mSeekBar.setProgress(mMax - mMin);
            mSeekBar.setOnSeekBarChangeListener(this);
            mSeekBar.setTag(view.findViewById(R.id.dialog_textsize_example));

            mDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getTitle())
                    .setPositiveButton("确定", this)
                    .setNegativeButton("取消", this)
                    .setView(view)
                    .create();
        }
        mSeekBar.setProgress(mCurrentValue - mMin);
        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int progress = mSeekBar.getProgress() + mMin;
        if (which == DialogInterface.BUTTON_POSITIVE &&
                callChangeListener(progress)) {
            mCurrentValue = progress;
            persistInt(mCurrentValue);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "onProgressChanged: progress: " + progress);
        mValueView.setText((mMin + progress) + mUnit);
        TextView example = (TextView) seekBar.getTag();
        example.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mMin + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        this.mMin = min;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        this.mUnit = unit;
    }
}
