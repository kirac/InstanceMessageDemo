package com.kira.common.widget.recyler;

/**
 * Created by kira on 2018/1/7/007.
 */

public interface IAdapterCallBack<Data> {
    void update(Data data,RecylerAdapter.ViewHolder<Data> holder);
}
