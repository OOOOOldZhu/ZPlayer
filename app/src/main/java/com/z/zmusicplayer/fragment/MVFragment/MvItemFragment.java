package com.z.zmusicplayer.fragment.MVFragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.VideoBean;
import com.z.zmusicplayer.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by z on 2017/7/16.
 */

public class MvItemFragment extends BaseFragment implements MVItemContract.View {


    @InjectView(R.id.recyclerview_yuedanfragment)
    RecyclerView recyclerview;
    @InjectView(R.id.swiperefresh_yuedanfragment)
    SwipeRefreshLayout swiperefresh;


    private MVItemContract.MVItemPresenter presenter;
    private List<VideoBean> videoBeens = new ArrayList<>();
    private boolean refreshing;
    private int lastItemIndex;
    private boolean hasMoreData;
    private MvItemAdapter adapter;

    @Override
    public void setPresenter(MVItemContract.MVItemPresenter presenter) {
       this.presenter=presenter;
    }

    @Override
    public void showData(List<VideoBean> videos) {
        videoBeens.addAll(videos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected int getLayoutId() {
        // mv 的fragment 和yuedanfragment 一模一样
        return R.layout.yuedanfagment;
    }

    @Override
    protected void init() {
        getPicSize(640, 360);
        adapter = new MvItemAdapter(videoBeens, MvItemFragment.this.getActivity(), mWidth, mHeight);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(adapter);
        Bundle arguments = MvItemFragment.this.getArguments();
        String code = arguments.getString("code");
        new MvItemPresenter(this);
        presenter.getData(code,startIndex,size);

    }

    public static MvItemFragment getInstance(String code) {
        MvItemFragment mvItemFragment = new MvItemFragment();
        Bundle args = new Bundle();
        args.putString("code", code);
        mvItemFragment.setArguments(args);
        return mvItemFragment;

    }



}
