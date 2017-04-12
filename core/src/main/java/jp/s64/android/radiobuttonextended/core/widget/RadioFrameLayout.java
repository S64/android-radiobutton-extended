package jp.s64.android.radiobuttonextended.core.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

/**
 * Created by shuma on 2017/04/10.
 */

public class RadioFrameLayout extends CompoundFrameLayout {

    public RadioFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RadioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            super.toggle();
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return RadioFrameLayout.class.getName();
    }

}
