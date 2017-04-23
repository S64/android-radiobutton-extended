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

package jp.s64.android.radiobuttonextended.example.main;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.UUID;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedAdapter;

public class ExampleViewHolder extends RecyclerView.ViewHolder implements RadioGroupedAdapter.IRadioGroupedViewHolder<ExampleViewHolder, Long> {

    private final UUID mInstanceUuid = UUID.randomUUID();

    private final RadioButton mRadioButton;

    @Nullable
    private Long mRadioKey;

    protected ExampleViewHolder(View itemView) {
        super(itemView);
        mRadioButton = (RadioButton) itemView.findViewById(R.id.radiobutton);
    }

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
    public void setCheckedChangeListener(final IOnCheckedChangeListener<ExampleViewHolder, Long> listener) {
        mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckedChange(ExampleViewHolder.this, buttonView, isChecked);
            }

        });
    }

    @Override
    public void setIsChecked(boolean isChecked) {
        mRadioButton.setChecked(isChecked);
    }

    public RadioButton getRadioButton() {
        return mRadioButton;
    }

}
