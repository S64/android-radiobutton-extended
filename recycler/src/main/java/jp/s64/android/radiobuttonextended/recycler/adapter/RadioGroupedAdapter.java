package jp.s64.android.radiobuttonextended.recycler.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/14.
 */

public abstract class RadioGroupedAdapter<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> extends RecyclerView.Adapter<VH> {

    private final Helper<VH, T, K> mHelper;

    public RadioGroupedAdapter(Class<? extends VH> viewHolderClass, IOnCheckedChangeListener listener, Helper.IPayloadGenerator<VH, T, K> generator) {
        mHelper = new Helper<>(viewHolderClass, listener, generator);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH ret = onCreateRadioGroupedViewHolder(parent, viewType);
        {
            mHelper.onCreateViewHolder(parent, viewType);
        }
        return ret;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindRadioGroupedViewHolder(holder, position);
        {
            mHelper.onBindViewHolder(holder);
        }
    }

    public abstract VH onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        onBindRadioGroupedViewHolder(holder, position, payloads);
        {
            mHelper.onBindViewHolder(holder);
        }
    }

    public abstract void onBindRadioGroupedViewHolder(VH holder, int position);

    public abstract void onBindRadioGroupedViewHolder(VH holder, int position, List<Object> payloads);

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

    public Helper.IPayloadGenerator<VH, T, K> getPayloadGenerator() {
        return mHelper.getPayloadGenerator();
    }

    public static class Helper<VH extends RecyclerView.ViewHolder & IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> {

        private final Class<? extends VH> mViewHolderClass;
        private final Set<RecyclerView> mAttachedRecyclers = new HashSet<>();
        private final IOnCheckedChangeListener mListener;
        private final IPayloadGenerator<VH, T, K> mPayloadGenerator;

        @Nullable
        private K mCheckedId = null;

        public Helper(Class<? extends VH> viewHolderClass, IOnCheckedChangeListener listener, IPayloadGenerator<VH, T, K> generator) {
            mViewHolderClass = viewHolderClass;
            mListener = listener;
            mPayloadGenerator = generator;
        }

        public void onCreateViewHolder(ViewGroup parent, int viewType) {
            // no-op
        }

        public void onBindViewHolder(VH holder) {
            final T item = holder.getBoundItem();

            {
                holder.setCheckedChangeListener(new IOnCheckedChangeListener() {

                    @Override
                    public void onCheckedChange(RecyclerView.ViewHolder holder, View checkable, boolean isChecked) {
                        mListener.onCheckedChange(holder, checkable, isChecked);
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

            List<IPayloadGenerator.Item<VH, T, K>> oldItems = new LinkedList<>();
            List<IPayloadGenerator.Item<VH, T, K>> newItems = new LinkedList<>();

            for (RecyclerView recycler : mAttachedRecyclers) {
                for (int adapterPosition = 0; adapterPosition < recycler.getAdapter().getItemCount(); adapterPosition++) {
                    RecyclerView.ViewHolder org = recycler.findViewHolderForAdapterPosition(adapterPosition);
                    if (org != null && mViewHolderClass.isAssignableFrom(org.getClass())) {
                        VH holder = (VH) org;
                        T itrItem = holder.getBoundItem();
                        if (itrItem != null) {
                            K itrId = itrItem.getId();
                            boolean isCheck = itrId.compareTo(mCheckedId) == 0;
                            boolean isOld = (oldId != null && itrId.compareTo(oldId) == 0);
                            if (isCheck) {
                                newItems.add(new IPayloadGenerator.Item<>(holder));
                            } else if (isOld) {
                                oldItems.add(new IPayloadGenerator.Item<>(holder));
                            }
                        }
                    }
                }
                {
                    IPayloadGenerator.Item<VH, T, K> primaryOld = oldItems.size() > 0 ? oldItems.get(0) : null;
                    IPayloadGenerator.Item<VH, T, K> primaryNew = newItems.size() > 0 ? newItems.get(0) : null;

                    for (IPayloadGenerator.Item<VH, T, K> item : oldItems) {
                        recycler.getAdapter().notifyItemChanged(
                                item.getViewHolder().getAdapterPosition(),
                                getPayloadGenerator().onUncheck(new IPayloadGenerator.TargetItem<>(
                                        item.getViewHolder(),
                                        primaryNew
                                ))
                        );
                    }

                    for (IPayloadGenerator.Item<VH, T, K> item : newItems) {
                        recycler.getAdapter().notifyItemChanged(
                                item.getViewHolder().getAdapterPosition(),
                                getPayloadGenerator().onCheck(new IPayloadGenerator.TargetItem<>(
                                        item.getViewHolder(),
                                        primaryOld
                                ))
                        );
                    }
                }

            }
        }

        @Nullable
        public K getCheckedId() {
            return mCheckedId;
        }

        public IPayloadGenerator<VH, T, K> getPayloadGenerator() {
            return mPayloadGenerator;
        }

        public interface IPayloadGenerator<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> {

            Object onCheck(TargetItem<VH, T, K> item);

            Object onUncheck(TargetItem<VH, T, K> item);

            class Item<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> {

                private final VH viewHolder;

                public Item(VH viewHolder) {
                    this.viewHolder = viewHolder;
                }

                public VH getViewHolder() {
                    return viewHolder;
                }

            }

            class TargetItem<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<T, K>, T extends ICheckableModel<K>, K extends Comparable<K>> extends Item<VH, T, K> {

                @Nullable
                private final Item<VH, T, K> pair;

                public TargetItem(VH viewHolder, @Nullable Item<VH, T, K> pair) {
                    super(viewHolder);
                    this.pair = pair;
                }

                @Nullable
                public Item<VH, T, K> getPair() {
                    return pair;
                }

            }

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
