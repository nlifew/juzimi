package cn.nlifew.juzimi.fragment.picture;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.application.Juzimi;
import cn.nlifew.juzimi.bean.Picture;
import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.fragment.loadmore.LoadMoreAdapter;
import cn.nlifew.support.BaseActivity;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.adapter.BaseViewHolder;
import cn.nlifew.support.dialog.FullImageDialog;

public class PictureAdapter extends LoadMoreAdapter<Picture>
    implements View.OnClickListener, BaseActivity.OnPermRequestListener {
    private static final String TAG = "PictureAdapter";

    private String mUrl;
    private final int mSpanSize;
    private FullImageDialog mDialog;

    PictureAdapter(BasePictureFragment fragment, List<Picture> list) {
        super(fragment, list);
        mSpanSize = fragment.getActivity().getWindowManager()
                .getDefaultDisplay().getWidth()
                / BasePictureFragment.SPAN_COUNT;
    }

    @Override
    public View getItemView(ViewGroup parent) {
        ImageView v = new ImageView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                mSpanSize, mSpanSize);
        v.setLayoutParams(params);
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int padding = Juzimi.dp2px(5);
        v.setPadding(padding, padding, padding, padding);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public void handleItemView(BaseViewHolder holder, Picture picture) {
        String url = picture.url;
        ImageView v = (ImageView) holder.itemView;
        v.setTag(R.id.view_tag_1, url);

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_image_failed)
                .placeholder(R.drawable.ic_image_loading);
        Glide.with(mFragment).load(url)
                .apply(options)
                .thumbnail(0.5f).into(v);
    }

    @Override
    public void onClick(View v) {
        mUrl = (String) v.getTag(R.id.view_tag_1);
        mFragment.tryToRequestPerm(Manifest.permission.
                WRITE_EXTERNAL_STORAGE, this);
    }

    @Override
    public void onPermGranted(String perm) {
        if (mDialog == null) {
            mDialog = new FullImageDialog(getContext());
        }
        ImageView iv = mDialog.getView();
        Glide.with(mFragment).load(mUrl).into(iv);

        mDialog.show(mUrl.substring(mUrl.lastIndexOf('/') + 1));
    }

    @Override
    public void onPermDenied(String perm) {
        ToastUtils.with(getContext()).show("请授予存储权限");
        mDialog.dismiss();
    }
}
