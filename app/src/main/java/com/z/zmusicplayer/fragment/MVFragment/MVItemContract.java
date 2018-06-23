package com.z.zmusicplayer.fragment.MVFragment;

import com.z.zmusicplayer.bean.VideoBean;
import com.z.zmusicplayer.fragment.BasePresenter;
import com.z.zmusicplayer.fragment.BaseView;

import java.util.List;

/**
 * Created by z on 2017/7/16.
 */

public interface MVItemContract {

    interface MVItemPresenter extends BasePresenter{
        void getData(String code,int offset,int size);

    }
    interface View extends BaseView<MVItemPresenter>{

        void showData(List<VideoBean> videos);
        void showError(String msg);

    }



}
