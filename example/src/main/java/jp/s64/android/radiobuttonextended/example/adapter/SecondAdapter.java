package jp.s64.android.radiobuttonextended.example.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import jp.s64.android.radiobuttonextended.core.widget.CompoundFrameLayout;
import jp.s64.android.radiobuttonextended.core.widget.RadioFrameLayout;
import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.Helper;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedRecyclerAdapter;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckChangedListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IRadioButtonBinder;
import jp.s64.android.radiobuttonextended.recycler.util.CompoundFrameOnCheckedChangeListener;

/**
 * Created by shuma on 2017/04/10.
 */

public class SecondAdapter extends RadioGroupedRecyclerAdapter<SecondAdapter.SecondModel, SecondAdapter.ViewHolder> {

    public SecondAdapter(final SecondAdapter.IListener listener, Helper.AbsLayoutItemResolver<SecondModel> resolver) {
        super(
                new IRadioButtonBinder() {

                    @Nullable
                    @Override
                    public <V extends View & Checkable> V getCheckable(RecyclerView.ViewHolder holder) {
                        SecondAdapter.ViewHolder vh = (SecondAdapter.ViewHolder) holder;
                        return (V) vh.getRadioButton();
                    }

                    @Override
                    public boolean setOnCheckListenerIfAvailable(RecyclerView.ViewHolder holder, IOnCheckChangedListener adapterListener) {
                        final SecondAdapter.ViewHolder vh = (SecondAdapter.ViewHolder) holder;
                        vh.getRadioButton().setOnCheckedChangeListener(new CompoundFrameOnCheckedChangeListener(adapterListener) {

                            @Override
                            public void onCheckedChanged(CompoundFrameLayout compoundButton, boolean b) {
                                super.onCheckedChanged(compoundButton, b);
                                listener.onCheckChanged(vh, compoundButton, b);
                            }

                        });
                        return true;
                    }

                },
                resolver
        );
    }

    @Override
    public ViewHolder onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second, parent, false);
        return new SecondAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SecondAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.getTextView().setText(String.format(
                "position = %d",
                position
        ));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ViewHolder(View itemView) {
            super(itemView);
        }

        public RadioFrameLayout getRadioButton() {
            return (RadioFrameLayout) itemView.findViewById(R.id.item_container);
        }

        public TextView getTextView() {
            return (TextView) itemView.findViewById(R.id.label);
        }

    }

    public static class SecondModel {

    }

    public interface IListener {

        void onCheckChanged(SecondAdapter.ViewHolder holder, CompoundFrameLayout view, boolean checked);

    }

}
