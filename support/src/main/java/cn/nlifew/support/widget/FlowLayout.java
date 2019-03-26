package cn.nlifew.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by Nlifew on 2018/7/21.
 */

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";

    private List<Integer> mLineViewsPosition;
    private List<Integer> mLineHeights;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLineViewsPosition = new ArrayList<>();
        mLineHeights = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 清空原有数据
        mLineViewsPosition.clear();
        mLineHeights.clear();

        int groupWidth = 0 - getPaddingStart() - getPaddingEnd();
        int groupHeight = getPaddingTop() + getPaddingBottom();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) + groupWidth;
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) + groupHeight;

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY &&
            MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            groupWidth = widthSize;
            groupHeight = heightSize;
        } else {
            View child;
            MarginLayoutParams params;
            int curLineWidth = 0, curLineHeight = 0, childWidth, childHeight;
            mLineViewsPosition.add(0);
            for (int i = 0, count = getChildCount(); i < count; i++) {
                child = getChildAt(i);
                // 测量子View
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                params = (MarginLayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                if (curLineWidth + childWidth > widthSize) {
                    // 换行排列
                    groupHeight += curLineHeight;
                    groupWidth = Math.max(groupWidth, curLineWidth);
                    mLineViewsPosition.add(i);
                    mLineHeights.add(curLineHeight);
                    curLineWidth = childWidth;
                    curLineHeight = childHeight;
                } else {
                    // 直接排列在这一行
                    curLineWidth += childWidth;
                    curLineHeight = Math.max(curLineHeight, childHeight);
                }

                if (i == count - 1) {
                    groupHeight += curLineHeight;
                    groupWidth = Math.max(groupWidth, curLineWidth);
                    mLineViewsPosition.add(count);
                    mLineHeights.add(curLineHeight);
                }
            }
        }
        setMeasuredDimension(groupWidth, groupHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();
        int left, lineHeight, vl, vt, vr, vb;
        View child;
        MarginLayoutParams params;
        for (int i = 0, lineNum = mLineHeights.size(); i < lineNum; i++) {
            left = getPaddingLeft();
            lineHeight = mLineHeights.get(i);
            for (int j = mLineViewsPosition.get(i),
                 childCount = mLineViewsPosition.get(i + 1); j < childCount; j++) {
                child = getChildAt(j);
                params = (MarginLayoutParams) child.getLayoutParams();
                vl = left + params.leftMargin;
                vt = top + params.topMargin;
                vr = vl + child.getMeasuredWidth();
                vb = vt + child.getMeasuredHeight();
                child.layout(vl, vt, vr, vb);
                left = vr + params.rightMargin;
            }
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
