package jp.s64.android.radiobuttonextended.example.second;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.core.widget.RadioFrameLayout;
import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckedChangeListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.RadioGroupedAdapter;

/**
 * Created by shuma on 2017/04/14.
 */
public class SecondViewHolder extends RecyclerView.ViewHolder implements RadioGroupedAdapter.IRadioGroupedViewHolder<SecondAdapter.SecondModel, Long> {

    private final RadioFrameLayout mRadioLayout;
    private final TextView mLabel;
    private final IListener mListener;

    public SecondViewHolder(View itemView, IListener listener) {
        super(itemView);
        mRadioLayout = (RadioFrameLayout) itemView.findViewById(R.id.item_container);
        mLabel = (TextView) itemView.findViewById(R.id.label);
        mListener = listener;
    }

    private SecondAdapter.SecondModel mBoundItem;

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
                mListener.onCheckedChange(SecondViewHolder.this, mRadioLayout, checked);
                listener.onCheckedChange(compoundFrameLayout, checked);
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

    public interface IListener {

        void onCheckedChange(SecondViewHolder holder, CompoundFrameLayout view, boolean isChecked);

    }
}
