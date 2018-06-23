package com.z.zmusicplayer.fragment;

/**
 * Created by z on 2017/7/2.
 */

public interface BasePresenter {


    /*
       @param offset  分页获取数据的起始数据索引
       @param  size   每次获取数据的条目数

    */

    void getData(int offset,int size);


}
