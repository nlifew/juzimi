package cn.nlifew.juzimi.ui.share;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.application.Juzimi;

public class TextEditHelper implements DialogInterface.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private AlertDialog mDialog;
    private TextView mExampleView;
    private TextView mTargetView;
    private SeekBar mSizeBar;
    private SeekBar mRgbBar;
    private SeekBar mAlphaBar;
    private TextInputEditText mContentView;


    public TextEditHelper(Context context) {
        mDialog = new AlertDialog.Builder(context)
                .setView(getDialogView(context))
                .setPositiveButton("确定", this)
                .setNegativeButton("取消", this)
                .create();
    }

    private View getDialogView(Context context) {
        View view = View.inflate(context, R.layout.dialog_share_edit, null);
        mExampleView = view.findViewById(R.id.dialog_share_edit_example);
        mContentView = view.findViewById(R.id.dialog_share_edit_content);

        mSizeBar = initSeekbar(view, R.id.dialog_share_edit_size_seek,
                R.id.dialog_share_edit_size_tv, 20);
        mRgbBar = initSeekbar(view, R.id.dialog_share_edit_rgb_seek,
                R.id.dialog_share_edit_rgb_tv, 0xffffff);
        mAlphaBar = initSeekbar(view, R.id.dialog_share_edit_alpha_seek,
                R.id.dialog_share_edit_alpha_tv, 0xff);
        return view;
    }

    private SeekBar initSeekbar(View view, int seekBarId, int textViewId, int max) {
        TextView textView = view.findViewById(textViewId);
        SeekBar seekBar = view.findViewById(seekBarId);
        seekBar.setTag(textView);
        seekBar.setMax(max);
        seekBar.setOnSeekBarChangeListener(this);
        return seekBar;
    }

    void edit(TextView tv) {
        mTargetView = tv;

        mSizeBar.setProgress(Juzimi.px2sp(tv.getTextSize()));
        mRgbBar.setProgress(tv.getCurrentTextColor() & 0xffffff);
        mAlphaBar.setProgress((int) (tv.getAlpha() * 0xff));
        mContentView.setText(tv.getText());

        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mTargetView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSizeBar.getProgress());
            mTargetView.setTextColor(0xff000000 | mRgbBar.getProgress());
            mTargetView.setAlpha(1.0f * mAlphaBar.getProgress() / 0xff);
            mTargetView.setText(mContentView.getText());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView tv = (TextView) seekBar.getTag();
        switch (seekBar.getId()) {
            case R.id.dialog_share_edit_size_seek:
                tv.setText(String.valueOf(progress));
                mExampleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
                break;
            case R.id.dialog_share_edit_rgb_seek:
                tv.setText(String.format("#%x", progress));
                mExampleView.setTextColor(progress | 0xff000000);
                break;
            case R.id.dialog_share_edit_alpha_seek:
                tv.setText(String.format("#%x", progress));
                mExampleView.setAlpha(1.0f * progress / 0xff);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
