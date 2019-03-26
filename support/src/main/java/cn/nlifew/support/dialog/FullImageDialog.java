package cn.nlifew.support.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import cn.nlifew.support.R;
import cn.nlifew.support.task.ESyncTaskFactory;
import cn.nlifew.support.task.SaveViewBitmapTask;

public class FullImageDialog implements View.OnClickListener,
    View.OnLongClickListener {

    private Dialog mDialog;
    private String mPhotoName;
    private PhotoView mPhotoView;

    public FullImageDialog(Context context) {
        mPhotoView = new PhotoView(context);
        MarginLayoutParams params = new MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPhotoView.setLayoutParams(params);
        mPhotoView.setOnClickListener(this);
        mPhotoView.setOnLongClickListener(this);

        mDialog = new Dialog(context, R.style.FullPictureStyle);
        mDialog.setContentView(mPhotoView);
    }

    public void show(String photoName) {
        mDialog.show();
        mPhotoName = photoName;
    }

    @Override
    public void onClick(View v) {
        mDialog.dismiss();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public boolean onLongClick(View v) {
        ESyncTaskFactory.with(mDialog.getContext())
                .execute(new SaveViewBitmapTask(v, mPhotoName));
        return true;
    }

    public ImageView getView() {
        return mPhotoView;
    }
}
