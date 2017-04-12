package jp.s64.android.radiobuttonextended.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.example.adapter.SecondAdapter;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.Helper;

/**
 * Created by shuma on 2017/04/10.
 */

public class SecondActivity extends AppCompatActivity {

    private static final String STATE_CHECKED_POSITION = "jp.s64.android.radiobuttonextended.example.activity.SecondActivity";

    private RecyclerView mRecyclerView;

    private SecondAdapter mAdapter;

    private final SecondAdapter.IListener mListener = new SecondAdapter.IListener() {

        @Override
        public void onCheckChanged(SecondAdapter.ViewHolder holder, CompoundFrameLayout view, boolean checked) {
            if (view.isPressed()) {
                Toast.makeText(
                        SecondActivity.this,
                        String.format(
                                "position=%d; label=%s; isChecked=%b;",
                                holder.getAdapterPosition(),
                                ((TextView) view.findViewById(R.id.label)).getText(),
                                checked
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
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter = new SecondAdapter(
                    mListener,
                    new jp.s64.android.radiobuttonextended.recycler.adapter.Helper.AbsLayoutItemResolver<SecondAdapter.SecondModel>() {

                        @Nullable
                        @Override
                        public SecondAdapter.SecondModel resolveRawItemByLayoutPosition(int layoutPosition) {
                            return mAdapter.get(layoutPosition);
                        }

                    }
            ));
        }
        for (int i = 0; i < 100; i++) {
            mAdapter.add(new SecondAdapter.SecondModel());
        }
        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(STATE_CHECKED_POSITION, Helper.POSITION_NOT_CHECKED);
            mAdapter.setCheckedPosition(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        {
            outState.putInt(STATE_CHECKED_POSITION, mAdapter.getCheckedItemsPosition());
        }
    }

}
