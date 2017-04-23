/*
 * Copyright (C) 2017 Shuma Yoshioka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public abstract class RadioGroupedAdapter<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<VH, K>, K extends Comparable<K>> extends RecyclerView.Adapter<VH> {

    private final Helper<VH, K> mHelper;

    public RadioGroupedAdapter(Class<? extends VH> viewHolderClass, IOnCheckedChangeListener listener, Helper.IPayloadGenerator<VH, K> generator) {
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

    public Helper.IPayloadGenerator<VH, K> getPayloadGenerator() {
        return mHelper.getPayloadGenerator();
    }

    public static class Helper<VH extends RecyclerView.ViewHolder & IRadioGroupedViewHolder<VH, K>, K> {

        private final Class<? extends VH> mViewHolderClass;
        private final Set<RecyclerView> mAttachedRecyclers = new HashSet<>();
        private final IOnCheckedChangeListener mListener;
        private final IPayloadGenerator<VH, K> mPayloadGenerator;

        @Nullable
        private K mCheckedId = null;

        public Helper(Class<? extends VH> viewHolderClass, IOnCheckedChangeListener listener, IPayloadGenerator<VH, K> generator) {
            mViewHolderClass = viewHolderClass;
            mListener = listener;
            mPayloadGenerator = generator;
        }

        public void onCreateViewHolder(ViewGroup parent, int viewType) {
            // no-op
        }

        public void onBindViewHolder(VH holder) {
            final K key = holder.getRadioKey();

            {
                holder.setCheckedChangeListener(new IOnCheckedChangeListener() {

                    @Override
                    public void onCheckedChange(RecyclerView.ViewHolder holder, View checkable, boolean isChecked) {
                        mListener.onCheckedChange(holder, checkable, isChecked);
                        if (isChecked) {
                            Helper.this.setCheckedId(key);
                        }
                    }

                });
            }

            if (mCheckedId == null) {
                holder.setIsChecked(false);
            } else if (key != null) {
                holder.setIsChecked(equals(key, mCheckedId));
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

            List<IPayloadGenerator.Item<VH, K>> oldItems = new LinkedList<>();
            List<IPayloadGenerator.Item<VH, K>> newItems = new LinkedList<>();

            for (RecyclerView recycler : mAttachedRecyclers) {
                for (int adapterPosition = 0; adapterPosition < recycler.getAdapter().getItemCount(); adapterPosition++) {
                    RecyclerView.ViewHolder org = recycler.findViewHolderForAdapterPosition(adapterPosition);
                    if (org != null && mViewHolderClass.isAssignableFrom(org.getClass())) {
                        VH holder = (VH) org;
                        K key = holder.getRadioKey();
                        if (key != null) {
                            boolean isCheck = equals(key, mCheckedId);
                            boolean isOld = equals(key, oldId);
                            if (isCheck) {
                                newItems.add(new IPayloadGenerator.Item<>(holder));
                            } else if (isOld) {
                                oldItems.add(new IPayloadGenerator.Item<>(holder));
                            }
                        }
                    } else if (org == null && !recycler.isComputingLayout()) {
                        recycler.getAdapter().notifyItemChanged(adapterPosition);
                    }
                }
                {
                    IPayloadGenerator.Item<VH, K> primaryOld = oldItems.size() > 0 ? oldItems.get(0) : null;
                    IPayloadGenerator.Item<VH, K> primaryNew = newItems.size() > 0 ? newItems.get(0) : null;

                    for (IPayloadGenerator.Item<VH, K> item : oldItems) {
                        recycler.getAdapter().notifyItemChanged(
                                item.getViewHolder().getAdapterPosition(),
                                getPayloadGenerator().onUncheck(new IPayloadGenerator.TargetItem<>(
                                        item.getViewHolder(),
                                        primaryNew
                                ))
                        );
                    }

                    for (IPayloadGenerator.Item<VH, K> item : newItems) {
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

        public IPayloadGenerator<VH, K> getPayloadGenerator() {
            return mPayloadGenerator;
        }

        public interface IPayloadGenerator<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<VH, K>, K> {

            Object onCheck(TargetItem<VH, K> item);

            Object onUncheck(TargetItem<VH, K> item);

            class Item<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<VH, K>, K> {

                private final VH viewHolder;

                public Item(VH viewHolder) {
                    this.viewHolder = viewHolder;
                }

                public VH getViewHolder() {
                    return viewHolder;
                }

            }

            class TargetItem<VH extends RecyclerView.ViewHolder & RadioGroupedAdapter.IRadioGroupedViewHolder<VH, K>, K> extends Item<VH, K> {

                @Nullable
                private final Item<VH, K> pair;

                public TargetItem(VH viewHolder, @Nullable Item<VH, K> pair) {
                    super(viewHolder);
                    this.pair = pair;
                }

                @Nullable
                public Item<VH, K> getPair() {
                    return pair;
                }

            }

        }

        protected static <K> boolean equals(@Nullable K k1, @Nullable K k2) {
            if (k1 == null) {
                return (k2 == null);
            } else {
                return (k2 != null) && k1.equals(k2);
            }
        }

    }

    public interface IRadioGroupedViewHolder<VH extends RecyclerView.ViewHolder & IRadioGroupedViewHolder<VH, K>, K> {

        @Nullable
        K getRadioKey();

        void setRadioItem(@Nullable K item);

        void setCheckedChangeListener(IOnCheckedChangeListener<VH, K> listener);

        void setIsChecked(boolean isChecked);

    }

}
