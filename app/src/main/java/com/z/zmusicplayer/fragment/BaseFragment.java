package com.z.zmusicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by z on 2014/6/25.
 */

public abstract class BaseFragment extends Fragment {

    public View rootView;
    protected int mWidth;
    protected int mHeight;
    protected final int size = 10;
    protected int startIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //   rootView如果为空，说明是第一次创建
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);
        }

        // Unbinder unbinder=ButterKnife.findById(BaseFragment.this.getActivity(),rootView);
        init();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //init();
    }

    protected abstract int getLayoutId();

    protected abstract void init();



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }

    protected void getPicSize(int width ,int height){
        mWidth =getActivity().getWindowManager().getDefaultDisplay().getWidth();
        mHeight = height*mWidth/width;
    }

}
