package com.z.zmusicplayer.http;

import android.app.DownloadManager;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by z on 2014/7/2.
 */

public class HttpManager {
    private OkHttpClient okHttpClient;
    private static HttpManager httpmanager;
    private Gson gson;
    private Handler handler;

    private HttpManager() {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static HttpManager getInstance() {
        if (httpmanager == null) {
            synchronized (HttpManager.class) {
                httpmanager = new HttpManager();
            }
        }
        return httpmanager;
    }

    //不知道是 get 请求 还是post请求
    private void doRequest(Request request, final MyHttpCallback callback) {

        callback.onBeforeRequest( request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final Object o = gson.fromJson(json, callback.type);

                    //发送一个new Runnable对象，run方法执行在主线程中
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(response, o);
                        }
                    });


                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(response, " service error ! ");
                        }
                    });
                }
            }
        });
    }


    public void doGet(String url,MyHttpCallback callback){
        Request request = new Request.Builder().url(url).get().build();
        doRequest(request,callback);
    }
    public void doPost( String url, Map<String,String> params, MyHttpCallback callback){

        // 创建表单
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry< String,String > entry: params.entrySet()) {
            formBuilder.add(entry.getKey(),entry.getValue());
        }

        RequestBody body= formBuilder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        doRequest(request,callback);
    }

}
