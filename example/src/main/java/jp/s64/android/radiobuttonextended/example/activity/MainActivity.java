package jp.s64.android.radiobuttonextended.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Checkable;
import android.widget.Toast;

import java.util.List;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.example.main.ExampleAdapter;
import jp.s64.android.radiobuttonextended.example.main.ExampleViewHolder;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_CHECKED_ID = "jp.s64.android.radiobuttonextended.example.activity.MainActivity.STATE_CHECKED_ID";

    private RecyclerView mRecyclerView;

    private ExampleAdapter mAdapter;

    private final IOnCheckedChangeListener<ExampleViewHolder, Long> mListener = new IOnCheckedChangeListener<ExampleViewHolder, Long>() {
        @Override
        public <V extends View & Checkable> void onCheckedChange(ExampleViewHolder vh, V view, boolean isChecked) {
            if (view.isPressed()) {
                Toast.makeText(
                        MainActivity.this,
                        String.format(
                                "position=%d; label=%s; isChecked=%b;",
                                vh.getAdapterPosition(),
                                vh.getRadioButton().getText(),
                                isChecked
                        ),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        }
        {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter = new ExampleAdapter(mListener));
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
        }
        {
            for (int i = 0; i < 100; i++) {
                mAdapter.getItems().add(new ExampleAdapter.ExampleModel(i));
            }
            mAdapter.notifyDataSetChanged();
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CHECKED_ID)) {
            long id = savedInstanceState.getLong(STATE_CHECKED_ID);
            mAdapter.setCheckedId(id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_second:
                startActivity(new Intent(this, SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Long checked = mAdapter.getCheckedId();
        if (checked != null) {
            outState.putLong(STATE_CHECKED_ID, checked);
        }
    }

}
