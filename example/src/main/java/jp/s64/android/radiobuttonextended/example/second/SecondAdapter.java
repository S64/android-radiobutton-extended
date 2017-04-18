package jp.s64.android.radiobuttonextended.example.second;

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

public class SecondAdapter extends RadioGroupedAdapter<SecondViewHolder, SecondAdapter.SecondModel, Long> {

    private final List<SecondModel> mItems = new ArrayList<>();

    public SecondAdapter(IOnCheckedChangeListener listener) {
        super(SecondViewHolder.class, listener, new PayloadGenerator());
    }

    @Override
    public SecondViewHolder onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second, parent, false);
        return new SecondViewHolder(view);
    }

    @Override
    public void onBindRadioGroupedViewHolder(SecondViewHolder holder, int position) {
        holder.setBoundItem(mItems.get(position));
        holder.getLabel().setText(generateLabel(position, holder.getUuid()));
    }

    @Override
    public void onBindRadioGroupedViewHolder(SecondViewHolder holder, int position, List<Object> payloads) {
        holder.setBoundItem(mItems.get(position));
        holder.getLabel().setText(generateLabel(position, holder.getUuid()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<SecondModel> getItems() {
        return mItems;
    }

    protected static String generateLabel(int adapterPosition, UUID uuid) {
        return String.format(
                "position = %d; (%s)",
                adapterPosition,
                uuid.toString()
        );
    }

    public static class SecondModel implements ICheckableModel<Long> {

        private final long id;

        public SecondModel(@NonNull long id) {
            this.id = id;
        }

        @NonNull
        @Override
        public Long getId() {
            return id;
        }

    }

    protected static class PayloadGenerator implements Helper.IPayloadGenerator<SecondViewHolder, SecondModel, Long> {

        @Override
        public Object onCheck(TargetItem<SecondViewHolder, SecondModel, Long> item) {
            return null;
        }

        @Override
        public Object onUncheck(TargetItem<SecondViewHolder, SecondModel, Long> item) {
            return null;
        }

    }

}
