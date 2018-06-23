package com.z.zmusicplayer.fragment.MVFragment;

import com.z.zmusicplayer.bean.AreaBean;
import com.z.zmusicplayer.fragment.BasePresenter;
import com.z.zmusicplayer.fragment.BaseView;

import java.util.List;

/**
 * Created by z on 2017/7/16.
 */

public interface MVFragmentContract {

    interface MVPresenter extends BasePresenter{

    }
    interface View extends BaseView<MVPresenter>{

        void showData (List<AreaBean> areaBeenList);
        void showError();

    }


}
