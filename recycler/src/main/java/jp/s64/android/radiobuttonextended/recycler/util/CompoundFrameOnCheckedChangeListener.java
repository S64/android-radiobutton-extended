package jp.s64.android.radiobuttonextended.recycler.util;

import android.view.View;
import android.widget.Checkable;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckedChangeListener;

/**
 * Created by shuma on 2017/04/11.
 */

public class CompoundFrameOnCheckedChangeListener implements CompoundFrameLayout.OnCheckedChangeListener, IOnCheckedChangeListener {

    private final IOnCheckedChangeListener mListener;

    public CompoundFrameOnCheckedChangeListener(IOnCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundFrameLayout compoundFrameLayout, boolean checked) {
        onCheckedChange(compoundFrameLayout, checked);
    }

    @Override
    public <V extends View & Checkable> void onCheckedChange(V checkable, boolean isChecked) {
        mListener.onCheckedChange(checkable, isChecked);
    }

}
