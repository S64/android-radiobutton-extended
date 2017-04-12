package jp.s64.android.radiobuttonextended.recycler.util;

import android.view.View;
import android.widget.Checkable;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckChangedListener;

/**
 * Created by shuma on 2017/04/11.
 */

public class CompoundFrameOnCheckedChangeListener implements CompoundFrameLayout.OnCheckedChangeListener, IOnCheckChangedListener {

    private final IOnCheckChangedListener mListener;

    public CompoundFrameOnCheckedChangeListener(IOnCheckChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundFrameLayout compoundFrameLayout, boolean checked) {
        onCheckChanged(compoundFrameLayout, checked);
    }

    @Override
    public <V extends View & Checkable> void onCheckChanged(V checkable, boolean isChecked) {
        mListener.onCheckChanged(checkable, isChecked);
    }

}
