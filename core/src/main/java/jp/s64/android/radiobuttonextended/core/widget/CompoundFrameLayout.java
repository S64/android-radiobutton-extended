/*
 * Copyright (C) 2017 Shuma Yoshioka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.s64.android.radiobuttonextended.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.widget.Checkable;
import android.widget.FrameLayout;

import jp.s64.android.radiobuttonextended.core.R;

public class CompoundFrameLayout extends FrameLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked,
            //
    };

    private boolean mIsInitialized = false;

    private boolean mIsChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    private boolean mBroadcasting;

    public CompoundFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public CompoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CompoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CompoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected boolean init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        if (mIsInitialized) return false;

        final TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CompoundFrameLayout,
                defStyleAttr,
                defStyleRes
        );

        try {
            setChecked(a.getBoolean(R.styleable.CompoundFrameLayout_checked, false));
            setClickable(a.getBoolean(R.styleable.CompoundFrameLayout_clickable, true));
        } finally {
            a.recycle();
        }

        return mIsInitialized = true;
    }

    @Override
    public void setChecked(boolean isChecked) {
        if (mIsChecked != isChecked) {
            mIsChecked = isChecked;

            refreshDrawableState();

            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mIsChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mIsChecked);
            }

            mBroadcasting = false;
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + CHECKED_STATE_SET.length);

        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }

        return drawableState;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return CompoundFrameLayout.class.getName();
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(CompoundFrameLayout compoundFrameLayout, boolean checked);

    }

}
