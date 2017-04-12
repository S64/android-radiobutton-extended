package jp.s64.android.radiobuttonextended.recycler.util;

import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;

import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckChangedListener;

/**
 * Created by shuma on 2017/04/11.
 */

public class CompoundOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener, IOnCheckChangedListener {

    private final IOnCheckChangedListener mListener;

    public CompoundOnCheckedChangeListener(IOnCheckChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        onCheckChanged(compoundButton, b);
    }

    @Override
    public <V extends View & Checkable> void onCheckChanged(V checkable, boolean isChecked) {
        mListener.onCheckChanged(checkable, isChecked);
    }

}
