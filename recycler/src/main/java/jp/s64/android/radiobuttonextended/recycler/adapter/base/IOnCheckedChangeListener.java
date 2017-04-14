package jp.s64.android.radiobuttonextended.recycler.adapter.base;

import android.view.View;
import android.widget.Checkable;

/**
 * Created by shuma on 2017/04/11.
 */
public interface IOnCheckedChangeListener {

    <V extends View & Checkable> void onCheckedChange(V checkable, boolean isChecked);

}
