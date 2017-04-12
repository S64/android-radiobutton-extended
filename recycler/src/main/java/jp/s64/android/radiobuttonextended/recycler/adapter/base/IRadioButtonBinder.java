package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by shuma on 2017/04/11.
 */
public interface IRadioButtonBinder {

    @Nullable
    <V extends View & Checkable> V getCheckable(RecyclerView.ViewHolder holder);

    boolean setOnCheckListenerIfAvailable(RecyclerView.ViewHolder holder, IOnCheckChangedListener listener);

}
