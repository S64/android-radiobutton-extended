package jp.s64.android.radiobuttonextended.recycler.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.s64.android.radiobuttonextended.recycler.adapter.base.IItemNotifier;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IItemResolver;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.ILayoutItemResolver;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IRadioButtonBinder;
import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/11.
 */
public class Helper<T> extends jp.s64.android.radiobuttonextended.recycler.adapter.base.Helper<Helper.CheckedStateHolder<T>, Helper.AbsLayoutItemResolver<T>> implements
        IItemResolver<Helper.CheckedStateHolder<T>>,
        IItemNotifier<Helper.CheckedStateHolder<T>> {

    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mAdapter;
    private final List<CheckedStateHolder<T>> mItems = new ArrayList<>();

    public Helper() {
        super();
        {
            setItemNotifier(this);
            setItemResolver(this);
        }
    }

    public Helper(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, IRadioButtonBinder binder, AbsLayoutItemResolver resolver) {
        super();
        {
            setAdapter(adapter);
            setRadioButtonBinder(binder);
            setItemNotifier(this);
            setItemResolver(this);
            setLayoutItemResolver(resolver);
        }
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
    }

    @Override
    public void setLayoutItemResolver(AbsLayoutItemResolver<T> resolver) {
        super.setLayoutItemResolver(resolver);
        {
            resolver.setHelper(this);
        }
    }

    public void add(T item) {
        mItems.add(new CheckedStateHolder<>(item));
    }

    public T get(int position) {
        return mItems.get(position).mItem;
    }

    public int getItemCount() {
        return mItems.size();
    }

    @Nullable
    protected CheckedStateHolder<T> resolveHolderItemByRawItem(T item) {
        for (CheckedStateHolder<T> holder : mItems) {
            if (holder.mItem == item) return holder;
        }
        return null;
    }

    @Nullable
    @Override
    public CheckedStateHolder<T> resolveItemByItemsPosition(int itemsPosition) {
        return mItems.get(itemsPosition);
    }

    @Override
    public List<CheckedStateHolder<T>> resolveCheckedItems() {
        List<CheckedStateHolder<T>> ret = new LinkedList<>();
        for (CheckedStateHolder<T> item : mItems) {
            if (item.isChecked()) ret.add(item);
        }
        return ret;
    }

    @Override
    public int resolveItemsPosition(CheckedStateHolder<T> item) {
        int position = mItems.indexOf(item);
        return position >= 0 ? position : POSITION_NOT_CHECKED;
    }

    @Override
    public void notifyItemChanged(CheckedStateHolder<T> item) {
        mAdapter.notifyItemChanged(resolveItemsPosition(item));
    }

    public static class CheckedStateHolder<T> implements ICheckableModel {

        public final T mItem;
        private boolean mIsChecked = false;

        public CheckedStateHolder(T item) {
            mItem = item;
        }

        @Override
        public void setChecked(boolean isChecked) {
            mIsChecked = isChecked;
        }

        @Override
        public boolean isChecked() {
            return mIsChecked;
        }

    }

    public static abstract class AbsLayoutItemResolver<T> implements ILayoutItemResolver<Helper.CheckedStateHolder<T>> {

        private Helper<T> mHelper;

        @Nullable
        public abstract T resolveRawItemByLayoutPosition(int layoutPosition);

        protected void setHelper(Helper<T> helper) {
            mHelper = helper;
        }

        @Nullable
        @Override
        public CheckedStateHolder<T> resolveItemByLayoutPosition(int layoutPosition) {
            T rawItem = resolveRawItemByLayoutPosition(layoutPosition);
            return mHelper.resolveHolderItemByRawItem(rawItem);
        }

    }

}
