package com.z.zmusicplayer.fragment.YuedanFragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.YueDanBean;
import com.z.zmusicplayer.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by z on 2017/6/25.
 */

public class YuedanFragment extends BaseFragment implements YuedanContract.YuedanView {

    @InjectView(R.id.recyclerview_yuedanfragment)
    RecyclerView recyclerviewYuedanfragment;
    @InjectView(R.id.swiperefresh_yuedanfragment)
    SwipeRefreshLayout swiperefreshYuedanfragment;


    private YuedanAdapter adapter;
    List<YueDanBean.PlayListsBean> list = new ArrayList<>();
    boolean hasMoreDate;
    private YuedanContract.YuedanPresenter presenter;
    private boolean isRefreshing;
    private int lastVisibleIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.yuedanfagment;
    }

    @Override
    protected void init() {
        //
        getPicSize(640, 360);
        adapter = new YuedanAdapter(list, this.getActivity(), mWidth, mHeight);
        recyclerviewYuedanfragment.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerviewYuedanfragment.setAdapter(adapter);
        recyclerviewYuedanfragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleIndex == list.size() - 1 && hasMoreDate) {
                    presenter.getData(startIndex, size);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleIndex = ((LinearLayoutManager) recyclerviewYuedanfragment.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        swiperefreshYuedanfragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                presenter.getData(startIndex, size);
            }
        });
        /*      public YuedanPresenter(YuedanContract.YuedanView view) {
                    this.view = view;
                    view.setPresenter(this);
                }
        *///在presenter的构造方法中可以看出 设置这个fragment 对象的presenter为自己( presenter )
        // 所以 YuedanFragment 中的成员变量presenter 就是这个new 出来的 presenter对象
        new YuedanPresenter(this);
        presenter.getData(startIndex, size);

    }

    @Override
    public void setPresenter(YuedanContract.YuedanPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showData(List<YueDanBean.PlayListsBean> playLists) {
        if (isRefreshing == true) {
            isRefreshing = false;
            list.clear();
        }
        if (playLists.size() > size) {
            hasMoreDate = true;
        } else {
            hasMoreDate = false;
        }
        //更新获取数据的起始索引
        if (hasMoreDate == true) {
            startIndex += playLists.size();
        } else {
            startIndex = 0;
        }
        list.addAll(playLists);
        adapter.notifyDataSetChanged();
        //数据展示完毕  progress 停止转动
        swiperefreshYuedanfragment.setRefreshing(false);


    }


    @Override
    public void showError() {

    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }*/
}
