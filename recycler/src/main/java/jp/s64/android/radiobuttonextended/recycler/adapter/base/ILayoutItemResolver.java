package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.support.annotation.Nullable;

import jp.s64.android.radiobuttonextended.recycler.model.ICheckableModel;

/**
 * Created by shuma on 2017/04/12.
 */

public interface ILayoutItemResolver<T extends ICheckableModel> {

    @Nullable
    T resolveItemByLayoutPosition(int layoutPosition);

}
