package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.support.annotation.Nullable;

import java.util.List;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/11.
 */
public interface IItemResolver<T extends ICheckableModel> {

    @Nullable
    T resolveItemByItemsPosition(int itemsPosition);

    List<T> resolveCheckedItems();

    int resolveItemsPosition(T item);
}
