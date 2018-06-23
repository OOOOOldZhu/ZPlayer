package com.z.zmusicplayer.fragment.mainfragment;

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
 * Created by z on 2014/7/2.
 */

public class MainFragmentPresenter implements MainfragmentContract.Presenter{
    MainfragmentContract.View view;

    public MainFragmentPresenter(MainfragmentContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getData(int offset, int size) {

        HttpManager.getInstance().doGet(URLProviderUtil.getMainPageUrl(offset, size), new MyHttpCallback<List<VideoBean>>() {
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
            public void onSuccess(Response response, List<VideoBean> videoBeanList) {
                view.showData(videoBeanList);
            }


            @Override
            public void onError(Response response, String errorMsg) {

            }
        });


    }




}
