package jp.s64.android.radiobuttonextended.recycler.util;

import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;

import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckedChangeListener;

/**
 * Created by shuma on 2017/04/11.
 */

public class CompoundOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener, IOnCheckedChangeListener {

    private final IOnCheckedChangeListener mListener;

    public CompoundOnCheckedChangeListener(IOnCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        onCheckedChange(compoundButton, b);
    }

    @Override
    public <V extends View & Checkable> void onCheckedChange(V checkable, boolean isChecked) {
        mListener.onCheckedChange(checkable, isChecked);
    }

}
