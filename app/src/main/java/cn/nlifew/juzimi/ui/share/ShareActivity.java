package cn.nlifew.juzimi.ui.share;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.io.File;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.task.ESyncTaskFactory;
import cn.nlifew.support.task.SaveViewBitmapTask;

public class ShareActivity extends BaseActivity
    implements View.OnClickListener,
        cn.nlifew.support.BaseActivity.OnPermRequestListener {

    private static final String TAG = "ShareActivity";
    private static final String EXTRA_SENTENCE = "extra_sentence";

    private static final int CODE_REQUEST_PICK = 1;
    private static final int CODE_REQUEST_CROP = 2;

    public static void start(Context context, Sentence sentence) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(EXTRA_SENTENCE, sentence);
        context.startActivity(intent);
    }

    private Uri tmpUri, mTargetUri;
    private ImageHelper mImgHelper;
    private TextEditHelper mEditHelper;
    private TypesetHelper mTypesetHelper;

    LinearLayout mLayout;
    ImageView mImageView;
    TextView mContentView;
    TextView mFromView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Toolbar toolbar = findViewById(R.id.activity_base_toolbar);
        setSupportActionBar(toolbar);

        mLayout = findViewById(R.id.activity_share_linear);
        mImageView = mLayout.findViewById(R.id.activity_share_image);
        mFromView = mLayout.findViewById(R.id.activity_share_from);
        mFromView.setOnClickListener(this);
        mContentView = mLayout.findViewById(R.id.activity_share_content);
        mContentView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sentence sentence = getIntent().getParcelableExtra(EXTRA_SENTENCE);
        mFromView.setText(sentence.getFrom());
        mContentView.setText(sentence.content);

        findViewById(R.id.activity_base_arrow).setOnClickListener(this);
        findViewById(R.id.activity_share_save).setOnClickListener(this);
        findViewById(R.id.activity_share_photo).setOnClickListener(this);
        findViewById(R.id.activity_share_typeset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_arrow:
                onBackPressed();
                break;
            case R.id.activity_share_content:
                if (mEditHelper == null) {
                    mEditHelper = new TextEditHelper(this);
                }
                mEditHelper.edit(mContentView);
                break;
            case R.id.activity_share_from:
                if (mEditHelper == null) {
                    mEditHelper = new TextEditHelper(this);
                }
                mEditHelper.edit(mFromView);
                break;
            case R.id.activity_share_typeset:
                if (mTypesetHelper == null)
                    mTypesetHelper = new TypesetHelper(this);
                mTypesetHelper.show();
                break;
            case R.id.activity_share_photo:
                tryToRequestPerm(Manifest.permission.
                        READ_EXTERNAL_STORAGE,this);
                break;
            case R.id.activity_share_save:
                tryToRequestPerm(Manifest.permission.
                        WRITE_EXTERNAL_STORAGE, this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " " + requestCode);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CODE_REQUEST_PICK:
                mTargetUri = data.getData();
                if (tmpUri == null) {
                    tmpUri = Uri.fromFile(new File(
                            getExternalCacheDir(), "share_bg.tmp"));
                }
                if (mImgHelper == null) {
                    mImgHelper = new ImageHelper(this);
                }
                mImgHelper.show();
                break;
            case CODE_REQUEST_CROP:
                mImageView.setImageURI(tmpUri);
                break;
        }
    }

    @Override
    public void onPermGranted(String perm) {
        switch (perm) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.
                        EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CODE_REQUEST_PICK);
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                Sentence sentence = getIntent().getParcelableExtra(EXTRA_SENTENCE);
                String name = sentence.url;
                ESyncTaskFactory.with(this).execute(new SaveViewBitmapTask(
                        mLayout, name.substring(name.lastIndexOf('/') + 1)
                ));
                break;
        }
    }

    private void resizeImgView(int width, int height) {
        mImageView.setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(null);
        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.height = (int) (1.0f *
                mLayout.getMeasuredWidth() / width * height + 0.5f);
        mImageView.setLayoutParams(params);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    void onCropPicture(int width, int height, float scale) {
        resizeImgView(width, height);

        int imageWidth = (int) (scale * mLayout.getMeasuredWidth());
        int imageHeight = (int) (imageWidth / width * height + 0.5f);

        Intent intent = new Intent("com.android.camera.action.CROP")
                .setDataAndType(mTargetUri, "image/*")
                .putExtra("scale", true)
                .putExtra("crop", "true")
                .putExtra("aspectX", width)
                .putExtra("aspectY", height)
                .putExtra("outputX", imageWidth)
                .putExtra("outputY", imageHeight)
                .putExtra("return-data", false)
                .putExtra("output", tmpUri)
                .putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent, CODE_REQUEST_CROP);
    }

    @Override
    public void onPermDenied(String perm) {
        ToastUtils.with(this).show("请授予存储权限");
    }
}
