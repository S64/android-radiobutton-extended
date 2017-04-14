package jp.s64.android.radiobuttonextended.recycler.model;

import android.support.annotation.NonNull;

/**
 * Created by shuma on 2017/04/14.
 */

public interface ICheckableModel<K extends Comparable<K>> {

    @NonNull
    K getId();
    
}
