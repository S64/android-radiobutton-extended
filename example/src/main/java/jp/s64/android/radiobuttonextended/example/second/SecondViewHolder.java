package jp.s64.android.radiobuttonextended.example.second;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

    public ImageView getIcon() {
        return mIcon;
    }

}
