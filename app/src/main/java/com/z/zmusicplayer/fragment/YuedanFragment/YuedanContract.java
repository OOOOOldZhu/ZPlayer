package com.z.zmusicplayer.fragment.YuedanFragment;

import com.z.zmusicplayer.bean.YueDanBean;
import com.z.zmusicplayer.fragment.BasePresenter;
import com.z.zmusicplayer.fragment.BaseView;

import java.util.List;

/**
 * Created by z on 2014/7/9.
 */

public interface YuedanContract {
    interface YuedanPresenter extends BasePresenter {

    };

    interface YuedanView extends BaseView<YuedanPresenter> {

        void showData(List<YueDanBean.PlayListsBean> playLists);
        void showError();

    };


}
