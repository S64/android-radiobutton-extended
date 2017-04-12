package jp.s64.android.radiobuttonextended.example.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.Helper;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedRecyclerAdapter;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IOnCheckChangedListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.IRadioButtonBinder;
import jp.s64.android.radiobuttonextended.recycler.util.CompoundOnCheckedChangeListener;

/**
 * Created by shuma on 2017/04/10.
 */

public class ExampleAdapter extends RadioGroupedRecyclerAdapter<ExampleAdapter.ExampleModel, ExampleAdapter.ViewHolder> {

    public ExampleAdapter(final IListener listener, Helper.AbsLayoutItemResolver<ExampleModel> resolver) {
        super(
                new IRadioButtonBinder() {

                    @Nullable
                    @Override
                    public <V extends View & Checkable> V getCheckable(RecyclerView.ViewHolder holder) {
                        ViewHolder vh = (ViewHolder) holder;
                        return (V) vh.getRadioButton();
                    }

                    @Override
                    public boolean setOnCheckListenerIfAvailable(RecyclerView.ViewHolder holder, final IOnCheckChangedListener adapterListener) {
                        final ViewHolder vh = (ViewHolder) holder;
                        vh.getRadioButton().setOnCheckedChangeListener(new CompoundOnCheckedChangeListener(adapterListener) {

                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.getRadioButton().setText(String.format(
                "position = %d",
                position
        ));
    }

    @Override
    public ViewHolder onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_example, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ViewHolder(View itemView) {
            super(itemView);
        }

        public RadioButton getRadioButton() {
            return (RadioButton) itemView.findViewById(R.id.radiobutton);
        }

    }

    public static class ExampleModel {

    }

    public interface IListener {

        void onCheckChanged(ExampleAdapter.ViewHolder holder, CompoundButton view, boolean checked);

    }

}
