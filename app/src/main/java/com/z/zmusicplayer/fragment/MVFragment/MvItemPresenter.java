package com.z.zmusicplayer.fragment.MVFragment;

import com.z.zmusicplayer.bean.MvListBean;
import com.z.zmusicplayer.bean.VideoBean;
import com.z.zmusicplayer.http.HttpManager;
import com.z.zmusicplayer.http.MyHttpCallback;
import com.z.zmusicplayer.utils.URLProviderUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by z on 2017/7/16.
 */

public class MvItemPresenter implements MVItemContract.MVItemPresenter{
   MVItemContract.View view;

    public MvItemPresenter(MVItemContract.View view) {
        this.view = view;
        view.setPresenter(MvItemPresenter.this);
    }

    @Override
    public void getData(int offset, int size) {

    }

    @Override
    public void getData(String code, int offset, int size) {
        HttpManager.getInstance().doGet(URLProviderUtil.getMVListUrl(code, offset, size), new MyHttpCallback<MvListBean>() {
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
            public void onSuccess(Response response, MvListBean mvListBean) {
                view.showData(mvListBean.getVideos());
            }


            @Override
            public void onError(Response response, String errorMsg) {

            }
        });
    }




}
