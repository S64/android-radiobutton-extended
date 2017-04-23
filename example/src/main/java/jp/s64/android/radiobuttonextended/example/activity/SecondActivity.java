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

package jp.s64.android.radiobuttonextended.example.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;
import android.widget.Toast;

import java.util.List;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.example.second.SecondAdapter;
import jp.s64.android.radiobuttonextended.example.second.SecondViewHolder;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;

public class SecondActivity extends AppCompatActivity {

    private static final String STATE_CHECKED_ID = "jp.s64.android.radiobuttonextended.example.activity.SecondActivity";

    private RecyclerView mRecyclerView;

    private SecondAdapter mAdapter;

    private final IOnCheckedChangeListener<SecondViewHolder, Long> mListener = new IOnCheckedChangeListener<SecondViewHolder, Long>() {

        @Override
        public <V extends View & Checkable> void onCheckedChange(SecondViewHolder holder, V view, boolean isChecked) {
            if (view.isPressed()) {
                Toast.makeText(
                        SecondActivity.this,
                        String.format(
                                "position=%d; label=%s; isChecked=%b;",
                                holder.getAdapterPosition(),
                                holder.getLabel().getText(),
                                isChecked
                        ),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        }
        {
            mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
                @Override

                public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                    return true;
                }

                @Override
                public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
                    return true;
                }

            });
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter = new SecondAdapter(mListener));
        }
        {
            for (int i = 0; i < 100; i++) {
                mAdapter.getItems().add(new SecondAdapter.SecondModel(i));
            }
            mAdapter.notifyDataSetChanged();
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CHECKED_ID)) {
            long checked = savedInstanceState.getLong(STATE_CHECKED_ID);
            mAdapter.setCheckedId(checked);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Long id = mAdapter.getCheckedId();
        if (id != null) {
            outState.putLong(STATE_CHECKED_ID, id);
        }
    }

}
