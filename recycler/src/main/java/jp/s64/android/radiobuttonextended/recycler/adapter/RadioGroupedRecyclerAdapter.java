package jp.s64.android.radiobuttonextended.recycler.adapter;

import android.support.v7.widget.RecyclerView;

import jp.s64.android.radiobuttonextended.recycler.adapter.base.BaseRadioGroupedRecyclerAdapter;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IRadioButtonBinder;

/**
 * Created by shuma on 2017/04/11.
 */

public abstract class RadioGroupedRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends BaseRadioGroupedRecyclerAdapter<Helper.CheckedStateHolder<T>, VH, Helper<T>, Helper.AbsLayoutItemResolver<T>> {

    public RadioGroupedRecyclerAdapter(IRadioButtonBinder binder, Helper.AbsLayoutItemResolver<T> resolver) {
        super(new Helper<T>());
        {
            getHelper().setAdapter(this);
            getHelper().setRadioButtonBinder(binder);
            getHelper().setLayoutItemResolver(resolver);
        }
    }

    @Override
    public int getItemCount() {
        return getHelper().getItemCount();
    }

    public void add(T item) {
        getHelper().add(item);
    }

    public T get(int position) {
        return getHelper().get(position);
    }

}
