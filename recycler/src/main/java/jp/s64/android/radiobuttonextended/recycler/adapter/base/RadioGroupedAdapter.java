package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import java.util.HashSet;
import java.util.Set;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/14.
 */

public abstract class RadioGroupedAdapter<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> extends RecyclerView.Adapter<VH> {

    private final Helper<VH, T, K> mHelper;

    public RadioGroupedAdapter(Class<? extends VH> viewHolderClass) {
        mHelper = new Helper<>(viewHolderClass);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH ret = onCreateRadioGroupedViewHolder(parent, viewType);
        {
            mHelper.onCreateViewHolder(parent, viewType);
        }
        return ret;
    }

    public abstract VH onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindRadioGroupedViewHolder(holder, position);
        {
            mHelper.onBindViewHolder(holder, position);
        }
    }

    public abstract void onBindRadioGroupedViewHolder(VH holder, int position);

    @CallSuper
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mHelper.onAttachedToRecyclerView(recyclerView);
    }

    @CallSuper
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mHelper.onDetachedFromRecyclerView(recyclerView);
    }

    public void setCheckedId(@Nullable K id) {
        mHelper.setCheckedId(id);
    }
    
    @Nullable
    public K getCheckedId() {
        return mHelper.getCheckedId();
    }

    public static class Helper<VH extends RecyclerView.ViewHolder & IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> {

        private final Class<? extends VH> mViewHolderClass;
        private final Set<RecyclerView> mAttachedRecyclers = new HashSet<>();

        @Nullable
        private K mCheckedId = null;

        public Helper(Class<? extends VH> viewHolderClass) {
            mViewHolderClass = viewHolderClass;
        }

        public void onCreateViewHolder(ViewGroup parent, int viewType) {
            // no-op
        }

        public void onBindViewHolder(VH holder, int position) {
            final T item = holder.getBoundItem();

            {
                holder.setCheckedChangeListener(new IOnCheckedChangeListener() {
                    @Override
                    public <V extends View & Checkable> void onCheckedChange(V checkable, boolean isChecked) {
                        if (isChecked) {
                            Helper.this.setCheckedId(item.getId());
                        }
                    }
                });
            }

            if (mCheckedId == null) {
                holder.setIsChecked(false);
            } else if (item != null) {
                holder.setIsChecked(item.getId().compareTo(mCheckedId) == 0);
            }
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            mAttachedRecyclers.add(recyclerView);
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            mAttachedRecyclers.remove(recyclerView);
        }

        public void setCheckedId(@Nullable K id) {
            K oldId = mCheckedId;
            mCheckedId = id;
            for (RecyclerView recycler : mAttachedRecyclers) {
                for (int adapterPosition = 0; adapterPosition < recycler.getAdapter().getItemCount(); adapterPosition++) {
                    RecyclerView.ViewHolder org = recycler.findViewHolderForAdapterPosition(adapterPosition);
                    if (org != null && mViewHolderClass.isAssignableFrom(org.getClass())) {
                        VH holder = (VH) org;
                        T itrItem = holder.getBoundItem();
                        if (itrItem != null) {
                            K itrId = itrItem.getId();
                            if (itrId.compareTo(mCheckedId) == 0 || (oldId != null && itrId.compareTo(oldId) == 0)) {
                                recycler.getAdapter().notifyItemChanged(holder.getAdapterPosition());
                            }
                        }
                    }
                }
            }
        }

        @Nullable
        public K getCheckedId() {
            return mCheckedId;
        }

    }

    public interface IRadioGroupedViewHolder<T extends ICheckableModel<K>, K extends Comparable<K>> {

        @Nullable
        T getBoundItem();

        void setBoundItem(@Nullable T item);

        void setCheckedChangeListener(IOnCheckedChangeListener listener);

        void setIsChecked(boolean isChecked);

    }

}
