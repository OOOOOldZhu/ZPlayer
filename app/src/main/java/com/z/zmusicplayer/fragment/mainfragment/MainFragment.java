package com.z.zmusicplayer.fragment.mainfragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.VideoBean;
import com.z.zmusicplayer.fragment.BaseFragment;
import com.z.zmusicplayer.http.HttpManager;
import com.z.zmusicplayer.http.MyHttpCallback;
import com.z.zmusicplayer.utils.URLProviderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.z.zmusicplayer.fragment.mainfragment.MainfragmentContract.*;

/**
 * Created by z on 2014/6/25.
 */

public class MainFragment extends BaseFragment implements MainfragmentContract.View<MainfragmentContract.Presenter> {
    MainfragmentContract.Presenter presenter;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh;
    private  MainFragmentAdapter adapter;
    private List<VideoBean> videoBeans = new ArrayList<>();
    private boolean refreshing;
    private int lastItemIndex;
    private boolean hasMoreData;
    int startIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.mainfragment;
    }

    @Override
    protected void init() {
        refresh= (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        recyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerview);

        // getPicSize(540,640);
        getPicSize(540,300);
        adapter= new MainFragmentAdapter(videoBeans,getActivity(),mWidth,mHeight);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing=true;

                presenter.getData(0,size);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(lastItemIndex == videoBeans.size()-1&& hasMoreData){
                    presenter.getData(startIndex,size);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastItemIndex = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        new MainFragmentPresenter(this);
        presenter.getData(startIndex,size);
    }



    @Override
    public void showData(List<VideoBean> videoBeanList) {
        if(refreshing){
            //成员变量 videoBeans 集合 ;
            videoBeans.clear();
            refreshing = false ;
        }
        if(videoBeanList.size()>0){
            hasMoreData = true;
        }else {
            hasMoreData = false ;
        }
        startIndex = startIndex +videoBeanList.size();
        videoBeans.addAll(videoBeanList);
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
