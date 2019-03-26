package cn.nlifew.juzimi.ui.share;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import cn.nlifew.juzimi.R;

public class ImageHelper implements
        DialogInterface.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener {

    private float scale;
    private int width, height;
    private AlertDialog mDialog;
    private ShareActivity mActivity;

    ImageHelper(ShareActivity activity) {
        mActivity = activity;
        mDialog = new AlertDialog.Builder(activity)
                .setView(getDialogView(activity))
                .setPositiveButton("开始", this)
                .setNegativeButton("取消", this)
                .create();
        width = 4;
        height = 3;
    }

    void show() {
        mDialog.show();
    }

    private View getDialogView(Context context) {
        View view = View.inflate(context,
                R.layout.dialog_share_picture, null);
        RadioGroup group = view.findViewById(R.id.dialog_share_pic_size);
        group.setOnCheckedChangeListener(this);
        SeekBar seekBar = view.findViewById(R.id.dialog_share_pic_scale_seek);
        seekBar.setTag(view.findViewById(R.id.dialog_share_pic_scale_tv));
        seekBar.setMax(10);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(8);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.dialog_share_pic_size_43:
                width = 4; height = 3;
                break;
            case R.id.dialog_share_pic_size_34:
                width = 3; height = 4;
                break;
            case R.id.dialog_share_pic_size_169:
                width = 16; height = 9;
                break;
            case R.id.dialog_share_pic_size_916:
                width = 9; height = 16;
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            mActivity.onCropPicture(width, height, scale);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView tv = (TextView) seekBar.getTag();
        scale = progress / 10.0f;
        tv.setText(String.format(Locale.US, "%.1f", scale));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
