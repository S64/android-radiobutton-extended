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

package jp.s64.android.radiobuttonextended.example.second;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedAdapter;

public class SecondAdapter extends RadioGroupedAdapter<SecondViewHolder, Long> {

    private final Set<RecyclerView> mAttachedRecyclers = new HashSet<>();

    private final List<SecondModel> mItems = new ArrayList<>();

    public SecondAdapter(IOnCheckedChangeListener listener) {
        super(SecondViewHolder.class, listener, new PayloadGenerator());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclers.add(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclers.remove(recyclerView);
    }

    @Override
    public SecondViewHolder onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second, parent, false);
        return new SecondViewHolder(view);
    }

    @Override
    public void onBindRadioGroupedViewHolder(SecondViewHolder holder, int position) {
        setDefaults(holder, position);
    }

    @Override
    public void onBindRadioGroupedViewHolder(SecondViewHolder holder, int position, List<Object> payloads) {
        {
            setDefaults(holder, position);
        }
        {
            MyPayload payload = null;

            for (Object itrPayload : payloads) {
                if (itrPayload != null && itrPayload instanceof MyPayload) {
                    payload = (MyPayload) itrPayload;
                    break;
                }
            }

            if (payload != null) {
                ViewPropertyAnimatorCompat animator = ViewCompat
                        .animate(holder.getIcon())
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setDuration(500);

                int diff = payload.calcDiffY();

                if (diff == 0) {
                    ViewCompat.setTranslationX(holder.getIcon(), holder.getIcon().getMeasuredWidth() * -1);
                    animator = animator
                            .translationX(0);
                } else {
                    ViewCompat.setTranslationY(holder.getIcon(), diff);
                    animator = animator
                            .translationY(0);
                }

                animator.start();
            }
        }
    }

    protected void setDefaults(SecondViewHolder holder, int position) {
        SecondModel item = mItems.get(position);
        {
            holder.setRadioItem(item.getId());
            holder.getLabel().setText(generateLabel(position, holder.getUuid()));
        }
        int visibility = getCheckedId() == item.getId() ? View.VISIBLE : View.INVISIBLE;
        {
            holder.getIcon().setVisibility(visibility);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<SecondModel> getItems() {
        return mItems;
    }

    protected static String generateLabel(int adapterPosition, UUID uuid) {
        return String.format(
                "position = %d; (%s)",
                adapterPosition,
                uuid.toString()
        );
    }

    public static class SecondModel {

        private final long id;

        public SecondModel(@NonNull long id) {
            this.id = id;
        }

        @NonNull
        public Long getId() {
            return id;
        }

    }

    protected static class PayloadGenerator implements Helper.IPayloadGenerator<SecondViewHolder, Long> {

        @Override
        public Object onCheck(TargetItem<SecondViewHolder, Long> item) {
            final Integer oldY, newY;

            {
                int[] newLoc = new int[2];
                item.getViewHolder().getIcon().getLocationInWindow(newLoc);
                newY = newLoc[1];
            }

            if (item.getPair() != null) {
                int[] oldLoc = new int[2];
                item.getPair().getViewHolder().getIcon().getLocationInWindow(oldLoc);
                oldY = oldLoc[1];
            } else {
                oldY = null;
            }

            return new MyPayload(oldY, newY);
        }

        @Override
        public Object onUncheck(TargetItem<SecondViewHolder, Long> item) {
            return null;
        }

    }

    protected static class MyPayload {

        private final Integer oldY;
        private final Integer newY;

        public MyPayload(@Nullable Integer oldY, @Nullable Integer newY) {
            this.oldY = oldY;
            this.newY = newY;
        }

        public int calcDiffY() {
            return (oldY != null && newY != null) ? oldY - newY : 0;
        }

    }

}
