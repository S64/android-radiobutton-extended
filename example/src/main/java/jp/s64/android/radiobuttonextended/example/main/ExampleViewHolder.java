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

/**
 * Created by shuma on 2017/04/14.
 */
public class ExampleViewHolder extends RecyclerView.ViewHolder implements RadioGroupedAdapter.IRadioGroupedViewHolder<Long> {

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
    public void setCheckedChangeListener(final IOnCheckedChangeListener listener) {
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
