package com.yellastro.mytonwallet.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DropDownRecyclerView extends RecyclerView {

    public class CustomAutoScroller {
        private final RecyclerView mRecyclerView;
        private boolean mIsEnabled;

        public CustomAutoScroller(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        public void setEnabled(boolean enabled) {
            mIsEnabled = enabled;
        }

        public void onTouch(RecyclerView recyclerView, MotionEvent event) {
            if (!mIsEnabled) {
                return;
            }

            // Implement scrolling logic here
            // For instance, smoothScrollBy or scrollBy methods
        }
    }

    private boolean mListSelectionHidden;
    private boolean mHijackFocus;
    private boolean mDrawsInPressedState;
    private CustomAutoScroller mScrollHelper;
    private ResolveHoverRunnable mResolveHoverRunnable;
    private int mMotionPosition = NO_POSITION;

    public DropDownRecyclerView(@NonNull Context context, boolean hijackFocus) {
        this(context, hijackFocus, android.R.attr.dropDownListViewStyle);
    }

    public DropDownRecyclerView(@NonNull Context context, boolean hijackFocus, int defStyleAttr) {
        super(context);
        mHijackFocus = hijackFocus;
        setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mResolveHoverRunnable != null) {
            mResolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onHoverEvent(@NonNull MotionEvent ev) {
        final int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_HOVER_EXIT && mResolveHoverRunnable == null) {
            mResolveHoverRunnable = new ResolveHoverRunnable();
            mResolveHoverRunnable.post();
        }
        final boolean handled = super.onHoverEvent(ev);
        if (action == MotionEvent.ACTION_HOVER_ENTER || action == MotionEvent.ACTION_HOVER_MOVE) {
            final View hoveredItem = findChildViewUnder(ev.getX(), ev.getY());
            if (hoveredItem != null && hoveredItem.isEnabled()) {
                requestFocus();
                hoveredItem.setSelected(true);
            }
            updateSelectorState();
        } else {
            clearSelection();
        }
        return handled;
    }

    private void updateSelectorState() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.isSelected()) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        if (mResolveHoverRunnable == null) {
            super.drawableStateChanged();
        }
    }

    public void setListSelectionHidden(boolean hideListSelection) {
        mListSelectionHidden = hideListSelection;
    }

    private void clearPressedItem() {
        mDrawsInPressedState = false;
        setPressed(false);
        updateSelectorState();
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        View motionView = layoutManager.findViewByPosition(mMotionPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
    }

    private void setPressedItem(@NonNull View child, int position, float x, float y) {
        mDrawsInPressedState = true;
        drawableHotspotChanged(x, y);
        if (!isPressed()) {
            setPressed(true);
        }
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        View motionView = layoutManager.findViewByPosition(mMotionPosition);
        if (motionView != null && motionView != child && motionView.isPressed()) {
            motionView.setPressed(false);
        }
        mMotionPosition = position;
        float childX = x - child.getLeft();
        float childY = y - child.getTop();
        child.drawableHotspotChanged(childX, childY);
        if (!child.isPressed()) {
            child.setPressed(true);
        }
        getAdapter().notifyItemChanged(position);
        refreshDrawableState();
    }

    private void clearSelection() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.isSelected()) {
                child.setSelected(false);
            }
        }
    }

    @Override
    public boolean isInTouchMode() {
        return (mHijackFocus && mListSelectionHidden) || super.isInTouchMode();
    }

    @Override
    public boolean hasWindowFocus() {
        return mHijackFocus || super.hasWindowFocus();
    }

    @Override
    public boolean isFocused() {
        return mHijackFocus || super.isFocused();
    }

    @Override
    public boolean hasFocus() {
        return mHijackFocus || super.hasFocus();
    }

    private class ResolveHoverRunnable implements Runnable {
        @Override
        public void run() {
            mResolveHoverRunnable = null;
            drawableStateChanged();
        }

        public void cancel() {
            mResolveHoverRunnable = null;
            removeCallbacks(this);
        }

        public void post() {
            DropDownRecyclerView.this.post(this);
        }
    }
}