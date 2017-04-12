package jp.s64.android.radiobuttonextended.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import jp.s64.android.radiobuttonextended.example.activity.SecondActivity;
import jp.s64.android.radiobuttonextended.example.adapter.ExampleAdapter;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.Helper;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_CHECKED_POSITION = "jp.s64.android.radiobuttonextended.example.MainActivity.STATE_CHECKED_POSITION";

    private RecyclerView mRecyclerView;

    private ExampleAdapter mAdapter;

    private final ExampleAdapter.IListener mListener = new ExampleAdapter.IListener() {

        @Override
        public void onCheckChanged(ExampleAdapter.ViewHolder holder, CompoundButton view, boolean checked) {
            if (view.isPressed()) {
                Toast.makeText(
                        MainActivity.this,
                        String.format(
                                "position=%d; label=%s; isChecked=%b;",
                                holder.getAdapterPosition(),
                                view.getText(),
                                checked
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
            mRecyclerView.setAdapter(mAdapter = new ExampleAdapter(
                    mListener,
                    new jp.s64.android.radiobuttonextended.recycler.adapter.Helper.AbsLayoutItemResolver<ExampleAdapter.ExampleModel>() {

                        @Nullable
                        @Override
                        public ExampleAdapter.ExampleModel resolveRawItemByLayoutPosition(int layoutPosition) {
                            return mAdapter.get(layoutPosition);
                        }

                    }
            ));
        }
        for (int i = 0; i < 100; i++) {
            mAdapter.add(new ExampleAdapter.ExampleModel());
        }
        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(STATE_CHECKED_POSITION, Helper.POSITION_NOT_CHECKED);
            mAdapter.setCheckedPosition(position);
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
        {
            outState.putInt(STATE_CHECKED_POSITION, mAdapter.getCheckedItemsPosition());
        }
    }

}
