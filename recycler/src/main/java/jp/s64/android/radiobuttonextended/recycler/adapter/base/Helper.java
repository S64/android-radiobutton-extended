package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;

import java.util.List;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/11.
 */
public class Helper<T extends ICheckableModel, LR extends ILayoutItemResolver<T>> {

    public static final int POSITION_NOT_CHECKED = -1;

    private IRadioButtonBinder mBinder;
    private IItemResolver<T> mResolver;
    private LR mLayoutResolver;
    private IItemNotifier<T> mNotifier;

    private boolean mBroadcasting;

    public Helper() {

    }

    public Helper(IRadioButtonBinder binder, IItemResolver<T> resolver, IItemNotifier<T> notifier, LR layoutResolver) {
        mBinder = binder;
        mResolver = resolver;
        mLayoutResolver = layoutResolver;
        mNotifier = notifier;
    }

    public void setRadioButtonBinder(IRadioButtonBinder binder) {
        mBinder = binder;
    }

    public void setItemResolver(IItemResolver<T> resolver) {
        mResolver = resolver;
    }

    public void setLayoutItemResolver(LR resolver) {
        mLayoutResolver = resolver;
    }

    public void setItemNotifier(IItemNotifier<T> notifier) {
        mNotifier = notifier;
    }

    public <VH extends RecyclerView.ViewHolder> void onCreateViewHolder(final VH holder) {
        mBinder.setOnCheckListenerIfAvailable(
                holder,
                new IOnCheckChangedListener() {

                    @Override
                    public <V extends View & Checkable> void onCheckChanged(V checkable, boolean isChecked) {
                        T item = mLayoutResolver.resolveItemByLayoutPosition(holder.getAdapterPosition());
                        if (isChecked) {
                            setChecked(item);
                        }
                    }

                }
        );
    }

    public void setChecked(@Nullable T item) {
        List<T> oldItems = mResolver.resolveCheckedItems();

        for (T oldItem : oldItems) {
            boolean oldCheck = oldItem.isChecked();
            if (oldCheck && oldItem != item) {
                oldItem.setChecked(false);
                if (!mBroadcasting) mNotifier.notifyItemChanged(oldItem);
            }
        }

        if (item != null) {
            boolean oldCheck = item.isChecked();
            if (!oldCheck) {
                item.setChecked(true);
                if (!mBroadcasting) mNotifier.notifyItemChanged(item);
            }
        }
    }

    public void setCheckedPosition(int position) {
        T item = position != POSITION_NOT_CHECKED ? mResolver.resolveItemByItemsPosition(position) : null;
        setChecked(item);
    }

    @Nullable
    public T getChecked() {
        List<T> items = mResolver.resolveCheckedItems();
        if (items.size() > 0) {
            return items.get(0);
        }
        return null;
    }

    public int getCheckedItemsPosition() {
        T item = getChecked();
        if (item != null) {
            return mResolver.resolveItemsPosition(item);
        }
        return POSITION_NOT_CHECKED;
    }

    public <VH extends RecyclerView.ViewHolder> void onBindViewHolder(VH holder, int position) {
        {
            mBroadcasting = false;
        }
        try {
            T item = mResolver.resolveItemByItemsPosition(position);
            Checkable v = mBinder.getCheckable(holder);
            if (v != null && v.isChecked() != item.isChecked()) {
                v.setChecked(item.isChecked());
            }
        } finally {
            mBroadcasting = false;
        }
    }

}
