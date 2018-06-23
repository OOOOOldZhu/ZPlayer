package com.z.zmusicplayer.fragment.mainfragment;

import com.z.zmusicplayer.bean.VideoBean;
import com.z.zmusicplayer.fragment.BasePresenter;
import com.z.zmusicplayer.fragment.BaseView;

import java.util.List;

/**
 * Created by z on 2014/7/2.
 */

public interface MainfragmentContract {

     interface Presenter extends BasePresenter{};

     interface View<P extends BasePresenter> extends BaseView<Presenter>{

        void showData(List<VideoBean> videoBeanList);

        void showError(String errorMsg);


     };

}
