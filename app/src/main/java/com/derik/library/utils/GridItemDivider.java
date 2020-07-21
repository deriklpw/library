package com.derik.library.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class GridItemDivider extends RecyclerView.ItemDecoration {
    private static final String TAG = "GridItemDivider";

    private int mDividerWidth;

    private int mTopWidth;

    private int mBottomWidth;

    private boolean isTopEnable = true;

    private boolean isBottomEnable = true;

    private boolean isLeftEnable = true;

    private boolean isRightEnable = true;

    private Paint mPaint;

    private RecyclerView.LayoutManager mLayoutManager;

    private int mChildCount;

    private boolean mBgMode;

    public GridItemDivider(int dividerWidth, int color) {
        this(dividerWidth, color, false);
    }

    public GridItemDivider(int dividerWidth, int color, boolean bgMode) {
        this.mPaint = new Paint();
        this.mPaint.setColor(color);
        this.mBgMode = bgMode;
        this.setDividerWidth(dividerWidth);
    }

    public void setDividerWidth(int dividerWidth) {
        this.mDividerWidth = dividerWidth;
        //默认顶部，底部，和通用分割线宽度一致
        this.mTopWidth = dividerWidth;
        this.mBottomWidth = dividerWidth;
    }

    public void setDividersEnable(boolean left, boolean top, boolean right, boolean bottom) {
        this.isLeftEnable = left;
        this.isTopEnable = top;
        this.isRightEnable = right;
        this.isBottomEnable = bottom;
    }

    public void setTopBottomDividers(int topWidth, int bottomWidth) {
        this.mTopWidth = topWidth;
        this.mBottomWidth = bottomWidth;
    }

    public void setTopEnable(boolean enable) {
        this.isTopEnable = enable;
    }

    public void setBottomEnable(boolean enable) {
        this.isBottomEnable = enable;
    }

    public void setLeftEnable(boolean enable) {
        this.isLeftEnable = enable;
    }

    public void setRightEnable(boolean enable) {
        this.isRightEnable = enable;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mLayoutManager == null) {
            mLayoutManager = parent.getLayoutManager();
        }

        if (parent.getAdapter() == null) {
            return;
        }

        int itemPosition = parent.getChildLayoutPosition(view);
        mChildCount = parent.getAdapter().getItemCount();

        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
            if (gridLayoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
                setGridVerticalRect(outRect, itemPosition, mChildCount, gridLayoutManager.getSpanCount());
            } else {
                setGridHorizontalRect(outRect, itemPosition, mChildCount, gridLayoutManager.getSpanCount());
            }
        }

    }

    private void setGridVerticalRect(@NonNull Rect outRect, int itemPosition, int childCount, int spanCount) {
        int left;
        int top = 0;
        int right;
        int bottom = mDividerWidth;

        if (isLeftEnable && isRightEnable) {
            int dl = mDividerWidth / spanCount;
            int eachWidth = mDividerWidth + dl;

            left = mDividerWidth - itemPosition % spanCount * dl;
            right = eachWidth - left;

        } else if (isLeftEnable) {
            right = 0;
            left = mDividerWidth;

        } else if (isRightEnable) {
            left = 0;
            right = mDividerWidth;

        } else {
            int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
            int dl = mDividerWidth - eachWidth;
            left = itemPosition % spanCount * dl;
            right = eachWidth - left;
        }

        //水平方向
        if (isFirstRow(itemPosition)) {
            if (isTopEnable) {
                top = mTopWidth;
            }
        }

        if (isLastRow(itemPosition, childCount)) {
            if (isBottomEnable) {
                bottom = mBottomWidth;
            } else {
                bottom = 0;
            }
        }

        outRect.set(left, top, right, bottom);
    }

    private void setGridHorizontalRect(@NonNull Rect outRect, int itemPosition, int childCount, int spanCount) {
        int left = 0;
        int top;
        int right = mDividerWidth;
        int bottom;

        if (isTopEnable && isBottomEnable) {
            int dl = mDividerWidth / spanCount;
            int eachWidth = mDividerWidth + dl;

            top = mDividerWidth - itemPosition % spanCount * dl;
            bottom = eachWidth - top;

        } else if (isTopEnable) {
            bottom = 0;
            top = mDividerWidth;

        } else if (isBottomEnable) {
            top = 0;
            bottom = mDividerWidth;

        } else {
            int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
            int dl = mDividerWidth - eachWidth;
            top = itemPosition % spanCount * dl;
            bottom = eachWidth - top;
        }

        if (isFirstRow(itemPosition)) {
            if (isLeftEnable) {
                left = mTopWidth;
            }
        }

        if (isLastRow(itemPosition, childCount)) {
            if (isRightEnable) {
                right = mBottomWidth;
            } else {
                right = 0;
            }
        }

        outRect.set(left, top, right, bottom);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        Log.d(TAG, "onDraw: ");
        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
            if (gridLayoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
                if (mBgMode) {
                    drawGridVerticalBgMode(canvas, parent, gridLayoutManager.getSpanCount(), mChildCount);
                } else {
                    drawGridVertical(canvas, parent, gridLayoutManager.getSpanCount(), mChildCount);
                }
            } else {
                if (mBgMode) {
                    drawGridHorizontalBgMode(canvas, parent, gridLayoutManager.getSpanCount(), mChildCount);
                } else {
                    drawGridHorizontal(canvas, parent, gridLayoutManager.getSpanCount(), mChildCount);
                }
            }
        }
    }

    private void drawGridVertical(Canvas canvas, RecyclerView parent, int spanCount, int allChild) {
        int drawChildCount = parent.getChildCount();
        for (int i = 0; i < drawChildCount; i++) {
            View view = parent.getChildAt(i);
            int itemPosition = parent.getChildLayoutPosition(view);

            int left;
            int top = 0;
            int right;
            int bottom = mDividerWidth;

            if (isLeftEnable && isRightEnable) {
                int dl = mDividerWidth / spanCount;
                int eachWidth = mDividerWidth + dl;

                left = mDividerWidth - itemPosition % spanCount * dl;
                right = eachWidth - left;

            } else if (isLeftEnable) {
                right = 0;
                left = mDividerWidth;

            } else if (isRightEnable) {
                left = 0;
                right = mDividerWidth;

            } else {
                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
                int dl = mDividerWidth - eachWidth;
                left = itemPosition % spanCount * dl;
                right = eachWidth - left;
            }

            //水平方向
            if (isFirstRow(itemPosition)) {
                if (isTopEnable) {
                    top = mTopWidth;
                    //画水平顶部
                    canvas.drawRect(view.getLeft() - left, view.getTop() - top, view.getRight() + right, view.getTop(), mPaint);
                }
            }

            if (isLastRow(itemPosition, allChild)) {
                if ((itemPosition + 1) % spanCount != 0) {
                    if (mChildCount % spanCount == (itemPosition + 1) % spanCount) {
                        right = mDividerWidth;
                    }
                }
                if (isBottomEnable) {
                    bottom = mBottomWidth;
                } else {
                    bottom = 0;
                }
            }
            //画水平底部
            canvas.drawRect(view.getLeft() - left, view.getBottom(), view.getRight() + right, view.getBottom() + bottom, mPaint);

            //画竖直左边
            canvas.drawRect(view.getLeft() - left, view.getTop() - top, view.getLeft(), view.getBottom() + bottom, mPaint);

            //画竖直右边
            canvas.drawRect(view.getRight(), view.getTop() - top, view.getRight() + right, view.getBottom() + bottom, mPaint);
        }
    }

    private void drawGridVerticalBgMode(Canvas canvas, RecyclerView parent, int spanCount, int allChild) {
        int drawChildCount = parent.getChildCount();
        for (int i = 0; i < drawChildCount; i++) {
            View view = parent.getChildAt(i);
            int itemPosition = parent.getChildLayoutPosition(view);

            int left;
            int top = 0;
            int right;
            int bottom = mDividerWidth;

            if (isLeftEnable && isRightEnable) {
                int dl = mDividerWidth / spanCount;
                int eachWidth = mDividerWidth + dl;

                left = mDividerWidth - itemPosition % spanCount * dl;
                right = eachWidth - left;

            } else if (isLeftEnable) {
                right = 0;
                left = mDividerWidth;

            } else if (isRightEnable) {
                left = 0;
                right = mDividerWidth;

            } else {
                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
                int dl = mDividerWidth - eachWidth;
                left = itemPosition % spanCount * dl;
                right = eachWidth - left;
            }

            //水平方向
            if (isFirstRow(itemPosition)) {
                if (isTopEnable) {
                    top = mTopWidth;
                }
            }

            if (isLastRow(itemPosition, allChild)) {
                if ((itemPosition + 1) % spanCount != 0) {
                    if (mChildCount % spanCount == (itemPosition + 1) % spanCount) {
                        right = mDividerWidth;
                    }
                }
                if (isBottomEnable) {
                    bottom = mBottomWidth;
                } else {
                    bottom = 0;
                }
            }
            canvas.drawRect(view.getLeft() - left, view.getTop() - top, view.getRight() + right, view.getBottom() + bottom, mPaint);
        }
    }

    private void drawGridHorizontal(Canvas canvas, RecyclerView parent, int spanCount, int allChild) {
        int drawChildCount = parent.getChildCount();
        for (int i = 0; i < drawChildCount; i++) {
            View view = parent.getChildAt(i);
            int itemPosition = parent.getChildLayoutPosition(view);

            int left = 0;
            int top;
            int right = mDividerWidth;
            int bottom;

            if (isTopEnable && isBottomEnable) {
                int dl = mDividerWidth / spanCount;
                int eachWidth = mDividerWidth + dl;

                top = mDividerWidth - itemPosition % spanCount * dl;
                bottom = eachWidth - top;

            } else if (isTopEnable) {
                bottom = 0;
                top = mDividerWidth;

            } else if (isBottomEnable) {
                top = 0;
                bottom = mDividerWidth;

            } else {
                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
                int dl = mDividerWidth - eachWidth;
                top = itemPosition % spanCount * dl;
                bottom = eachWidth - top;
            }

            if (isFirstRow(itemPosition)) {
                if (isLeftEnable) {
                    left = mTopWidth;
                    //画竖直最左边
                    canvas.drawRect(view.getLeft() - left, view.getTop() - top, view.getLeft(), view.getBottom() + bottom, mPaint);
                }
            }

            if (isLastRow(itemPosition, allChild)) {
                if ((itemPosition + 1) % spanCount != 0) {
                    if (mChildCount % spanCount == (itemPosition + 1) % spanCount) {
                        bottom = mDividerWidth;
                    }
                }
                if (isRightEnable) {
                    right = mBottomWidth;
                } else {
                    right = 0;
                }
            }
            //画竖直右边和最右边
            canvas.drawRect(view.getRight(), view.getTop() - top, view.getRight() + right, view.getBottom() + bottom, mPaint);
            //画水平顶部
            canvas.drawRect(view.getLeft(), view.getTop() - top, view.getRight(), view.getTop(), mPaint);
            //画水平底部
            canvas.drawRect(view.getLeft(), view.getBottom(), view.getRight(), view.getBottom() + bottom, mPaint);
        }
    }

    private void drawGridHorizontalBgMode(Canvas canvas, RecyclerView parent, int spanCount, int allChild) {
        int drawChildCount = parent.getChildCount();
        for (int i = 0; i < drawChildCount; i++) {
            View view = parent.getChildAt(i);
            int itemPosition = parent.getChildLayoutPosition(view);

            int left = 0;
            int top;
            int right = mDividerWidth;
            int bottom;

            if (isTopEnable && isBottomEnable) {
                int dl = mDividerWidth / spanCount;
                int eachWidth = mDividerWidth + dl;

                top = mDividerWidth - itemPosition % spanCount * dl;
                bottom = eachWidth - top;

            } else if (isTopEnable) {
                bottom = 0;
                top = mDividerWidth;

            } else if (isBottomEnable) {
                top = 0;
                bottom = mDividerWidth;

            } else {
                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;
                int dl = mDividerWidth - eachWidth;
                top = itemPosition % spanCount * dl;
                bottom = eachWidth - top;
            }

            //水平方向
            if (isFirstRow(itemPosition)) {
                if (isLeftEnable) {
                    left = mTopWidth;
                }
            }

            if (isLastRow(itemPosition, allChild)) {
                if ((itemPosition + 1) % spanCount != 0) {
                    if (mChildCount % spanCount == (itemPosition + 1) % spanCount) {
                        bottom = mDividerWidth;
                    }
                }
                if (isRightEnable) {
                    right = mBottomWidth;
                } else {
                    right = 0;
                }
            }
            canvas.drawRect(view.getLeft() - left, view.getTop() - top, view.getRight() + right, view.getBottom() + bottom, mPaint);
        }
    }

    private boolean isFirstRow(int itemPosition) {
        boolean result = false;
        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
            int spanCount = gridLayoutManager.getSpanCount();

            result = (itemPosition / spanCount + 1) == 1;
        }
        return result;
    }

    private boolean isLastRow(int itemPosition, int childCount) {
        boolean result = false;
        if (mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
            int spanCount = gridLayoutManager.getSpanCount();

            int lines = childCount % spanCount == 0 ? childCount / spanCount : (childCount / spanCount) + 1;
            result = lines == (itemPosition / spanCount) + 1;
        }
        return result;
    }

}
