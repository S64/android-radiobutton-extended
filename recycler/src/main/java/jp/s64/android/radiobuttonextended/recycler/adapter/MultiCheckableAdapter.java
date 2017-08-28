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
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class MultiCheckableAdapter<VH extends RecyclerView.ViewHolder & MultiCheckableAdapter.IMultiCheckableViewHolder<VH, K>, K> extends RecyclerView.Adapter<VH> {

    private final Helper<VH, K> mHelper;

    public MultiCheckableAdapter(Class<? extends VH> viewHolderClass, IOnMultipleCheckedChangeListener<VH, K> listener, IPayloadGenerator generator) {
        mHelper = new Helper<>(viewHolderClass, listener, generator);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH ret = onCreateMultiCheckableViewHolder(parent, viewType);
        {
            mHelper.onCreateViewHolder(parent, viewType);
        }
        return ret;
    }

    public abstract VH onCreateMultiCheckableViewHolder(ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindMultiCheckableViewHolder(holder, position);
        {
            mHelper.onBindViewHolder(holder, position);
        }
    }

    public abstract void onBindMultiCheckableViewHolder(VH holder, int position);

    @CallSuper
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mHelper.onAttachedToRecyclerView(recyclerView);
    }

    public <V extends View & Checkable> boolean updateCheckedIds(K key, boolean isChecked) {
        return mHelper.updateCheckedIds(key, isChecked).first;
    }

    public ImmutableSet<K> getCheckedIds() {
        return mHelper.getCheckedIds();
    }

    public IPayloadGenerator<VH, K> getPayloadGenerator() {
        return mHelper.getPayloadGenerator();
    }


    public static class Helper<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> {

        private final Class<? extends VH> mViewHolderClass;
        private final Set<RecyclerView> mAttachedRecyclers = new HashSet<>();
        private final IOnMultipleCheckedChangeListener<VH, K> mListener;
        private final Set<K> mCheckedIds = new HashSet<>();
        private final IPayloadGenerator<VH, K> mPayloadGenerator;

        public Helper(Class<? extends VH> viewHolderClass, IOnMultipleCheckedChangeListener<VH, K> listener, IPayloadGenerator<VH, K> generator) {
            mViewHolderClass = viewHolderClass;
            mListener = listener;
            mPayloadGenerator = generator;
        }

        public void onCreateViewHolder(ViewGroup parent, int viewType) {
            // no-op
        }

        public void onBindViewHolder(VH holder, int position) {
            final K key = holder.getCheckableKey();

            holder.setCheckedChangeListener(new IOnCheckedChangeListener<VH, K>() {

                @Override
                public <V extends View & Checkable> void onCheckedChange(VH holder, V checkable, boolean isChecked) {
                    Pair<Boolean, ImmutableSet<K>> ret = Helper.this.updateCheckedIds(key, isChecked);
                    if (ret.first) {
                        mListener.onCheckedChange(holder, checkable, ret.second, ImmutableSet.copyOf(mCheckedIds));
                    }
                }

            });


            holder.setIsChecked(key != null && mCheckedIds.contains(key));
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            mAttachedRecyclers.add(recyclerView);
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            mAttachedRecyclers.remove(recyclerView);
        }

        public <V extends View & Checkable> Pair<Boolean, ImmutableSet<K>> updateCheckedIds(K key, boolean isChecked) {
            ImmutableSet<K> oldCheckedIds = ImmutableSet.copyOf(mCheckedIds);
            boolean updated;

            if (key == null) {
                updated = false;
            } else if (isChecked && !mCheckedIds.contains(key)) {
                updated = true;
                mCheckedIds.add(key);
            } else if (!isChecked && mCheckedIds.contains(key)) {
                updated = true;
                mCheckedIds.remove(key);
            } else {
                updated = false;
            }

            if (updated) {
                List<IPayloadGenerator.Item<VH, K>> oldItems = new LinkedList<>();
                List<IPayloadGenerator.Item<VH, K>> newItems = new LinkedList<>();

                for (RecyclerView recycler : mAttachedRecyclers) {
                    for (int adapterPosition = 0; adapterPosition < recycler.getAdapter().getItemCount(); adapterPosition++) {
                        RecyclerView.ViewHolder org = recycler.findViewHolderForAdapterPosition(adapterPosition);
                        if (org != null && mViewHolderClass.isAssignableFrom(org.getClass())) {
                            VH holder = (VH) org;
                            K k = holder.getCheckableKey();
                            if (k != null) {
                                boolean isCheck = mCheckedIds.contains(k);
                                boolean isOld = oldCheckedIds.contains(k);
                                if (isCheck && !isOld) {
                                    newItems.add(new IPayloadGenerator.Item<>(holder));
                                } else if (!isCheck && isOld) {
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

            return new Pair<>(updated, oldCheckedIds);
        }

        public IPayloadGenerator<VH, K> getPayloadGenerator() {
            return mPayloadGenerator;
        }

        public ImmutableSet<K> getCheckedIds() {
            return ImmutableSet.copyOf(mCheckedIds);
        }

    }

    public interface IMultiCheckableViewHolder<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> {

        @Nullable
        K getCheckableKey();

        void setCheckableItem(@Nullable K item);

        void setCheckedChangeListener(IOnCheckedChangeListener<VH, K> listener);

        void setIsChecked(boolean isChecked);

    }

    public interface IOnCheckedChangeListener<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> {

        <V extends View & Checkable> void onCheckedChange(VH holder, V checkable, boolean isChecked);

    }

    public interface IOnMultipleCheckedChangeListener<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> {

        <V extends View & Checkable> void onCheckedChange(VH holder, V checkable, ImmutableSet<K> oldCheckedIds, ImmutableSet<K> newCheckedIds);

    }

    public interface IPayloadGenerator<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> {

        Object onCheck(TargetItem<VH, K> item);

        Object onUncheck(TargetItem<VH, K> item);

        class Item<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> {

            private final VH viewHolder;

            public Item(VH viewHolder) {
                this.viewHolder = viewHolder;
            }

            public VH getViewHolder() {
                return viewHolder;
            }

        }

        class TargetItem<VH extends RecyclerView.ViewHolder & IMultiCheckableViewHolder<VH, K>, K> extends Item<VH, K> {

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

}
