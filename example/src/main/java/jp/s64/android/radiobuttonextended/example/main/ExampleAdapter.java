package jp.s64.android.radiobuttonextended.example.main;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.IOnCheckedChangeListener;
import jp.s64.android.radiobuttonextended.recycler.adapter.RadioGroupedAdapter;
import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/14.
 */

public class ExampleAdapter extends RadioGroupedAdapter<ExampleViewHolder, ExampleAdapter.ExampleModel, Long> {

    private final List<ExampleModel> mItems = new ArrayList<>();

    public ExampleAdapter(IOnCheckedChangeListener listener) {
        super(ExampleViewHolder.class, listener, new PayloadGenerator());
    }

    @Override
    public ExampleViewHolder onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_example, parent, false);
        return new ExampleViewHolder(view);
    }

    @Override
    public void onBindRadioGroupedViewHolder(ExampleViewHolder holder, int position) {
        holder.setBoundItem(mItems.get(position));
        holder.getRadioButton().setText(generateLabel(position));
    }

    @Override
    public void onBindRadioGroupedViewHolder(ExampleViewHolder holder, int position, List<Object> payloads) {
        holder.setBoundItem(mItems.get(position));
        holder.getRadioButton().setText(generateLabel(position));
    }

    protected static String generateLabel(int adapterPosition) {
        return String.format(
                "position = %d; (%s)",
                adapterPosition,
                UUID.randomUUID().toString()
        );
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<ExampleModel> getItems() {
        return mItems;
    }

    public static class ExampleModel implements ICheckableModel<Long> {

        private long id;

        public ExampleModel(long id) {
            this.id = id;
        }

        @NonNull
        @Override
        public Long getId() {
            return id;
        }

    }

    protected static class PayloadGenerator implements Helper.IPayloadGenerator<ExampleViewHolder, ExampleModel, Long> {


        @Override
        public Object onCheck(TargetItem<ExampleViewHolder, ExampleModel, Long> item) {
            return null;
        }

        @Override
        public Object onUncheck(TargetItem<ExampleViewHolder, ExampleModel, Long> item) {
            return null;
        }

    }

}
