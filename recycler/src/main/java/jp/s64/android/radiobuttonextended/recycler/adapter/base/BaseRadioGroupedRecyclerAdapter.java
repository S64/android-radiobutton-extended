package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/10.
 */

public abstract class BaseRadioGroupedRecyclerAdapter<T extends ICheckableModel, VH extends RecyclerView.ViewHolder, H extends Helper<T, LR>, LR extends ILayoutItemResolver<T>> extends RecyclerView.Adapter<VH> {

    private final H mHelper;

    protected BaseRadioGroupedRecyclerAdapter(H helper) {
        mHelper = helper;
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH ret = onCreateRadioGroupedViewHolder(parent, viewType);
        {
            getHelper().onCreateViewHolder(ret);
        }
        return ret;
    }

    @CallSuper
    @Override
    public void onBindViewHolder(VH holder, int position) {
        getHelper().onBindViewHolder(holder, position);
    }

    public abstract VH onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType);

    public H getHelper() {
        return mHelper;
    }

    public T getChecked() {
        return mHelper.getChecked();
    }

    public int getCheckedItemsPosition() {
        return mHelper.getCheckedItemsPosition();
    }

    public void setCheckedPosition(int position) {
        mHelper.setCheckedPosition(position);
    }

}
