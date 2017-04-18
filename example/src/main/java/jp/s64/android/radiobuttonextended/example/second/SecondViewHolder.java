package jp.s64.android.radiobuttonextended.example.second;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.core.widget.RadioFrameLayout;
import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedAdapter;

/**
 * Created by shuma on 2017/04/14.
 */
public class SecondViewHolder extends RecyclerView.ViewHolder implements RadioGroupedAdapter.IRadioGroupedViewHolder<SecondAdapter.SecondModel, Long> {

    private final UUID mInstanceUuid = UUID.randomUUID();

    private final RadioFrameLayout mRadioLayout;
    private final TextView mLabel;

    public SecondViewHolder(View itemView) {
        super(itemView);
        mRadioLayout = (RadioFrameLayout) itemView.findViewById(R.id.item_container);
        mLabel = (TextView) itemView.findViewById(R.id.label);
    }

    private SecondAdapter.SecondModel mBoundItem;

    public UUID getUuid() {
        return mInstanceUuid;
    }

    @Nullable
    @Override
    public SecondAdapter.SecondModel getBoundItem() {
        return mBoundItem;
    }

    @Override
    public void setBoundItem(@Nullable SecondAdapter.SecondModel item) {
        mBoundItem = item;
    }

    @Override
    public void setCheckedChangeListener(final IOnCheckedChangeListener listener) {
        mRadioLayout.setOnCheckedChangeListener(new CompoundFrameLayout.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundFrameLayout compoundFrameLayout, boolean checked) {
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

}
