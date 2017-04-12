package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/11.
 */
public interface IItemNotifier<T extends ICheckableModel> {

    void notifyItemChanged(T item);

}
