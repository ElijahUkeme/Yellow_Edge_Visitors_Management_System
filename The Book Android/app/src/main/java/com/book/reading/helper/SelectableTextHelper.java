package com.book.reading.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.ColorInt;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.book.reading.R;
import com.book.reading.adapter.ReadingAdapter;



public class SelectableTextHelper {

    private final static int DEFAULT_SELECTION_LENGTH = 7;
    private static final int DEFAULT_SHOW_DURATION = 500;

    private CursorHandle mStartHandle;
    private CursorHandle mEndHandle;
    private OperateWindow mOperateWindow;
    private SelectionInfo mSelectionInfo = new SelectionInfo();
    private OnSelectListener mSelectListener;

    private Context mContext;
    private TextView mTextView;
    private Spannable mSpannable;

    private int mTouchX;
    private int mTouchY;

    private int mSelectedColor;
    private int mCursorHandleColor;
    private int mCursorHandleSize;
    private BackgroundColorSpan mSpan;
    private boolean isHideWhenScroll;
    private boolean isHide = true;
    public static View newView;
    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener;
    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    public SelectableTextHelper(Builder builder) {
        mTextView = builder.mTextView;
        mContext = mTextView.getContext();
        mSelectedColor = builder.mSelectedColor;
        mCursorHandleColor = builder.mCursorHandleColor;
        mCursorHandleSize = TextLayoutUtil.dp2px(mContext, builder.mCursorHandleSizeInDp);
        init();
    }

    private void init() {
        mTextView.setText(mTextView.getText(), TextView.BufferType.SPANNABLE);
        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSelectView(mTouchX, mTouchY);
                return true;
            }
        });

        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                return false;
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectionInfo();
                hideSelectView();
            }
        });
        mTextView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                destroy();
            }
        });

        mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isHideWhenScroll) {
                    isHideWhenScroll = false;
                    postShowSelectView(DEFAULT_SHOW_DURATION);
                }
                return true;
            }
        };
        mTextView.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!isHideWhenScroll && !isHide) {
                    isHideWhenScroll = true;
                    if (mOperateWindow != null) {
                        mOperateWindow.dismiss();
                    }
                    if (mStartHandle != null) {
                        mStartHandle.dismiss();
                    }
                    if (mEndHandle != null) {
                        mEndHandle.dismiss();
                    }
                }
            }
        };
        mTextView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);

        mOperateWindow = new OperateWindow(mContext);
    }

    private void postShowSelectView(int duration) {
        mTextView.removeCallbacks(mShowSelectViewRunnable);
        if (duration <= 0) {
            mShowSelectViewRunnable.run();
        } else {
            mTextView.postDelayed(mShowSelectViewRunnable, duration);
        }
    }

    private final Runnable mShowSelectViewRunnable = new Runnable() {
        @Override
        public void run() {
            if (isHide) return;
            if (mOperateWindow != null) {
                mOperateWindow.show();
            }
            if (mStartHandle != null) {
                showCursorHandle(mStartHandle);
            }
            if (mEndHandle != null) {
                showCursorHandle(mEndHandle);
            }
        }
    };

    private void hideSelectView() {
        isHide = true;
        if (mStartHandle != null) {
            mStartHandle.dismiss();
        }
        if (mEndHandle != null) {
            mEndHandle.dismiss();
        }
        if (mOperateWindow != null) {
            mOperateWindow.dismiss();
        }
    }

    private void resetSelectionInfo() {
        mSelectionInfo.mSelectionContent = null;
        if (mSpannable != null && mSpan != null) {
            mSpannable.removeSpan(mSpan);
            mSpan = null;
        }
    }

    public void showSelectView(int x, int y) {
        hideSelectView();
        resetSelectionInfo();
        isHide = false;
        if (mStartHandle == null) mStartHandle = new CursorHandle(true);
        if (mEndHandle == null) mEndHandle = new CursorHandle(false);

        int startOffset = TextLayoutUtil.getPreciseOffset(mTextView, x, y);
        int endOffset = startOffset + DEFAULT_SELECTION_LENGTH;
        if (mTextView.getText() instanceof Spannable) {
            mSpannable = (Spannable) mTextView.getText();
        }
        if (mSpannable == null || startOffset >= mTextView.getText().length()) {
            return;
        }
        selectText(startOffset, endOffset);
        showCursorHandle(mStartHandle);
        showCursorHandle(mEndHandle);
        mOperateWindow.show();
    }

    private void showCursorHandle(CursorHandle cursorHandle) {
        Layout layout = mTextView.getLayout();
        int offset = cursorHandle.isLeft ? mSelectionInfo.mStart : mSelectionInfo.mEnd;
        cursorHandle.show((int) layout.getPrimaryHorizontal(offset), layout.getLineBottom(layout.getLineForOffset(offset)));
    }

    private void selectText(int startPos, int endPos) {
        if (startPos != -1) {
            mSelectionInfo.mStart = startPos;
        }
        if (endPos != -1) {
            mSelectionInfo.mEnd = endPos;
        }
        if (mSelectionInfo.mStart > mSelectionInfo.mEnd) {
            int temp = mSelectionInfo.mStart;
            mSelectionInfo.mStart = mSelectionInfo.mEnd;
            mSelectionInfo.mEnd = temp;
        }

        if (mSpannable != null) {
            if (mSpan == null) {
                mSpan = new BackgroundColorSpan(mSelectedColor);
            }
            mSelectionInfo.mSelectionContent = mSpannable.subSequence(mSelectionInfo.mStart, mSelectionInfo.mEnd).toString();
            mSpannable.setSpan(mSpan, mSelectionInfo.mStart, mSelectionInfo.mEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (mSelectListener != null) {
                mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent);
            }
        }
    }

    public void setSelectListener(OnSelectListener selectListener) {
        mSelectListener = selectListener;
    }

    public void destroy() {
        mTextView.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        mTextView.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
        resetSelectionInfo();
        hideSelectView();
        mStartHandle = null;
        mEndHandle = null;
        mOperateWindow = null;
    }

    /**
     * Operate windows : copy, select all
     */
    private class OperateWindow {

        private PopupWindow mWindow;
        private int[] mTempCoors = new int[2];

        private int mWidth;
        private int mHeight;

        public OperateWindow(final Context context) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.layout_operate_windows, null);
            contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            mWidth = contentView.getMeasuredWidth();
            mHeight = contentView.getMeasuredHeight();
            mWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            mWindow.setClippingEnabled(false);
            newView = contentView;
            contentView.findViewById(R.id.tv_note).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    if (mSelectListener != null) {
                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent);
                    }*/
                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
             ReadingAdapter.NoteWord(mSelectionInfo.mStart, mSelectionInfo.mEnd);

                }
            });
            contentView.findViewById(R.id.tv_mark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
                    ReadingAdapter.MarkWord(mSelectionInfo.mStart, mSelectionInfo.mEnd);
                }
            });

            contentView.findViewById(R.id.tv_underline).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
                    ReadingAdapter.UnderlineWord(mSelectionInfo.mStart, mSelectionInfo.mEnd);

                }
            });
        }

        public void show() {
            mTextView.getLocationInWindow(mTempCoors);
            Layout layout = mTextView.getLayout();
            int posX = (int) layout.getPrimaryHorizontal(mSelectionInfo.mStart) + mTempCoors[0];
            int posY = layout.getLineTop(layout.getLineForOffset(mSelectionInfo.mStart)) + mTempCoors[1] - mHeight - 16;
            if (posX <= 0) posX = 16;
            if (posY < 0) posY = 16;
            if (posX + mWidth > TextLayoutUtil.getScreenWidth(mContext)) {
                posX = TextLayoutUtil.getScreenWidth(mContext) - mWidth - 16;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWindow.setElevation(8f);
            }
            mWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, posX, posY);
        }

        public void dismiss() {
            mWindow.dismiss();
        }

        public boolean isShowing() {
            return mWindow.isShowing();
        }
    }

    private class CursorHandle extends View {

        private PopupWindow mPopupWindow;
        private Paint mPaint;

        private int mCircleRadius = mCursorHandleSize / 2;
        private int mWidth = mCircleRadius * 2;
        private int mHeight = mCircleRadius * 2;
        private int mPadding = 25;
        private boolean isLeft;

        public CursorHandle(boolean isLeft) {
            super(mContext);
            this.isLeft = isLeft;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(mCursorHandleColor);

            mPopupWindow = new PopupWindow(this);
            mPopupWindow.setClippingEnabled(false);
            mPopupWindow.setWidth(mWidth + mPadding * 2);
            mPopupWindow.setHeight(mHeight + mPadding / 2);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(mCircleRadius + mPadding, mCircleRadius, mCircleRadius, mPaint);
            if (isLeft) {
                canvas.drawRect(mCircleRadius + mPadding, 0, mCircleRadius * 2 + mPadding, mCircleRadius, mPaint);
            } else {
                canvas.drawRect(mPadding, 0, mCircleRadius + mPadding, mCircleRadius, mPaint);
            }
        }

        private int mAdjustX;
        private int mAdjustY;

        private int mBeforeDragStart;
        private int mBeforeDragEnd;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBeforeDragStart = mSelectionInfo.mStart;
                    mBeforeDragEnd = mSelectionInfo.mEnd;
                    mAdjustX = (int) event.getX();
                    mAdjustY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mOperateWindow.show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mOperateWindow.dismiss();
                    final int rawX = (int) event.getRawX();
                    final int rawY = (int) event.getRawY();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            update(rawX + mAdjustX - mWidth, rawY + mAdjustY - mHeight);
                        }
                    }, 300);

                    break;
            }
            return true;
        }

        private void changeDirection() {
            isLeft = !isLeft;
            invalidate();
        }

        public void dismiss() {
            mPopupWindow.dismiss();
        }

        private int[] mTempCoors = new int[2];

        public void update(int x, int y) {
            mTextView.getLocationInWindow(mTempCoors);
            int oldOffset;
            if (isLeft) {
                oldOffset = mSelectionInfo.mStart;
            } else {
                oldOffset = mSelectionInfo.mEnd;
            }

            y -= mTempCoors[1];

            int offset = TextLayoutUtil.getHysteresisOffset(mTextView, x, y, oldOffset);

            if (offset != oldOffset) {
                resetSelectionInfo();
                if (isLeft) {
                    if (offset > mBeforeDragEnd) {
                        CursorHandle handle = getCursorHandle(false);
                        changeDirection();
                        handle.changeDirection();
                        mBeforeDragStart = mBeforeDragEnd;
                        selectText(mBeforeDragEnd, offset);
                        handle.updateCursorHandle();
                    } else {
                        selectText(offset, -1);
                    }
                    updateCursorHandle();
                } else {
                    if (offset < mBeforeDragStart) {
                        CursorHandle handle = getCursorHandle(true);
                        handle.changeDirection();
                        changeDirection();
                        mBeforeDragEnd = mBeforeDragStart;
                        selectText(offset, mBeforeDragStart);
                        handle.updateCursorHandle();
                    } else {
                        selectText(mBeforeDragStart, offset);
                    }
                    updateCursorHandle();
                }
            }
        }

        private void updateCursorHandle() {
            mTextView.getLocationInWindow(mTempCoors);
            final Layout layout = mTextView.getLayout();
            final Handler handler = new Handler();
            if (isLeft) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPopupWindow.update((int) layout.getPrimaryHorizontal(mSelectionInfo.mStart) - mWidth + getExtraX(), layout.getLineBottom(layout.getLineForOffset(mSelectionInfo.mStart)) + getExtraY(), -1, -1);
                    }
                }, 100);

            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPopupWindow.update((int) layout.getPrimaryHorizontal(mSelectionInfo.mEnd) + getExtraX(), layout.getLineBottom(layout.getLineForOffset(mSelectionInfo.mEnd)) + getExtraY(), -1, -1);
                    }
                }, 100);

            }
        }

        public void show(int x, int y) {
            mTextView.getLocationInWindow(mTempCoors);
            int offset = isLeft ? mWidth : 0;
            mPopupWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, x - offset + getExtraX(), y + getExtraY());
        }

        public int getExtraX() {
            return mTempCoors[0] - mPadding + mTextView.getPaddingLeft();
        }

        public int getExtraY() {
            return mTempCoors[1] + mTextView.getPaddingTop();
        }
    }

    private CursorHandle getCursorHandle(boolean isLeft) {
        if (mStartHandle.isLeft == isLeft) {
            return mStartHandle;
        } else {
            return mEndHandle;
        }
    }

    public static class Builder {
        private TextView mTextView;
        private int mCursorHandleColor = 0xFF1379D6;
        private int mSelectedColor = 0xFFAFE1F4;
        private float mCursorHandleSizeInDp = 24;

        public Builder(TextView textView) {
            mTextView = textView;
        }

        public Builder setCursorHandleColor(@ColorInt int cursorHandleColor) {
            mCursorHandleColor = cursorHandleColor;
            return this;
        }

        public Builder setCursorHandleSizeInDp(float cursorHandleSizeInDp) {
            mCursorHandleSizeInDp = cursorHandleSizeInDp;
            return this;
        }

        public Builder setSelectedColor(@ColorInt int selectedBgColor) {
            mSelectedColor = selectedBgColor;
            return this;
        }

        public SelectableTextHelper build() {
            return new SelectableTextHelper(this);
        }
    }
}


