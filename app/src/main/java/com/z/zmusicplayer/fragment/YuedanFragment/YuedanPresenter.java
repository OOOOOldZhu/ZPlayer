package com.z.zmusicplayer.fragment.YuedanFragment;

import com.z.zmusicplayer.bean.YueDanBean;
import com.z.zmusicplayer.http.HttpManager;
import com.z.zmusicplayer.http.MyHttpCallback;
import com.z.zmusicplayer.utils.URLProviderUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by z on 2014/7/9.
 */

public class YuedanPresenter implements YuedanContract.YuedanPresenter {
    YuedanContract.YuedanView view;

    public YuedanPresenter(YuedanContract.YuedanView view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getData(int offset, int size) {
        HttpManager.getInstance().doGet(URLProviderUtil.getMainPageUrl(offset, size), new MyHttpCallback() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse() {

            }

            @Override
            public void onSuccess(Response response, Object o) {
                List<YueDanBean.PlayListsBean> playLists = ((YueDanBean) o).getPlayLists();
                view.showData(playLists);
            }


            @Override
            public void onError(Response response, String errorMsg) {

            }
        });
    }


}
