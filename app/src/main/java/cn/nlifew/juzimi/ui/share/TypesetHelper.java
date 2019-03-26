package cn.nlifew.juzimi.ui.share;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.application.Juzimi;


public class TypesetHelper implements
        SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener {

    private View mTargetView;
    private AlertDialog mDialog;
    private SeekBar mLeftBar;
    private SeekBar mTopBar;
    private SeekBar mRightBar;
    private SeekBar mBottomBar;
    private ShareActivity mActivity;

    TypesetHelper(ShareActivity activity) {
        mActivity = activity;
        mDialog = new AlertDialog.Builder(activity)
                .setView(getDialogView(activity))
                .create();
        setTargetView(activity.mContentView);
    }

    void show() {
        mDialog.show();
    }

    private View getDialogView(Context context) {
        View view = View.inflate(context,
                R.layout.dialog_share_typeset, null);
        mLeftBar = initSeekbar(view, R.id.dialog_share_types_start_seek,
                R.id.dialog_share_types_start_tv);
        mTopBar = initSeekbar(view, R.id.dialog_share_types_top_seek,
                R.id.dialog_share_types_top_tv);
        mRightBar = initSeekbar(view, R.id.dialog_share_types_end_seek,
                R.id.dialog_share_types_end_tv);
        mBottomBar = initSeekbar(view, R.id.dialog_share_types_bottom_seek,
                R.id.dialog_share_types_bottom_tv);
        initRadioGroup(view, R.id.dialog_share_types_target);
        return view;
    }

    private SeekBar initSeekbar(View view, int seekbarId, int textViewId) {
        SeekBar seekBar = view.findViewById(seekbarId);
        seekBar.setMax(100);
        seekBar.setProgress(1);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setTag(view.findViewById(textViewId));
        return seekBar;
    }

    private void initRadioGroup(View view, int id) {
        RadioGroup group = view.findViewById(id);
        group.setOnCheckedChangeListener(this);
    }

    private void setTargetView(View v) {
        mTargetView = null;
        MarginLayoutParams params = (MarginLayoutParams)
                v.getLayoutParams();
        mLeftBar.setProgress(Juzimi.px2dp(params.leftMargin));
        mTopBar.setProgress(Juzimi.px2dp(params.topMargin));
        mRightBar.setProgress(Juzimi.px2dp(params.rightMargin));
        mBottomBar.setProgress(Juzimi.px2dp(params.bottomMargin));
        mTargetView = v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.dialog_share_types_target_pic:
                setTargetView(mActivity.mImageView);
                break;
            case R.id.dialog_share_types_target_content:
                setTargetView(mActivity.mContentView);
                break;
            case R.id.dialog_share_types_target_from:
                setTargetView(mActivity.mFromView);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView tv = (TextView) seekBar.getTag();
        tv.setText(String.valueOf(progress));
        if (mTargetView == null) return;
        MarginLayoutParams params = (MarginLayoutParams)
                mTargetView.getLayoutParams();
        switch (seekBar.getId()) {
            case R.id.dialog_share_types_start_seek:
                params.leftMargin = Juzimi.dp2px(progress);
                break;
            case R.id.dialog_share_types_top_seek:
                params.topMargin = Juzimi.dp2px(progress);
                break;
            case R.id.dialog_share_types_end_seek:
                params.rightMargin = Juzimi.dp2px(progress);
                break;
            case R.id.dialog_share_types_bottom_seek:
                params.bottomMargin = Juzimi.dp2px(progress);
                break;
        }
        mTargetView.setLayoutParams(params);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
