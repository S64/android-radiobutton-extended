package jp.s64.android.radiobuttonextended.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by shuma on 2017/04/11.
 */
public interface IOnCheckedChangeListener<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<VH, K>, K> {

    <V extends View & Checkable> void onCheckedChange(VH holder, V checkable, boolean isChecked);

}
