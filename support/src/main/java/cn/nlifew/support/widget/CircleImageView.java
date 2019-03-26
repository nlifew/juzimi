package cn.nlifew.support.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 12642 on 2018/7/20.
 * 圆形的 ImageView
 */

public class CircleImageView extends ImageView {
    private static final String TAG = "CircleImageView";
    private Paint mPaint;
    private Bitmap mBitmap;
    private BitmapShader mShader;
    private Matrix mMatrix;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = getBitmap(getDrawable());
        if (bitmap == null) {
            super.onDraw(canvas);
        } else {
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            int viewMinSize = viewWidth < viewHeight ? viewWidth : viewHeight;
            if (mShader == null || ! bitmap.equals(mBitmap)) {
                mBitmap = bitmap;
                mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
            if (mShader != null) {
                mMatrix.setScale(1.0f * viewMinSize / bitmap.getWidth(), 1.0f * viewMinSize / bitmap.getHeight());
                mShader.setLocalMatrix(mMatrix);
            }
            mPaint.setShader(mShader);
            float radius = viewMinSize / 2.0f;
            canvas.drawCircle(radius, radius, radius, mPaint);
        }
    }


    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        if (drawable instanceof ColorDrawable) {
            Rect rect = drawable.getBounds();
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            int color = ((ColorDrawable) drawable).getColor();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color),
                    Color.green(color), Color.blue(color));
            return bitmap;
        }
        return null;
    }
}
