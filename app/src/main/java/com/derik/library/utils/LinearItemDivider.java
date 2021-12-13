package com.derik.library.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinearItemDivider extends RecyclerView.ItemDecoration {
    private static final String TAG = "LinearItemDivider";

    //默认分割线宽
    private final int mDividerWidth;

    //最顶部分隔宽度
    private int mTopWidth = 0;

    //最底部分隔宽度
    private int mBottomWidth = 0;

    //左竖直分隔线宽度
    private int mLeftWidth = 0;

    //右竖直分隔线宽度
    private int mRightWidth = 0;

    //水平分割线左内边距
    private int mPaddingLeft = 0;

    //水平分割线右内边距
    private int mPaddingRight = 0;

    //竖直分割线上内边距
    private int mPaddingTop = 0;

    //竖直分割线下内边距
    private int mPaddingBottom = 0;

    private Paint mPaint;

    private RecyclerView.LayoutManager mLayoutManager;

    //画背景矩形的方式实现分隔线
    private boolean mUseBgMode;

    //无需画分割线的位置
    private int[] mPositionsExcluded;

    public LinearItemDivider(int color) {
        this(1, color);
    }

    public LinearItemDivider(int dividerWidth, int color) {
        this(dividerWidth, color, false);
    }

    public LinearItemDivider(int dividerWidth, int color, boolean bottomLineEnable) {
        this(dividerWidth, color, false, bottomLineEnable);
    }

    public LinearItemDivider(int dividerWidth, int color, boolean mUseBgMode, boolean bottomLineEnable) {
        this.mDividerWidth = dividerWidth;
        this.mUseBgMode = mUseBgMode;

        mPaint = new Paint();
        mPaint.setColor(color);
        if (bottomLineEnable) {
            mBottomWidth = dividerWidth;
        }
    }

    public void setExcludePositions(int[] excludePositions) {
        mPositionsExcluded = excludePositions;
    }

    public void setDividersWidth(int leftWidth, int topWidth, int rightWidth, int bottomWidth) {
        this.mLeftWidth = leftWidth;
        this.mTopWidth = topWidth;
        this.mRightWidth = rightWidth;
        this.mBottomWidth = bottomWidth;
    }

    public void setDividersPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.mPaddingLeft = paddingLeft;
        this.mPaddingTop = paddingTop;
        this.mPaddingRight = paddingRight;
        this.mPaddingBottom = paddingBottom;
    }

    public void setDividerPaddingLeft(int padding) {
        this.mPaddingLeft = padding;
    }

    public void setDividerPaddingRight(int padding) {
        this.mPaddingRight = padding;
    }

    public void setDividerPaddingTop(int padding) {
        this.mPaddingTop = padding;
    }

    public void setDividerPaddingBottom(int padding) {
        this.mPaddingBottom = padding;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getAdapter() == null) {
            return;
        }

        if (mLayoutManager == null) {
            mLayoutManager = parent.getLayoutManager();
        }

        int itemPosition = parent.getChildAdapterPosition(view);
        int childCount = parent.getAdapter().getItemCount();

        if (mLayoutManager instanceof GridLayoutManager) {

        } else if (mLayoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLayoutManager;
            if (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
                setLinearVerticalRect(outRect, itemPosition, childCount);
            } else {
                setLinearHorizontalRect(outRect, itemPosition, childCount);
            }
        }

    }

    private void setLinearHorizontalRect(Rect outRect, int itemPosition, int childCount) {
        int left = 0;
        int right = mDividerWidth;

        if (isFirsItem(itemPosition)) {
            left = mLeftWidth;
        }

        if (isLastItem(itemPosition, childCount)) {
            right = mRightWidth;
        }
        outRect.set(left, mTopWidth, right, mBottomWidth);
    }

    private void setLinearVerticalRect(Rect outRect, int itemPosition, int childCount) {
        int top = 0;
        int bottom = mDividerWidth;

        if (isFirsItem(itemPosition)) {
            top = mTopWidth;
        }

        if (isLastItem(itemPosition, childCount)) {
            bottom = mBottomWidth;
        }
        outRect.set(mLeftWidth, top, mRightWidth, bottom);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        int childCount = parent.getChildCount();

        if (mLayoutManager instanceof GridLayoutManager) {

        } else if (mLayoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLayoutManager;
            if (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
                if (mUseBgMode) {
                    drawLinearVerticalBgMode(canvas, parent, childCount);
                } else {
                    drawLinearVertical(canvas, parent, childCount);
                }
            } else {
                if (mUseBgMode) {
                    drawLinearHorizontalBgMode(canvas, parent, childCount);
                } else {
                    drawLinearHorizontal(canvas, parent, childCount);
                }

            }
        }
    }

    private void drawLinearHorizontal(Canvas canvas, RecyclerView parent, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (isSkip(position)) {
                continue;
            }

            int top = view.getTop() + mPaddingTop - mTopWidth;
            int bottom = view.getBottom() - mPaddingBottom + mBottomWidth;
            //绘制竖直分割线
            if (i == 0 && mLeftWidth > 0) {
                //绘制第一个的左边
                canvas.drawRect(view.getLeft() - mLeftWidth, top, view.getLeft(), bottom, mPaint);
            }

            if (i == childCount - 1) {
                if (mRightWidth > 0) {
                    //绘制最后一个右边
                    canvas.drawRect(view.getRight(), top, view.getRight() + mRightWidth, bottom, mPaint);
                }
            } else {
                //绘制普通竖直分隔线（右侧）
                canvas.drawRect(view.getRight(), top, view.getRight() + mDividerWidth, bottom, mPaint);
            }

            //开始绘制水平分隔，顶部和底部
            int left = view.getLeft() + mPaddingLeft;
            int right = view.getRight() - mPaddingRight;

            if (mTopWidth > 0) {
                canvas.drawRect(left, view.getTop() - mTopWidth, right, view.getTop(), mPaint);
            }

            if (mBottomWidth > 0) {
                canvas.drawRect(left, view.getBottom(), right, view.getBottom() + mBottomWidth, mPaint);
            }

        }
    }

    private void drawLinearVertical(Canvas canvas, RecyclerView parent, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (isSkip(position)) {
                continue;
            }

            //竖直方向
            //左边
            if (mLeftWidth > 0) {
                canvas.drawRect(view.getLeft() - mLeftWidth, view.getTop() + mPaddingTop, view.getLeft(), view.getBottom() - mPaddingBottom, mPaint);
            }
            //右边
            if (mRightWidth > 0) {
                canvas.drawRect(view.getRight(), view.getTop() + mPaddingTop, view.getRight() + mRightWidth, view.getBottom() - mPaddingBottom, mPaint);
            }

            //水平方向
            int left = view.getLeft() - mLeftWidth + mPaddingLeft;
            int right = view.getRight() + mRightWidth - mPaddingRight;

            //绘制顶部
            if (i == 0 && mTopWidth > 0) {
                canvas.drawRect(left, view.getTop() - mTopWidth, right, view.getTop(), mPaint);
            }

            //绘制底部
            int top = view.getBottom();
            int bottom = view.getBottom();

            if (i == childCount - 1) {
                if (mBottomWidth > 0) {
                    bottom += mBottomWidth;
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            } else {
                //普通分割线
                bottom += mDividerWidth;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private void drawLinearHorizontalBgMode(Canvas canvas, RecyclerView parent, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (isSkip(position)) {
                continue;
            }

            int left = view.getLeft();
            int right = view.getRight();
            int top = view.getTop() + mPaddingTop;
            int bottom = view.getBottom() - mPaddingBottom;

            //绘制竖直分割线
            if (i == 0 && mLeftWidth > 0) {
                left -= mLeftWidth;
            }

            if (i == childCount - 1) {
                if (mRightWidth > 0) {
                    //绘制最后一个右边
                    right += mRightWidth;
                }
            } else {
                //绘制普通竖直分隔线（右侧）
                right += mDividerWidth;
            }

            if (mTopWidth > 0) {
                top -= mTopWidth;
            }

            if (mBottomWidth > 0) {
                bottom += mBottomWidth;
            }

            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawLinearVerticalBgMode(Canvas canvas, RecyclerView parent, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (isSkip(position)) {
                continue;
            }

            int left = view.getLeft() + mPaddingLeft;
            int right = view.getRight() - mPaddingRight;
            int top = view.getTop();
            int bottom = view.getBottom();

            if (mLeftWidth > 0) {
                left -= mLeftWidth;
            }

            if (mRightWidth > 0) {
                right += mRightWidth;
            }

            if (i == 0 && mTopWidth > 0) {
                top -= mTopWidth;
            }

            if (i == childCount - 1) {
                if (mBottomWidth > 0) {
                    bottom += mBottomWidth;
                }
            } else {
                bottom += mDividerWidth;
            }
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    public boolean isFirsItem(int position) {
        boolean result = false;
        if (mLayoutManager instanceof GridLayoutManager) {

        } else if (mLayoutManager instanceof LinearLayoutManager) {
            result = position == 0;
        }
        return result;
    }

    public boolean isLastItem(int position, int childCount) {
        boolean result = false;
        if (mLayoutManager instanceof GridLayoutManager) {

        } else if (mLayoutManager instanceof LinearLayoutManager) {
            result = position == childCount - 1;
        }
        return result;
    }

    private boolean isSkip(int position) {
        if (mPositionsExcluded != null && mPositionsExcluded.length > 0) {
            for (int value : mPositionsExcluded) {
                if (position == value) {
                    return true;
                }
            }
        }
        return false;
    }

}
