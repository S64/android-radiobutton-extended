package jp.s64.android.radiobuttonextended.example.second;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jp.s64.android.radiobuttonextended.example.R;
import jp.s64.android.radiobuttonextended.recycler.adapter.base.RadioGroupedAdapter;
import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/14.
 */

public class SecondAdapter extends RadioGroupedAdapter<SecondViewHolder, SecondAdapter.SecondModel, Long> {

    private final List<SecondModel> mItems = new ArrayList<>();
    private final SecondViewHolder.IListener mListener;

    public SecondAdapter(SecondViewHolder.IListener listener) {
        super(SecondViewHolder.class);
        mListener = listener;
    }

    @Override
    public SecondViewHolder onCreateRadioGroupedViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second, parent, false);
        return new SecondViewHolder(view, mListener);
    }

    @Override
    public void onBindRadioGroupedViewHolder(SecondViewHolder holder, int position) {
        holder.setBoundItem(mItems.get(position));
        holder.getLabel().setText(String.format(
                "position = %d; (%s)",
                position,
                UUID.randomUUID().toString()
        ));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<SecondModel> getItems() {
        return mItems;
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

}
