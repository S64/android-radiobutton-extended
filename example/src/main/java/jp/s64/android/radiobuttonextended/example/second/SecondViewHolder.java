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

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.core.widget.NonClickableCompoundFrameLayout;
import jp.s64.android.radiobuttonextended.core.widget.RadioFrameLayout;
import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedAdapter;

public class SecondViewHolder extends RecyclerView.ViewHolder implements RadioGroupedAdapter.IRadioGroupedViewHolder<SecondViewHolder, Long> {

    private final UUID mInstanceUuid = UUID.randomUUID();

    private final RadioFrameLayout mRadioLayout;
    private final TextView mLabel;
    private final ImageView mIcon;

    public SecondViewHolder(View itemView) {
        super(itemView);
        mRadioLayout = (RadioFrameLayout) itemView.findViewById(R.id.item_container);
        mLabel = (TextView) itemView.findViewById(R.id.label);
        mIcon = (ImageView) itemView.findViewById(R.id.icon);
    }

    @Nullable
    private Long mRadioKey;

    public UUID getUuid() {
        return mInstanceUuid;
    }

    @Nullable
    @Override
    public Long getRadioKey() {
        return mRadioKey;
    }

    @Override
    public void setRadioItem(@Nullable Long item) {
        mRadioKey = item;
    }

    @Override
    public void setCheckedChangeListener(final IOnCheckedChangeListener<SecondViewHolder, Long> listener) {
        mRadioLayout.setOnCheckedChangeListener(new CompoundFrameLayout.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(NonClickableCompoundFrameLayout compoundFrameLayout, boolean checked) {
                listener.onCheckedChange(SecondViewHolder.this, compoundFrameLayout, checked);
            }

        });
    }

    @Override
    public void setIsChecked(boolean isChecked) {
        mRadioLayout.setChecked(isChecked);
    }

    public TextView getLabel() {
        return mLabel;
    }

    public ImageView getIcon() {
        return mIcon;
    }

}
